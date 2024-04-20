/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.netbeans.examples.modules.povproject;

import java.awt.BorderLayout;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.examples.api.povray.RendererService;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;

/**
 *
 * @author mathe
 */
public final class PovrayProject implements Project {
    
    public static final String SCENES_DIR = "scenes"; //NOI18N
    public static final String IMAGES_DIR = "images"; //NOI18N
    public static final String KEY_MAINFILE = "main.file";

    private final FileObject projectDir;
    private final ProjectState state;
    private Lookup lkp;
    
    LogicalViewProvider logicalView = new PovrayLogicalView(this);

    public PovrayProject(FileObject projectDir, ProjectState state) {
        this.projectDir = projectDir;
        this.state = state;
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    FileObject getScenesFolder(boolean create) {
        FileObject result =
            projectDir.getFileObject(SCENES_DIR);

        if (result == null && create) {
            try {
                result = projectDir.createFolder (SCENES_DIR);
            } catch (IOException ioe) {
                Exceptions.printStackTrace(ioe);
            }
        }
        return result;
    }

    FileObject getImagesFolder(boolean create) {
        FileObject result =
            projectDir.getFileObject(IMAGES_DIR);
        if (result == null && create) {
            try {
                result = projectDir.createFolder (IMAGES_DIR);
            } catch (IOException ioe) {
                Exceptions.printStackTrace(ioe);
            }
        }
        return result;
    }

    @Override
    public Lookup getLookup() {
        if (lkp == null) {
            lkp = Lookups.fixed(new Object[]{
                        this, //handy to expose a project in its own lookup
                        state, //allow outside code to mark the project as needing saving
                        new ActionProviderImpl(), //Provides standard actions like Build and Clean
                        loadProperties(), //The project properties
                        new Info(), //Project information implementation
                        logicalView, //Logical view of project implementation
                        new MainFileProviderImpl(this),
                        new RendererServiceImpl(this),
                        new ViewServiceImpl(this),
                    });
        }
        return lkp;
    }
    
    private Properties loadProperties() {

        FileObject fob = projectDir.getFileObject(PovProjectFactory.PROJECT_DIR
                + "/" + PovProjectFactory.PROJECT_PROPFILE);

        Properties properties = new NotifyProperties(state);
        if (fob != null) {
            try {
                properties.load(fob.getInputStream());
            } catch (Exception e) {
                Exceptions.printStackTrace(e);
            }
        }

        return properties;

    }

    private static class NotifyProperties extends Properties {

        private final ProjectState state;

        NotifyProperties(ProjectState state) {
            this.state = state;
        }

        @Override
        public Object put(Object key, Object val) {

            Object result = super.put(key, val);

            if (((result == null) != (val == null)) || (result != null
                    && val != null && !val.equals(result))) {
                state.markModified();
            }

            return result;

        }

    }
    
    private final class ActionProviderImpl implements ActionProvider {

        @Override
        public String[] getSupportedActions() {
            return new String[]{
                ActionProvider.COMMAND_BUILD,
                ActionProvider.COMMAND_CLEAN,
                ActionProvider.COMMAND_COMPILE_SINGLE
            };
        }

        @Override
        public void invokeAction(String string, Lookup lookup) throws IllegalArgumentException {
            int idx = Arrays.asList(getSupportedActions()).indexOf(string);
            switch (idx) {
                case 0: //build
                    final RendererService ren = (RendererService) getLookup().lookup(RendererService.class);
                    RequestProcessor.getDefault().post(new Runnable() {
                        @Override
                        public void run() {
                            FileObject image = ren.render();
                            //If we succeeded, try to open the image
                            if (image != null) {
                                try {
                                    DataObject dob = DataObject.find(image);
                                    FileObject imageFile = dob.getPrimaryFile();

                                    // Cria um FileInputStream para ler os bytes do arquivo de imagem
                                    FileInputStream fis = new FileInputStream(new File(imageFile.getPath()));
                                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                                    int nRead;
                                    byte[] data = new byte[1024];
                                    while ((nRead = fis.read(data, 0, data.length)) != -1) {
                                        buffer.write(data, 0, nRead);
                                    }
                                    buffer.flush();
                                    byte[] imageData = buffer.toByteArray();
                                    fis.close();

                                    // Cria um ImageIcon diretamente a partir dos bytes da imagem
                                    ImageIcon icon = new ImageIcon(imageData);
                                    JLabel label = new JLabel(icon);
                                    JScrollPane scrollPane = new JScrollPane(label);

                                    // Cria um novo TopComponent para exibir a imagem
                                    TopComponent imageTopComponent = new TopComponent();
                                    imageTopComponent.setLayout(new BorderLayout());
                                    imageTopComponent.add(scrollPane, BorderLayout.CENTER);
                                    imageTopComponent.setName("Image Viewer");
                                    imageTopComponent.open();
                                    imageTopComponent.requestActive();
                                } catch (DataObjectNotFoundException | FileStateInvalidException e) {
                                    //Should never happen
                                    Exceptions.printStackTrace(e);
                                } catch (FileNotFoundException ex) {
                                    Exceptions.printStackTrace(ex);
                                } catch (IOException ex) {
                                    Exceptions.printStackTrace(ex);
                                }
                            }
                        }
                    });
                    break;
                case 1: // clean
                    FileObject fob = getImagesFolder(false);
                    if (fob != null) {
                        DataFolder fld = DataFolder.findFolder(fob);
                        for (Enumeration en = fld.children(); en.hasMoreElements();) {
                            DataObject ob = (DataObject) en.nextElement();
                            try {
                                ob.delete();
                            } catch (IOException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        }
                    }
                    break;
                case 2: //compile-sigle
                    final DataObject ob = (DataObject) lookup.lookup(DataObject.class);
                    if (ob != null) {
                        final RendererService ren1 = (RendererService) getLookup().lookup(RendererService.class);
                        RequestProcessor.getDefault().post(new Runnable() {
                            @Override
                            public void run() {
                                if (ob.isValid()) {
                                    ren1.render(ob.getPrimaryFile());
                                }
                            }
                        });
                    }
                    break;
                default:
                    throw new IllegalArgumentException(string);
            }
        }

        @Override
        public boolean isActionEnabled(String string, Lookup lookup) throws IllegalArgumentException {
            int idx = Arrays.asList(getSupportedActions()).indexOf(string);
            boolean result;
            switch (idx) {
                case 0: //build
                    result = true;
                    break;
                case 1: //clean
                    result = getImagesFolder(false) != null && getImagesFolder(false).getChildren().length > 0;
                    break;
                case 2: //compile-single
                    DataObject ob = (DataObject) lookup.lookup(DataObject.class);
                    if (ob != null) {
                        FileObject file = ob.getPrimaryFile();
                        result = "text/x-povray".equals(file.getMIMEType());
                    } else {
                        result = false;
                    }
                    break;
                default:
                    result = false;
            }
            return result;
        }

    }

    /**
     * Implementation of project system's ProjectInformation class
     */
    private final class Info implements ProjectInformation {

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(
                    "org/netbeans/examples/modules/povproject/resources/scenes.gif"));
        }

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public Project getProject() {
            return PovrayProject.this;
        }

    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.netbeans.examples.modules.povproject;

import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeListener;
import org.netbeans.examples.api.povray.MainFileProvider;
import org.netbeans.examples.api.povray.RendererService;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.explorer.ExplorerManager;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.WeakListeners;

/**
 *
 * @author mathe
 */
public final class RendererServiceImpl extends RendererService implements FileChangeListener {
    
    private PovrayProject proj;
    private Set scenesListenedTo = new HashSet();
    private boolean listeningToImagesFolder = false;
    
    public RendererServiceImpl(PovrayProject proj) {
        this.proj = proj;
    }
    
    PovrayProject getProject() {
        return proj;
    }
    
    private FileObject getRendererSettingsFolder() {
        String folderName = "Povray/RendererSettings";
        FileObject result = FileUtil.getConfigFile(folderName);
        if (result == null && !logged) {
            //Corrupted userdir or something is very very wrong.
            //Log it and move on.
            Exceptions.printStackTrace(new IllegalStateException("Renderer settings dir missing!"));
            logged = true;
        }
        return result;
    }

    private static boolean logged = false;

    private FileObject fileFor (String settingsName) {
        FileObject settingsFolder = getRendererSettingsFolder();
        FileObject result;
        if (settingsFolder != null) { //should never be null
            result = settingsFolder.getFileObject(settingsName);
        } else {
            result = null;
        }
        return result;
    }

    private void setPreferredRendererSettingsName(String val) {
        getPreferences().put(KEY_PREFERRED_SETTINGS, val);
    }

    private static final String KEY_PREFERRED_SETTINGS = "preferredSettings";

    static Preferences getPreferences() {
        return Preferences.userNodeForPackage(RendererServiceImpl.class);
    }

    @Override
    public FileObject render(FileObject scene, String propertiesName) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public FileObject render(FileObject scene, Properties renderSettings) {
        Povray pov = new Povray (this, scene, renderSettings);
        FileObject result;
        try {
            result = pov.render();
            if (!listeningToImagesFolder) {
                startListeningToImagesFolder();
            }
        } catch (IOException ioe) {
            Exceptions.printStackTrace(ioe);
            result = null;
        }
        return result;
    }

    @Override
    public FileObject render(FileObject scene) {
        Povray pov = new Povray(this, scene, null);
        FileObject result;
        try {
            result = pov.render();
            if (!listeningToImagesFolder) {
                startListeningToImagesFolder();
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            result = null;
        }
        return result;
    }

    @Override
    public FileObject render() {
        MainFileProvider mfp = (MainFileProvider) proj.getLookup().lookup(MainFileProvider.class);
        assert mfp != null;
        if (mfp.getMainFile() == null) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Select Main File!");
            //showChooseMainFileDlg(mfp);
        }
        if (mfp.getMainFile() != null) {
            return render(mfp.getMainFile());
        } else {
            return null;
        }
    }
    
    @NbBundle.Messages("TTL_ChooseMainFile=Choose Main File")
    private void showChooseMainFileDlg(final MainFileProvider mfp) {

        final MainFileChooser chooser = new MainFileChooser(proj);

        String title = Bundle.TTL_ChooseMainFile();

        //Create a simple dialog descriptor describing what kind of dialog
        //we want and its title and contents
        final DialogDescriptor desc = new DialogDescriptor(chooser, title);

        //The OK button should be disabled initially
        desc.setValid(false);

        //Create a property change listener.  It will listen on the selection
        //in our MainFileChooser, and enable the OK button if an appropriate
        //node is selected:
        PropertyChangeListener pcl = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                String propName = pce.getPropertyName();
                if (ExplorerManager.PROP_SELECTED_NODES.equals(propName)) {
                    Node[] n = (Node[]) pce.getNewValue();
                    boolean valid = n.length == 1;
                    if (valid) {
                        DataObject ob = (DataObject) n[0].getLookup().lookup(DataObject.class);
                        valid = ob != null;
                        if (valid) {
                            FileObject selectedFile = ob.getPrimaryFile();
                            String mimeType = selectedFile.getMIMEType();
                            valid = "text/x-povray".equals(mimeType);
                        }
                    }
                    desc.setValid(valid);
                }
            }
        };
        chooser.getExplorerManager().addPropertyChangeListener(pcl);

        //Show the dialog—dialogResult will be OK or Cancel
        Object dialogResult = DialogDisplayer.getDefault().notify(desc);

        //If the user clicked OK, try to set the main file
        //from the selection
        if (DialogDescriptor.OK_OPTION.equals(dialogResult)) {

            //Get the selected Node
            Node[] n = chooser.getExplorerManager().getSelectedNodes();

            //If it's > 1, explorer is broken—we set
            //single selection mode
            assert n.length <= 1;
            DataObject ob = (DataObject) n[0].getLookup().lookup(
                    DataObject.class);

            //Get the file from the data object
            FileObject selectedFile = ob.getPrimaryFile();

            //And save it as the main file
            mfp.setMainFile(selectedFile);

        }

    }

    @Override
    public String[] getAvailableRendererSettingsNames() {
        FileObject settingsFolder = getRendererSettingsFolder();
        String[] result;
        if (settingsFolder != null) {
            //Use a DataFolder here, so our ordering attributes in the layer
            //file are applied, and our returned String array will be in the
            //order we want
            DataFolder fld = DataFolder.findFolder(settingsFolder);
            DataObject[] kids = fld.getChildren();
            result = new String[ kids.length ];
            for (int i = 0; i < kids.length; i++) {
                result[i] = kids[i].getPrimaryFile().getNameExt();
            }
        } else {
            result = new String[0];
        }
        return result;
    }

    @Override
    public Properties getRendererSettings(String name) {
        Properties result = new Properties();
        FileObject settingsFile = fileFor (name);
        if (settingsFile != null) {
            try {
                result.load(new BufferedInputStream(settingsFile.getInputStream()));
            } catch (FileNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            Exceptions.printStackTrace(
		    new NullPointerException("Requested non-existent settings " +
                    "file " + name));
        }
        return result;
    }

    @Override
    public String getPreferredRendererSettingsNames() {
        String result = getPreferences().get(KEY_PREFERRED_SETTINGS, null);
        if (result == null) {
            result = "640x480.properties";
        }
        return result;
    }

    @Override
    public String getDisplayName(String settingsName) {
        FileObject file = fileFor (settingsName);
        String result;
        if (file != null) {
            DataObject dob;
            try {
                dob = DataObject.find(file);
                result = dob.getNodeDelegate().getDisplayName();
            } catch (DataObjectNotFoundException ex) {
                Exceptions.printStackTrace(ex);
                result = "[error]";
            }
        } else {
            result = "";
        }
        return result;
    }
    
    @Override
    protected void listenerAdded(FileObject scene, ChangeListener l) {
        synchronized (this) {
            if (scenesListenedTo.add(scene.getPath())) {
                if (scenesListenedTo.size() == 1 || !listeningToImagesFolder) {
                    startListeningToImagesFolder();
                }
                listenTo(scene);
            }
        }
    }
    
    @Override
    protected void noLongerListeningTo(FileObject scene) {
        synchronized (this) {
            scenesListenedTo.remove(scene.getPath());
        }
    }
    
    private void startListeningToImagesFolder() {
        FileObject imageFolder = proj.getImagesFolder(false);
        listeningToImagesFolder = imageFolder != null;
        if (listeningToImagesFolder) {
            listenTo(imageFolder);
        }
    }
    
    private void listenTo(FileObject file) {
        
        FileChangeListener stub = (FileChangeListener) WeakListeners.create(FileChangeListener.class, this, file);
        
        file.addFileChangeListener(stub);
    }

    @Override
    public void fileFolderCreated(FileEvent fe) {
        //Do nothing
    }

    @Override
    public void fileDataCreated(FileEvent fe) {
        FileObject created = fe.getFile();
        fireSceneChange(created);
    }

    @Override
    public void fileChanged(FileEvent fe) {
        FileObject changed = fe.getFile();
        fireSceneChange(changed);
    }

    @Override
    public void fileDeleted(FileEvent fe) {
        FileObject deleted = fe.getFile();
        fireSceneChange(deleted);
        if (deleted.isFolder() && "images".equals(deleted.getNameExt())) {
            fireSceneChange(null);
            listeningToImagesFolder = false;
        }
    }

    @Override
    public void fileRenamed(FileRenameEvent fre) {
        //do nothing
    }

    @Override
    public void fileAttributeChanged(FileAttributeEvent fae) {
        //do nothing
    }
    
}

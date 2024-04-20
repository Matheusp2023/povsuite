/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.netbeans.examples.modules.povfile;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import org.netbeans.examples.api.povray.RendererService;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.actions.Presenter;
import org.openide.windows.TopComponent;

/**
 *
 * @author mathe
 */
public class RendererAction extends AbstractAction implements Presenter.Popup{

    private final RendererService renderer;
    private final PovrayDataNode node;

    public RendererAction(RendererService renderer, PovrayDataNode node) {
        this.renderer = renderer;
        this.node = node;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        assert false;
    }

    @NbBundle.Messages("LBL_Render=Render")
    @Override
    public JMenuItem getPopupPresenter() {

        JMenu result = new JMenu();

        //Set the menu's label
        result.setText(Bundle.LBL_Render());

        //Get the names of all available settings sets:
        String[] availableSettings =
                renderer.getAvailableRendererSettingsNames();

        //Get the name of the most recently used setting set:
        String preferred = renderer.getPreferredRendererSettingsNames();

        for (int i = 0; i < availableSettings.length; i++) {

            String currName = availableSettings[i];

            RenderWithSettingsAction action =
                    new RenderWithSettingsAction(currName);

            JCheckBoxMenuItem itemForSettings = new JCheckBoxMenuItem(action);

            //Show our menu item checked if it is the most recently used set
            //of settings:
            itemForSettings.setSelected(preferred != null
                    && preferred.equals(currName));

            result.add(itemForSettings);

        }

        return result;

    }
    
    private class RenderWithSettingsAction extends AbstractAction implements Runnable {

        private final String name;

        public RenderWithSettingsAction(String name) {
            this.name = name;
            putValue(NAME, renderer.getDisplayName(name));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            RequestProcessor.getDefault().post(this);
        }

        @Override
        public void run() {
            DataObject ob = node.getDataObject();
            FileObject toRender = ob.getPrimaryFile();
            Properties mySettings = renderer.getRendererSettings(name);
            FileObject image = renderer.render(toRender, mySettings);
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

    }
    
}

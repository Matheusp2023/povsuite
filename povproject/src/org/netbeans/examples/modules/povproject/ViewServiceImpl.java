/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.netbeans.examples.modules.povproject;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import org.netbeans.examples.api.povray.ViewService;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;

/**
 *
 * @author mathe
 */
public class ViewServiceImpl implements ViewService {
    
    private final PovrayProject proj;
    
    public ViewServiceImpl(PovrayProject proj) {
        this.proj = proj;
    }
    
    private FileObject getImageFor (FileObject scene) {
        FileObject imagesDir = proj.getImagesFolder(false);
        FileObject result;
        if (imagesDir != null) {
            File sceneFile = FileUtil.toFile(scene);
            if (sceneFile != null) {
                String imageName = Povray.stripExtension(sceneFile) + ".png";
                result = imagesDir.getFileObject(imageName);
            } else {
                result = null;
            }
        } else {
            result = null;
        }
        return result;
    }

    @Override
    public boolean isRendered(FileObject file) {
        return getImageFor(file) != null;
    }

    @Override
    public boolean isUpToDate(FileObject file) {
        FileObject image = getImageFor(file);
        boolean result;
        if (image != null) {
            result = file.lastModified().before(image.lastModified());
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public void view(FileObject file) {
        FileObject image = getImageFor(file);
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
            } catch (DataObjectNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            } catch (FileNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        Toolkit.getDefaultToolkit().beep();
    }
    
}

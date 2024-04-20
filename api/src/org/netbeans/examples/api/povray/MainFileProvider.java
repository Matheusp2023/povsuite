/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.netbeans.examples.api.povray;

import org.openide.filesystems.FileObject;

/**
 *
 * @author mathe
 */
public abstract class MainFileProvider {
    
    public abstract FileObject getMainFile();

    public abstract void setMainFile (FileObject file);

    public boolean isMainFile (FileObject file) {
        return file.equals(getMainFile());
    }
    
}

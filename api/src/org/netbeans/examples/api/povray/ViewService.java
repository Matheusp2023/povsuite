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
public interface ViewService {
    
    boolean isRendered(FileObject file);

    boolean isUpToDate(FileObject file);

    void view(FileObject file);
}

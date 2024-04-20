/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.netbeans.examples.api.povray;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.filesystems.FileObject;

/**
 *
 * @author mathe
 */
public abstract class RendererService {
    
    public static final String PROJECT_RENDERER_KEY_PREFIX = "renderer.";
    public static final String PRODUCTION_RENDERER_SETTINGS_NAME = "production";
    
    private Map scenes2listeners = new HashMap();

    public abstract FileObject render(FileObject scene, String propertiesName);

    public abstract FileObject render(FileObject scene, Properties renderSettings);

    public abstract FileObject render(FileObject scene);

    public abstract FileObject render();

    public abstract String[] getAvailableRendererSettingsNames();

    public abstract Properties getRendererSettings(String name);

    public abstract String getPreferredRendererSettingsNames();

    public abstract String getDisplayName(String settingsName);
    
    public final void addChangeListener(FileObject scene, ChangeListener l) {
        
        String scenePath = scene.getName();
        
        synchronized (scenes2listeners) {
            
            Reference listenerRef = new WeakReference(l);
            List listeners = (List) scenes2listeners.get(scenePath);
            if (listeners == null) {
                listeners = new LinkedList();
                scenes2listeners.put(scenePath, listeners);
            }
            
            listeners.add(listenerRef);
        }
        
        listenerAdded(scene, l);
    }
    
    protected void listenerAdded(FileObject scene, ChangeListener l) {
        //do nothing, should be overridden.  Here we should start listening
        //for changes in the image file (particularly deletion)
    }

    protected void noLongerListeningTo(FileObject scene) {
        //detach any listeners for image files being created/destroyed here
    }
    
    /**
    * Fire a change event to any listeners that care about changes for the
    * passed scene file. If the scene file is null, fire changes to all
    * listeners for all files.
    *
    * @param scene a POV-Ray scene or include file
    */
    protected final void fireSceneChange(FileObject scene) {
        
        String scenePath = scene == null ? null : scene.getName();
        List fireTo = null;
        
        Boolean stillListening = null;
        
        synchronized (scenes2listeners) {
            
            List listeners;
            if (scenePath != null) {
                listeners = (List) scenes2listeners.get(scenePath);
            } else {
                listeners = new ArrayList();
                for (Iterator i = scenes2listeners.keySet().iterator(); i.hasNext();) {
                    String path = (String) i.next();
                    List curr = (List) scenes2listeners.get(path);
                    if (curr != null) {
                        listeners.addAll(curr);
                    }
                }
            }
            if (listeners != null && !listeners.isEmpty()) {
                fireTo = new ArrayList(3);
                for (Iterator i = listeners.iterator(); i.hasNext();) {
                    Reference ref = (Reference) i.next();
                    ChangeListener l = (ChangeListener) ref.get();
                    if (l != null) {
                        fireTo.add(l);
                    } else {
                        i.remove();
                    }
                }
                if (listeners.isEmpty()) {
                    scenes2listeners.remove(scenePath);
                    stillListening = Boolean.FALSE;
                } else {
                    stillListening = Boolean.TRUE;
                }
            }
        }
        
        if (stillListening != null && Boolean.FALSE.equals(stillListening)) {
            noLongerListeningTo(scene);
        }
        
        if (fireTo != null) {
            for (Iterator i = fireTo.iterator(); i.hasNext();) {
                ChangeListener l = (ChangeListener) i.next();
                l.stateChanged(new ChangeEvent(this));
            }
        }
    }
}

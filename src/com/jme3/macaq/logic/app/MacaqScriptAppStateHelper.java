/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.logic.app;

import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.Cinematic;
import com.jme3.macaq.base.WorldPresence;
import com.jme3.macaq.control.MoveToTargetControl;
import com.jme3.macaq.logic.components.MoveToTarget;
import com.jme3.macaq.logic.components.Notify;
import com.jme3.macaq.logic.components.PlayCinematic;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme3.macaq.control.Notifyable;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class MacaqScriptAppStateHelper {
    
    private final List<Object> loadedObjects = new ArrayList<>();
    
    protected void setupMoveToTarget(Node rootNode, WorldPresence component){
        Spatial spatial = rootNode.getChild(((WorldPresence)component).getSpatialName());
        if(spatial != null){
            try {
                Control control = spatial.getControl(MoveToTargetControl.class);
                if(control == null){
                    control = ((WorldPresence)component).getControlType().getConstructor(component.getClass()).newInstance(component);
                    spatial.addControl(control);
                    loadedObjects.add(control);
                }
                ((MoveToTargetControl)control).initialize((MoveToTarget) component);

            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(MacaqScriptAppState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    protected void setupWorldPresence(Node rootNode, WorldPresence component){
        try {
            Spatial spatial = rootNode.getChild(((WorldPresence)component).getSpatialName());
            if(spatial != null){
                // add a control to a Geometry
                if(spatial instanceof Geometry){
                    Control control = ((WorldPresence)component).getControlType().getConstructor(component.getClass()).newInstance(component);
                    spatial.addControl(control);
                    loadedObjects.add(control);

                // special feature: add a control to all of a Node's direct children
                } else if (spatial instanceof Node){
                    for(Spatial s : ((Node)spatial).getChildren()){
                        Control control = ((WorldPresence)component).getControlType().getConstructor(component.getClass()).newInstance(component);
                        s.addControl(control);
                        loadedObjects.add(control);
                    }
                }
            } else {
                Logger.getLogger(MacaqScriptAppState.class.getName()).log(Level.SEVERE, null, String.format("Missing spatial for %s", component));
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(MacaqScriptAppState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void setupNotify(Node rootNode, Notify component){
        String controlName = component.getControlClassName();
        try {
            Spatial s = rootNode.getChild(component.getSpatialName());
            Class<? extends Notifyable> controlClass = (Class<? extends Notifyable>) Class.forName(controlName);
            Notifyable control = s.getControl(controlClass);
            if(control != null){
                component.onMessage().attachChild(control.message());
                control.onPerformed().attachChild(component.performed());
            } else {
                Logger.getLogger(MacaqScriptAppState.class.getName()).log(Level.SEVERE, String.format("Missing control for notify: %s", s.getName()));
            }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MacaqScriptAppState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected CinematicsController setupPlayCinematics(PlayCinematic component, AssetManager assetManager, AppStateManager stateManager){
        Cinematic cinematic = (Cinematic) assetManager.loadAsset(((PlayCinematic)component).getCinematicName());
        CinematicsController controller = new CinematicsController(stateManager, (PlayCinematic) component, cinematic);
        loadedObjects.add(controller);
        return controller;
    }
}

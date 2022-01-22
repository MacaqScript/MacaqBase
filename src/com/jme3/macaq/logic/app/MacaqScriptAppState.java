/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.logic.app;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.CinematicLoader;
import com.jme3.asset.MacaqLoader;
import com.jme3.macaq.base.Updatable;
import com.jme3.scene.Node;
import com.jme3.scene.control.Control;
import com.jme3.util.IntMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.logic.script.MacaqScript;
import com.jme3.macaq.logic.components.PlayCinematic;
import com.jme3.macaq.base.WorldPresence;
import com.jme3.macaq.control.MacaqControl;
import com.jme3.macaq.logic.components.MoveToTarget;
import com.jme3.macaq.logic.components.Notify;
import com.jme3.macaq.logic.components.Start;
import com.jme3.macaq.logic.script.ScriptConstants;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class MacaqScriptAppState extends AbstractAppState{
    
    private MacaqScript macaqScript;
    
    /*
    Used for application specific purposes
    */
    private String scriptName;
    private Node rootNode;
    private Application app;
    
    private boolean scriptLoaded = false;
    
    private List<Object> loadedObjects = new ArrayList<>();
    private List<Updatable> updatableComponents = new ArrayList<>();
    private MacaqScriptAppStateHelper helper = new MacaqScriptAppStateHelper();
    
    public MacaqScriptAppState(String scriptName){
        this.scriptName = scriptName;
    }
    
    public MacaqScriptAppState(MacaqScript macaqScript){
        this.macaqScript = macaqScript;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager, app); //To change body of generated methods, choose Tools | Templates.
        rootNode = ((SimpleApplication)app).getRootNode();
        this.app = app;
        initialized = true;
        if(scriptName != null){
            app.getAssetManager().registerLoader(MacaqLoader.class, ScriptConstants.EXT);
            macaqScript = (MacaqScript) app.getAssetManager().loadAsset(scriptName);
        }
        if(macaqScript == null){
            throw new NullPointerException("MacaqScript is null. It may have failed to load");
        }
        initializeScript();
        
        startScript();
    }
    
    public MacaqScript getScript() {
        return macaqScript;
    }

    public void setScript(MacaqScript script) {
        this.macaqScript = script;
    }

    public String getScriptName() {
        return scriptName;
    }

    private void initializeScript(){
        Iterator it = macaqScript.getScriptMap().iterator();
        while(it.hasNext()){
            
            IntMap.Entry entry = (IntMap.Entry) it.next();
            
            AbstractLogicalComponent component = (AbstractLogicalComponent) entry.getValue();
            Logger.getLogger(MacaqScriptAppState.class.getName()).log(Level.INFO, String.format("Adding component %s", component));
            // special treatment
            if(component instanceof MoveToTarget){
                helper.setupMoveToTarget(rootNode, (WorldPresence) component);
            } else if(component instanceof WorldPresence){
                helper.setupWorldPresence(rootNode, (WorldPresence) component);
            } else if (component instanceof PlayCinematic){
                app.getAssetManager().registerLoader(CinematicLoader.class, "j3c");
                CinematicsController controller = helper.setupPlayCinematics((PlayCinematic) component, app.getAssetManager(), app.getStateManager());
            } else if (component instanceof Notify){
                helper.setupNotify(rootNode, (Notify) component);
            }
            
            if(component instanceof Updatable){
                updatableComponents.add((Updatable) component);
            }
        }
        scriptLoaded = true;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        
        for(Updatable updatable: updatableComponents){
            updatable.updateComponent(tpf);
        }
    }
    
    
    
    public void unload(){
        for(Object o: loadedObjects){
            if(o instanceof MacaqControl){
                ((MacaqControl)o).getSpatial().removeControl((Control) o);
            } else if(o instanceof CinematicsController){
                ((CinematicsController) o).stop();
            }
        }
        
        updatableComponents.clear();
    }

    @Override
    public void stateAttached(AppStateManager stateManager) {
        super.stateAttached(stateManager);
        if(scriptLoaded){
            startScript();
        }
    }

    private void startScript(){
        ((Start)macaqScript.getScriptMap().get(0)).onStart().execute(null);
        System.out.println("Starting script");
    }
    
}

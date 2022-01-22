/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jme3.macaq.logic;

import com.jme3.bounding.BoundingVolume;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.jme3.macaq.base.LogicOutConnection;
import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.base.Updatable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Rickard
 */
public class Trigger extends AbstractLogicalComponent implements Updatable{

    private int delay = 0;

    private boolean triggered = false;
    private BoundingVolume triggerVolume;
    private Node triggerActors;
    private Spatial triggeringObject;
    
    private List<String> actorNames = new ArrayList<>();
    private String boundingVolumeName;

    private String name = "Trigger";
    
    public Trigger(){
        super();
    }

    @Override
    public void updateComponent(float tpf) {
        
        if(!enabled){
            return;
        }
        if(triggerVolume != null && triggerActors != null){
            checkTriggerVolume();
        }
        if(triggered){
            if(delay < 1){
                if(triggeringObject != null){
                    onTrigger().execute(  Map.ofEntries(Map.entry("", triggeringObject)));
                }
                else{
                    onTrigger().execute( new HashMap<>());
                }
                triggered = false;
            } else delay -= tpf*1000;
        }
    }
    
    protected void checkTriggerVolume(){
        Spatial n;
        for(int i = 0; i < triggerActors.getChildren().size(); i++ ){

            n =  triggerActors.getChild(i);
            if(triggerVolume.contains(n.getWorldTranslation())){
                triggered = true;
                triggeringObject = n;
            }
        }
    }

    /**
     * Trigger this trigger
     * @return
     */
    public LogicInConnection trigger(){
        LogicInConnection trigger = inConnections.get("trigger");
        if(trigger == null){
            trigger = new LogicInConnection((Map<String,Object> args) -> {
                if(!enabled) return;
                triggered = true;
            }, "trigger");
            inConnections.put("trigger", trigger);
        }
        return trigger;
    }

    /**
     * Connections that the trigger will trigger
     * @return
     */
    public LogicOutConnection onTrigger(){
        LogicOutConnection onTrigger = outConnections.get("onTrigger");
        if(onTrigger == null){
            onTrigger = new LogicOutConnection("onTrigger");
            outConnections.put("onTrigger", onTrigger);
        }
        return onTrigger;
    }

    /**
     * Set a delay that must pass before a triggered trigger executes onTrigger()
     * @param delay
     */
    public void setDelay(int delay){
        this.delay = delay;
    }

    /**
     * Set the node which this triggers BoundingVolume should react on
     * @param triggers
     */
    public void setTriggerNode(Node triggers){
        this.triggerActors = triggers;
    }

    /**
     * The physical presence (if any) of this trigger
     * @param volume
     */
    public void setVolume(BoundingVolume volume){
        this.triggerVolume = volume;
    }

    public BoundingVolume getVolume(){
        return triggerVolume;
    }
    
    public String getName() {
        return name;
    }

    public List<String> getActorNames() {
        return actorNames;
    }

    public void setActorNames(List<String> actorNames) {
        this.actorNames = actorNames;
    }

    public String getBoundingVolumeName() {
        return boundingVolumeName;
    }

    public void setBoundingVolumeName(String boundingVolumeName) {
        this.boundingVolumeName = boundingVolumeName;
    }
    
    public void addActorName(String actor){
        actorNames.add(actor);
    }
    
    public void removeActorName(String actor){
        actorNames.remove(actor);
    }

    @Override
    public void forceAddConnections() {
        super.forceAddConnections();
        trigger();
        onTrigger();
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.writeSavableArrayList((ArrayList) actorNames, "actorNames", null);
        capsule.write(triggerVolume, "triggerVolume", null);
        capsule.write(boundingVolumeName, "boundingVolumeName", null);
    }
    
    @Override
    public void read(JmeImporter im) throws IOException{
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        actorNames = ic.readSavableArrayList("actorNames", null);
        triggerVolume = (BoundingVolume) ic.readSavable("triggerVolume", null);
        boundingVolumeName = ic.readString("boundingVolumeName", null);
    }
}

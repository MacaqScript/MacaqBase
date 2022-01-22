/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jme3.macaq.logic.components;

import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.base.LogicOutConnection;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.macaq.base.ExecutableLogic;
import com.jme3.scene.control.Control;
import java.io.IOException;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.base.WorldPresence;
import com.jme3.macaq.control.TriggerControl;
import java.util.Map;

/**
 *
 * @author Rickard
 */
public class Trigger extends AbstractLogicalComponent implements WorldPresence{

    private boolean triggered = false;
    
    private String[] actorNames = new String[0];
    private String triggerSpatialName;
    private float resetTime;
    private boolean singleUse;
    
    public Trigger(){
        super();
    }

    /**
     * Trigger this trigger. Usually handled by TriggerControl
     * @return
     */
    public LogicInConnection<ExecutableLogic> trigger(){
        LogicInConnection<ExecutableLogic> trigger = inConnections.get("trigger");
        if(trigger == null){
            trigger = new LogicInConnection<ExecutableLogic>((Map<String,Object> args) -> {
                if(!enabled) return;
                triggered = true;
                onTrigger().execute(populateArgs(args));
                if(singleUse){
                    enabled = false;
                }
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

    public String[]  getActorNames() {
        return actorNames;
    }

    public void setActorNames(String...  actorNames) {
        this.actorNames = actorNames;
    }

    public float getResetTime() {
        return resetTime;
    }

    public void setResetTime(float resetTime) {
        this.resetTime = resetTime;
    }

    public boolean isSingleUse() {
        return singleUse;
    }

    public void setSingleUse(boolean singleUse) {
        this.singleUse = singleUse;
    }

    public String getTriggerSpatialName() {
        return triggerSpatialName;
    }

    public void setTriggerSpatialName(String triggerSpatialName) {
        this.triggerSpatialName = triggerSpatialName;
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
        capsule.write(actorNames, "actorNames", null);
        capsule.write(triggerSpatialName, "triggerSpatialName", null);
        capsule.write(triggered, "triggered", false);
        capsule.write(resetTime, "resetTime", -1f);
        capsule.write(singleUse, "singleUse", true);
    }
    
    @Override
    public void read(JmeImporter im) throws IOException{
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        actorNames = ic.readStringArray("actorNames", new String[0]);
        triggerSpatialName = ic.readString("triggerSpatialName", null);
        triggered = ic.readBoolean("triggered", false);
        resetTime = ic.readFloat("resetTime", -1f);
        singleUse = ic.readBoolean("singleUse", true);
    }

    @Override
    public Class<? extends Control> getControlType() {
        return TriggerControl.class;
    }

    @Override
    public String getSpatialName() {
        return triggerSpatialName;
    }

    @Override
    public void setControlType(Class c) {
    }

    @Override
    public void setSpatialName(String spatialName) {
        this.triggerSpatialName = spatialName;
    }
}

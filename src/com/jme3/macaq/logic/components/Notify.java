/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.logic.components;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.macaq.base.LogicConnection;
import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.base.LogicOutConnection;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Can notify a spatial's control on message.
 * 
 * @author Rickard <neph1 @ github>
 */
public class Notify extends AbstractLogicalComponent{
    
    private String spatialName;
    private String controlClassName;
    private String message;
    
    public LogicInConnection message(){
        LogicInConnection trigger = inConnections.get("message");
        if(trigger == null){
            trigger = new LogicInConnection((Map<String,Object> args) -> {
                if(enabled){
                    Logger.getLogger(Notify.class.getSimpleName()).log(Level.INFO, "message");
                    onMessage().execute(populateArgs(args));
                }
            }, "message");
            inConnections.put("message", trigger);
        }
        return trigger;
    }
    
    public LogicOutConnection onMessage(){
        LogicOutConnection onTrigger = outConnections.get("onMessage");
        if(onTrigger == null){
            onTrigger = new LogicOutConnection("onMessage");
            outConnections.put("onMessage", onTrigger);
        }
        return onTrigger;
    }
    
    public LogicInConnection performed(){
        LogicInConnection performed = inConnections.get("performed");
        if(performed == null){
            performed = new LogicInConnection((Map<String,Object> args) -> {
                if(enabled && (int)args.get("id") == id){
                    Logger.getLogger(Notify.class.getSimpleName()).log(Level.INFO, "performed");
                    onPerformed().execute(populateArgs(args));
                }
            }, "performed");
            inConnections.put("performed", performed);
        }
        return performed;
    }
    
    public LogicOutConnection onPerformed(){
        LogicOutConnection onPerformed = outConnections.get("onPerformed");
        if(onPerformed == null){
            onPerformed = new LogicOutConnection("onPerformed");
            outConnections.put("onPerformed", onPerformed);
        }
        return onPerformed;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(spatialName, "spatialName", null);
        capsule.write(controlClassName, "controlClassName", null);
        capsule.write(message, "message", null);
    }
    
    @Override
    public void read(JmeImporter im) throws IOException{
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        spatialName = ic.readString("spatialName", null);
        controlClassName = ic.readString("controlClassName", null);
        message = ic.readString("message", null);
    }

    public String getSpatialName() {
        return spatialName;
    }

    public void setSpatialName(String spatialName) {
        this.spatialName = spatialName;
    }

    public String getControlClassName() {
        return controlClassName;
    }

    public void setControlClassName(String controlClassName) {
        this.controlClassName = controlClassName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    protected Map<String, Object> populateArgs(Map<String, Object> args) {
        super.populateArgs(args);
        args.put("message", message);
        return args;
    }

    @Override
    public void forceAddConnections() {
        super.forceAddConnections();
        message();
        onMessage();
        onPerformed();
    }

    
    
    
}

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
import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.base.LogicOutConnection;
import com.jme3.macaq.base.WorldPresence;
import com.jme3.macaq.control.MoveToTargetControl;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.scene.control.Control;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class MoveToTarget extends AbstractLogicalComponent implements WorldPresence{
    
    private String spatialName = "";
    private String targetSpatialName = "";
    private float speed = 1f;
    private boolean lookAt;
    
    public MoveToTarget(){
        
    }
    
    public LogicInConnection reset(){
        LogicInConnection reset = inConnections.get("reset");
        if(reset == null){
            reset = new LogicInConnection((Map<String,Object> args) -> {
                onReset().execute(populateArgs(args));
            }, "reset");
            inConnections.put("reset", reset);
        }
        return reset;
    }
    
    public LogicInConnection performed(){
        LogicInConnection perform = inConnections.get("performed");
        if(perform == null){
            perform = new LogicInConnection((Map<String,Object> args) -> {
                Logger.getLogger(getClass().getSimpleName()).log(Level.INFO, String.format("%s performed movement", args.get("id")));
                int id = (int) args.get("id");
                // this action has been performed
                if(this.id == id){
                    onPerformed().execute(populateArgs(args));
                }
                
            }, "performed");
            inConnections.put("performed", perform);
        }
        return perform;
    }
    
    public LogicInConnection move(){
        LogicInConnection move = inConnections.get("move");
        if(move == null){
            move = new LogicInConnection((Map<String,Object> args) -> {
                onMove().execute(populateArgs(args));
            }, "move");
            inConnections.put("move", move);
        }
        return move;
    }
    
    public LogicOutConnection onPerformed(){
        LogicOutConnection onPerformed = outConnections.get("onPerformed");
        if(onPerformed == null){
            onPerformed = new LogicOutConnection("onPerformed");
            outConnections.put("onPerformed", onPerformed);
        }
        return onPerformed;
    }
    
    public LogicOutConnection onReset(){
        LogicOutConnection onReset = outConnections.get("onReset");
        if(onReset == null){
            onReset = new LogicOutConnection("onReset");
            outConnections.put("onReset", onReset);
        }
        return onReset;
    }
    
    public LogicOutConnection onMove(){
        LogicOutConnection onMove = outConnections.get("onMove");
        if(onMove == null){
            onMove = new LogicOutConnection("onMove");
            outConnections.put("onMove", onMove);
        }
        return onMove;
    }

    @Override
    public Class<? extends Control> getControlType() {
        return MoveToTargetControl.class;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(spatialName, "spatialName", null);
        capsule.write(targetSpatialName, "targetSpatialName", null);
        capsule.write(speed, "speed", -1f);
        capsule.write(lookAt, "lookAt", true);
    }
    
    @Override
    public void read(JmeImporter im) throws IOException{
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        spatialName = ic.readString("spatialName", null);
        targetSpatialName = ic.readString("targetSpatialName", null);
        speed = ic.readFloat("speed", -1f);
        lookAt = ic.readBoolean("lookAt", true);
    }

    @Override
    public String getSpatialName() {
        return spatialName;
    }

    @Override
    public void setControlType(Class c) {
    }

    @Override
    public void setSpatialName(String spatialName) {
        this.spatialName = spatialName;
    }

    public String getTargetSpatialName() {
        return targetSpatialName;
    }

    public void setTargetSpatialName(String targetSpatialName) {
        this.targetSpatialName = targetSpatialName;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isLookAt() {
        return lookAt;
    }

    public void setLookAt(boolean lookAt) {
        this.lookAt = lookAt;
    }

    @Override
    public void forceAddConnections() {
        super.forceAddConnections();
        
        performed();
        move();
        onPerformed();
        onMove();
    }
    
    
}

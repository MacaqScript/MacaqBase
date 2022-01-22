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
import com.jme3.macaq.base.ExecutableLogic;
import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.base.LogicOutConnection;
import com.jme3.macaq.base.WorldPresence;
import com.jme3.macaq.control.AnimationControl;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.scene.control.Control;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class ControlAnimation extends AbstractLogicalComponent implements WorldPresence{

    private String spatialName;
    private String animation;
    private String loopMode = "Looping";
    private float animationSpeed = 1f;
    private float blendTime = 0.2f;
    
    
    public LogicInConnection<ExecutableLogic> start(){
        LogicInConnection<ExecutableLogic> start = inConnections.get("start");
        if(start == null){
            start = new LogicInConnection<ExecutableLogic>((Map<String,Object> args) -> {
                if(enabled){
                    if(args == null){
                        args = new HashMap<>();
                    }
                    args.put("id", id);
                    args.put("animName", animation);
                    onStart().execute(args);
                }
            }, "start");
            inConnections.put("start", start);
        }
        return start;
    }
    
    public LogicInConnection<ExecutableLogic> stop(){
        LogicInConnection<ExecutableLogic> stop = inConnections.get("stop");
        if(stop == null){
            stop = new LogicInConnection<ExecutableLogic>((Map<String,Object> args) -> {
                if(enabled){
                    if(args == null){
                        args = new HashMap<>();
                    }
                    args.put("id", id);
                    args.put("animName", animation);
                    onStop().execute(args);
                }
            }, "stop");
            inConnections.put("stop", stop);
        }
        return stop;
    }
    
    public LogicOutConnection onStart(){
        LogicOutConnection onStart = outConnections.get("onStart");
        if(onStart == null){
            onStart = new LogicOutConnection("onStart");
            outConnections.put("onStart", onStart);
        }
        return onStart;
    }
    
    public LogicOutConnection onStop(){
        LogicOutConnection onStop = outConnections.get("onStop");
        if(onStop == null){
            onStop = new LogicOutConnection("onStop");
            outConnections.put("onStop", onStop);
        }
        return onStop;
    }
    
    @Override
    public Class<? extends Control> getControlType() {
        return AnimationControl.class;
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
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(spatialName, "spatialName", null);
        capsule.write(animation, "animation", null);
    }
    
    @Override
    public void read(JmeImporter im) throws IOException{
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        spatialName = ic.readString("spatialName", null);
        animation = ic.readString("animation", null);
    }

    @Override
    public void forceAddConnections() {
        super.forceAddConnections();
        start();
        stop();
        onStart();
        onStop();
    }
    
    
}

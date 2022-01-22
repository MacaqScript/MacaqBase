/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jme3.macaq.logic.components;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.scene.control.Control;
import java.io.IOException;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.base.WorldPresence;
import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.base.LogicOutConnection;
import com.jme3.macaq.control.PlayEffectControl;
import java.util.Map;

/**
 *
 * @author Rickard
 */
public class PlayEffect extends AbstractLogicalComponent implements WorldPresence{

    private boolean emitAllParticles;

    private String emitterName;
    
    public PlayEffect(){
        super();
    }

    public void update(float tpf) {
    }

    /**
     * Start the current effect
     * @return
     */
    public LogicInConnection startEffect(){
        LogicInConnection startEffect = inConnections.get("startEffect");
        if(startEffect == null){
            startEffect = new LogicInConnection((Map<String,Object> args) -> {
                if(enabled){
                    onStartEffect().execute(populateArgs(args));
                }
            }, "startEffect");
            inConnections.put("startEffect", startEffect);
        }
        return startEffect;
    }

    /**
     * Stop playing the effect
     * @return
     */
    public LogicInConnection stopEffect(){
        LogicInConnection stopEffect = inConnections.get("stopEffect");
        if(stopEffect == null){
            stopEffect = new LogicInConnection((Map<String,Object> args) -> {
                if(enabled){
                    onStopEffect().execute(populateArgs(args));
                }
            }, "stopEffect");
            inConnections.put("stopEffect", stopEffect);
        }
        return stopEffect;
    }
    
    public LogicInConnection emitAll(){
        LogicInConnection emitAll = inConnections.get("emitAll");
        if(emitAll == null){
            emitAll = new LogicInConnection((Map<String,Object> args) -> {
                if(enabled){
                    emitAll().execute(populateArgs(args));
                }
            }, "emitAll");
            inConnections.put("emitAll", emitAll);
        }
        return emitAll;
    }
    
    public LogicOutConnection onStartEffect(){
        LogicOutConnection onStartEffect = outConnections.get("onStartEffect");
        if(onStartEffect == null){
            onStartEffect = new LogicOutConnection("onStartEffect");
            outConnections.put("onStartEffect", onStartEffect);
        }
        return onStartEffect;
    }
    
    public LogicOutConnection onStopEffect(){
        LogicOutConnection onStopEffect = outConnections.get("onStopEffect");
        if(onStopEffect == null){
            onStopEffect = new LogicOutConnection("onStopEffect");
            outConnections.put("onStopEffect", onStopEffect);
        }
        return onStopEffect;
    }
    
    public LogicOutConnection onEmitAll(){
        LogicOutConnection onEmitAll = outConnections.get("onEmitAll");
        if(onEmitAll == null){
            onEmitAll = new LogicOutConnection("onEmitAll");
            outConnections.put("onEmitAll", onEmitAll);
        }
        return onEmitAll;
    }
    /**
     * Set the particle emitter that this effect should play
     * @param effectName the name of the ParticleEmitter
     */
    public void setEmitterName(String effectName){
        this.emitterName = effectName;
    }
    
    public String getEmitterName(){
        return emitterName;
    }
    
    @Override
    public void forceAddConnections() {
        super.forceAddConnections();
        startEffect();
        stopEffect();
        emitAll();
        onStartEffect();
        onStopEffect();
        onEmitAll();
    }

    @Override
    public Class<? extends Control> getControlType() {
        return PlayEffectControl.class;
    }

    @Override
    public String getSpatialName() {
        return emitterName;
    }

    @Override
    public void setControlType(Class c) {
    }

    @Override
    public void setSpatialName(String spatialName) {
        emitterName = spatialName;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(emitterName, "emitterName", null);
    }
    
    @Override
    public void read(JmeImporter im) throws IOException{
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        emitterName = ic.readString("emitterName", null);
    }
}

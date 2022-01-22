/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.control;

import com.jme3.effect.ParticleEmitter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.logic.components.PlayEffect;
import java.util.Map;
/**
 *
 * @author Rickard <neph1 @ github>
 */
public class PlayEffectControl extends MacaqControl{

    private PlayEffect playEffect;
    private LogicInConnection start;
    private LogicInConnection stop;
    private LogicInConnection emitAll;
    
    public PlayEffectControl(PlayEffect playEffect){
        this.playEffect = playEffect;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    public LogicInConnection start(){
        if(start == null){
            
            start = new LogicInConnection((Map<String,Object> args) -> {
                if(spatial != null){
                    ((ParticleEmitter)spatial).setEnabled(true);
                    
                }
            });
        }
        return start;
    }
    
    public LogicInConnection stop(){
        if(stop == null){
            stop = new LogicInConnection((Map<String,Object> args) -> {
                if(spatial != null){
                    ((ParticleEmitter)spatial).setEnabled(false);
                }
            });
        }
        return stop;
    }
    
    public LogicInConnection emitAll(){
        if(emitAll == null){
            emitAll = new LogicInConnection((Map<String,Object> args) -> {
                if(spatial != null){
                    ((ParticleEmitter)spatial).emitAllParticles();
                }
            });
        }
        return emitAll;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        initialize();
    }
    
    
    
    public void initialize(){

        playEffect.onEmitAll().attachChild(emitAll());
        playEffect.onStartEffect().attachChild(start());
        playEffect.onStopEffect().attachChild(stop());
    }
}

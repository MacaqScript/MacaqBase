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
import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.base.LogicOutConnection;
import com.jme3.macaq.control.PlayEffectControl;
import java.util.Map;

/**
 *
 * @author Rickard
 */
public class PlayCinematic extends AbstractLogicalComponent{

    private String cinematicName;
    private String nodeName;
    
    public PlayCinematic(){
        super();
    }

    public void update(float tpf) {
    }

    /**
     * Start the cinematic
     * @return
     */
    public LogicInConnection play(){
        LogicInConnection play = inConnections.get("play");
        if(play == null){
            play = new LogicInConnection((Map<String,Object> args) -> {
                if(enabled){
                    onPlay().execute(populateArgs(args));
                }
            }, "play");
            inConnections.put("play", play);
        }
        return play;
    }

    /**
     * Stop the cinematic
     * @return
     */
    public LogicInConnection stop(){
        LogicInConnection stop = inConnections.get("stop");
        if(stop == null){
            stop = new LogicInConnection((Map<String,Object> args) -> {
                if(enabled){
                    onStop().execute(populateArgs(args));
                }
            }, "stop");
            inConnections.put("stop", stop);
        }
        return stop;
    }
    
    /**
     * Pause the cinematic
     * @return 
     */
    public LogicInConnection pause(){
        LogicInConnection pause = inConnections.get("pause");
        if(pause == null){
            pause = new LogicInConnection((Map<String,Object> args) -> {
                if(enabled){
                    onPause().execute(populateArgs(args));
                }
            }, "pause");
            inConnections.put("pause", pause);
        }
        return pause;
    }
    
    public LogicOutConnection onPlay(){
        LogicOutConnection onPlay = outConnections.get("onPlay");
        if(onPlay == null){
            onPlay = new LogicOutConnection("onPlay");
            outConnections.put("onPlay", onPlay);
        }
        return onPlay;
    }
    
    public LogicOutConnection onStop(){
        LogicOutConnection onStop = outConnections.get("onStop");
        if(onStop == null){
            onStop = new LogicOutConnection("onStop");
            outConnections.put("onStop", onStop);
        }
        return onStop;
    }
    
    public LogicOutConnection onPause(){
        LogicOutConnection onPause = outConnections.get("onPause");
        if(onPause == null){
            onPause = new LogicOutConnection("onPause");
            outConnections.put("onPause", onPause);
        }
        return onPause;
    }

    public String getCinematicName() {
        return cinematicName;
    }

    public void setCinematicName(String cinematicName) {
        this.cinematicName = cinematicName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    
    @Override
    public void forceAddConnections() {
        super.forceAddConnections();
        play();
        stop();
        pause();
        onPlay();
        onStop();
        onPause();
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(cinematicName, "cinematicName", null);
    }
    
    @Override
    public void read(JmeImporter im) throws IOException{
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        cinematicName = ic.readString("cinematicName", null);
    }
}

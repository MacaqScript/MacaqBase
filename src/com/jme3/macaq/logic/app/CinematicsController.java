/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.logic.app;

import com.jme3.app.state.AppStateManager;
import com.jme3.cinematic.Cinematic;
import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.logic.components.PlayCinematic;
import java.util.Map;

/**
 * Class responsible for handling the control of cinematics through Macaq.
 * Will automatically attach and detach the cinematic.
 * GOTCHA: Since getState returns the first instance of a class, it is only safe to have one Cinematic attached at a time.
 * @author Rickard <neph1 @ github>
 */
public class CinematicsController {
    
    private final Cinematic cinematic;
    private final AppStateManager stateManager;
    private final PlayCinematic playCinematic;
    
    private LogicInConnection play;
    private LogicInConnection stop;
    private LogicInConnection pause;
    
    public CinematicsController(AppStateManager stateManager, PlayCinematic playCinematic, Cinematic cinematic){
        this.stateManager = stateManager;
        this.cinematic = cinematic;
        this.playCinematic = playCinematic;
        
        playCinematic.onPlay().attachChild(play());
        playCinematic.onPause().attachChild(pause());
        playCinematic.onStop().attachChild(stop());
    }

    /**
     * Start the cinematic
     * @return
     */
    public LogicInConnection play(){
        if(play == null){
            play = new LogicInConnection((Map<String,Object> args) -> {
                if(stateManager.getState(Cinematic.class) == null){
                    stateManager.attach(cinematic);
                }
                cinematic.play();
            }, "play");
        }
        return play;
    }

    /**
     * Stop the cinematic
     * @return
     */
    public LogicInConnection stop(){
        if(stop == null){
            stop = new LogicInConnection((Map<String,Object> args) -> {
                cinematic.stop();
                stateManager.detach(cinematic);
            }, "stop");
        }
        return stop;
    }
    
    /**
     * Pause the cinematic
     * @return 
     */
    public LogicInConnection pause(){
        if(pause == null){
            pause = new LogicInConnection((Map<String,Object> args) -> {
                cinematic.pause();
            }, "pause");
        }
        return pause;
    }
}

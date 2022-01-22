/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jme3.macaq.logic.components;

import com.jme3.macaq.base.Updatable;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.base.LogicOutConnection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * This is a simple timer that fires an event when triggerTime is reached
 * Don't forget to start it.
 * @author Rickard <neph1 @ github>
 */
public class Timer extends AbstractLogicalComponent implements Updatable{

    private boolean stopped = true;

    private int time = 0; // time in ms
    private int triggerTime = 1000;

    private Callback tempEvent;

    public Timer(){
        super();
    }
    
    @Override
    public void updateComponent(float tpf) {
        if(!enabled) return;
        if(!stopped){
            time += tpf*1000;
            if(time >= triggerTime){
                Logger.getLogger(Timer.class.getSimpleName()).log(Level.FINE, "Timer triggered");
                onTrigger().execute(null);
                stopped = true;
                time = 0;
                stop().execute(null);
            }
        }
    }

    
    public LogicInConnection start(){
        LogicInConnection startTimer = inConnections.get("start");
        if(startTimer == null){
            startTimer = new LogicInConnection((Map<String,Object> args) -> {
                    if(enabled){
                        Logger.getLogger(Timer.class.getSimpleName()).log(Level.FINE, "Timer started");
                        stopped = false;
                        onStart().execute(populateArgs(args));
                    }
            }, "start");
            inConnections.put("start", startTimer);
        }
        return startTimer;
    }

    public LogicInConnection stop(){
        LogicInConnection stopTimer = inConnections.get("stop");
        if(stopTimer == null){
            stopTimer = new LogicInConnection((Map<String,Object> args) -> {
                    if(enabled){
                        stopped = true;
                        onStop().execute(populateArgs(args));
                    }
            }, "stop");
            inConnections.put("stop", stopTimer);
        }
        return stopTimer;
    }

    public LogicInConnection reset(){
        LogicInConnection resetTimer = inConnections.get("reset");
        if(resetTimer == null){
            resetTimer = new LogicInConnection((Map<String,Object> args) -> {
                    if(enabled) time = 0;
            }, "reset");
            inConnections.put("reset", resetTimer);
        }
        return resetTimer;
    }

    public LogicOutConnection onStop(){
        LogicOutConnection onStop = outConnections.get("onStop");
        if(onStop == null){
            onStop = new LogicOutConnection("onStop");
            outConnections.put("onStop", onStop);
        }
        return onStop;
    }

    public LogicOutConnection onStart(){
        LogicOutConnection onStart = outConnections.get("onStart");
        if(onStart == null){
            onStart = new LogicOutConnection("onStart");
            outConnections.put("onStart", onStart);
        }
        return onStart;
    }
    
    public LogicOutConnection onTrigger(){
        LogicOutConnection onTrigger = outConnections.get("onTrigger");
        if(onTrigger == null){
            onTrigger = new LogicOutConnection("onTrigger");
            outConnections.put("onTrigger", onTrigger);
        }
        return onTrigger;
    }

    public int getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(int triggerTime) {
        this.triggerTime = triggerTime;
    }
    
    

    public boolean isStopped(){
        return stopped;
    }

    public interface Callback{
        public Object[] call();
    }

    @Override
    public void forceAddConnections() {
        super.forceAddConnections();
        start();
        stop();
        onStart();
        onStop();
        reset();
        onTrigger();
    }
    
    
}

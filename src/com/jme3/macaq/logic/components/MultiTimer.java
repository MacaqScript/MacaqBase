/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jme3.macaq.logic.components;

import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.base.LogicOutConnection;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Rickard
 */
public class MultiTimer extends AbstractLogicalComponent{

    private boolean stopped = true;

    private int time = 0; // time in ms
    private int lastTime = 0;
    private int maxTime = -1;

    private HashMap<Integer, Callback> timerEvents = new HashMap<Integer, Callback>();
    private Callback tempEvent;

    public MultiTimer(){
        super();
    }
    
    public void update(float tpf) {
        if(!enabled) return;
        if(!stopped){
            time += tpf*1000;
            for(int i = lastTime; i < time; i++){
                tempEvent = timerEvents.get(i);
                if(tempEvent != null){
                    System.out.println("executing " + tempEvent);
                    tempEvent.call();
                    //tempEvent.execute(null);
                    tempEvent = null;
                }
            }
            
            if(time > maxTime){
                stopped = true;
                onStop().execute(null);
            }
            lastTime = time;
        }
    }

    
    public LogicInConnection start(){
        LogicInConnection startTimer = inConnections.get("start");
        if(startTimer == null){
            startTimer = new LogicInConnection((Map<String,Object> args) -> {
                    if(enabled) stopped = false;
            }, "start");
            inConnections.put("start", startTimer);
        }
        return startTimer;
    }

    public LogicInConnection stop(){
        LogicInConnection stopTimer = inConnections.get("stop");
        if(stopTimer == null){
            stopTimer = new LogicInConnection((Map<String,Object> args) -> {
                    if(enabled) stopped = true;
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

    /**
     * Add an event to the timer
     * @param time the time when the event should be triggered
     * @param callback the event to be triggered
     */
    public void addTimerEvent(int time, Callback callback){
        //System.out.println("adding event at "+ time + ", " + callback);
        timerEvents.put(time, callback);
        if(time > maxTime ) maxTime = time;
    }
    
    public LogicOutConnection addTimerEvent(int time){
        String key = "time"+time;
        LogicOutConnection timerEvent = outConnections.get(key);
        if(timerEvent == null){
            final LogicOutConnection newTimerEvent = new LogicOutConnection(key);
            outConnections.put(key, newTimerEvent);
            timerEvents.put(time, new Callback() {
                @Override
                public Object[] call() {
                    newTimerEvent.execute(null);
                    return null;
                }
            });
            if(time > maxTime ){
                maxTime = time;
            }
            timerEvent = newTimerEvent;
        }
        return timerEvent;
    }

    public HashMap<Integer, Callback> getTimerEvents() {
        return timerEvents;
    }

    public void setTimerEvents(HashMap<Integer, Callback> timerEvents) {
        this.timerEvents = timerEvents;
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
    }
    
    
}

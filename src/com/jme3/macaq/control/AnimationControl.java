/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.control;

import com.jme3.animation.AnimControl;
import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.base.LogicOutConnection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class AnimationControl extends MacaqControl{
    
    public AnimationControl(){
        
    }
    
    private LogicInConnection start;
    private LogicInConnection stop;
    
    private AnimControl animControl;
    
    
//    public LogicInConnection start(){
//        if(start == null){
//            start = new LogicInConnection((Map<String,Object> args) -> {
//                if(enabled){
//                    animControl.getChannel(0).
//                    animControl.getAnim((String) args.get("animation")).
//                }
//            }, "start");
//        }
//        return start;
//    }
//    
//    public LogicInConnection stop(){
//        if(stop == null){
//            stop = new LogicInConnection((Map<String,Object> args) -> {
//                if(enabled){
//                    if(args == null){
//                        args = new HashMap<>();
//                    }
//                    args.put("id", id);
//                    args.put("animName", animation);
//                    onStop().execute(args);
//                }
//            }, "stop");
//        }
//        return stop;
//    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.logic.components;

import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.base.LogicOutConnection;

/**
 * This is present in all scripts from the beginning and it's not recommended to remove it.
 * onStart is fired when the script is run.
 * @author Rickard <neph1 @ github>
 */
public class Start extends AbstractLogicalComponent{
    
    public Start(){
        super();
    }
    
    public LogicOutConnection onStart(){
        LogicOutConnection onStart = outConnections.get("onStart");
        if(onStart == null){
            onStart = new LogicOutConnection("onStart");
            outConnections.put("onStart", onStart);
        }
        return onStart;
    }
    
    @Override
    public void forceAddConnections() {
        super.forceAddConnections();
        onStart();
    }
    
}

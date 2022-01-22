/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jme3.macaq.logic.components;


import com.jme3.macaq.base.ExecutableLogic;
import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.base.LogicOutConnection;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import java.util.Collections;
import java.util.Map;
/**
 *
 * @author Rickard
 */
public class Interact extends AbstractLogicalComponent{

    
    public static final String INTERACT = "INTERACT";
    
    private boolean single = true;
    private int pointsRequired = 5;
    
    public enum Type{
        Normal, Search, Door, Locked_Door;
    }
    

    public Interact(){
    }

    public LogicOutConnection onInteract(){
        LogicOutConnection onTrigger = outConnections.get("onInteract");
        if(onTrigger == null){
            onTrigger = new LogicOutConnection();
            outConnections.put("onInteract", onTrigger);
        }
        return onTrigger;
    }

    public LogicInConnection<ExecutableLogic> interact(){
        LogicInConnection<ExecutableLogic> interact = inConnections.get(INTERACT);
        if(interact == null){
            interact = new LogicInConnection<ExecutableLogic>((Map<String,Object> args) -> {
                onInteract().execute(Collections.emptyMap());
                
                if(single){
                    setEnabled(false);
                }
            });
            inConnections.put(INTERACT, interact);
        }
        return interact;
    }

    public int getPointsRequired() {
        return pointsRequired;
    }

    public void setPointsRequired(int pointsRequired) {
        this.pointsRequired = pointsRequired;
    }

    public boolean isSingle() {
        return single;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    @Override
    public void forceAddConnections() {
        super.forceAddConnections();
        interact();
        onInteract();
    }
    
    
}

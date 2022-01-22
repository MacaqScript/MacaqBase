/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.logic.components;

import com.jme3.macaq.base.ExecutableLogic;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.base.LogicInConnection;
import java.util.Map;

/**
 *
 * @author Rickard
 */
public class AddMessage extends AbstractLogicalComponent{

    private String title;
    private String message;
    private AddMessageCallback callback;
    
    public AddMessage(AddMessageCallback callback, String title, String message){
        this.callback = callback;
        this.message = message;
        this.title = title;
    }
    
    public LogicInConnection show(){
        LogicInConnection show = inConnections.get("INTERACT");
        if(show == null){
            show = new LogicInConnection<ExecutableLogic>() {
                @Override
                public void execute(Map args) {
                    callback.onCallback(title, message);
                }
            };
            inConnections.put("INTERACT", show);
        }
        return show;
    }
    
    public interface AddMessageCallback{
        
        void onCallback(String title, String message);
    }
    
}

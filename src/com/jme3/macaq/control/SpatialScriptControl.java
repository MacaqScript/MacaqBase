/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.control;

import com.jme3.macaq.base.LogicInConnection;
import com.jme3.math.Vector3f;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class SpatialScriptControl extends MacaqControl {
    
    private HashMap<String, LogicInConnection> actions = new HashMap<>();
    
    private LinkedBlockingQueue<Action> queuedActions = new LinkedBlockingQueue<>();
    private Action currentAction;
    
    public LogicInConnection move(){
        LogicInConnection move = actions.get("move");
        if(move == null){
            move = new LogicInConnection((Map<String,Object> args) -> {
            }, "move");
            actions.put("move", move);
        }
        return move;
    }

    @Override
    protected void controlUpdate(float tpf) {
        super.controlUpdate(tpf);
        if(currentAction == null && !queuedActions.isEmpty()){
            currentAction = queuedActions.poll();
            currentAction.targetRotation = spatial.getLocalRotation().getRotationColumn(2);
        }
        if(currentAction != null){
            if(!currentAction.movementDone && currentAction.distanceTravelled < currentAction.moveVector.length()){
                currentAction.distanceTravelled += currentAction.speed * tpf;
                spatial.move(currentAction.moveVector.normalize().mult(currentAction.speed * tpf));
                if(currentAction.lookAt){
                    spatial.lookAt(currentAction.moveVector, Vector3f.UNIT_Y);
                }
            } else {
                currentAction.movementDone = true;
            }
            
            if(currentAction.movementDone && currentAction.rotationDone){
                currentAction = null;
            }
        }
    }
    
    public LogicInConnection reset(){
        LogicInConnection reset = actions.get("reset");
        if(reset == null){
            reset = new LogicInConnection((Map<String,Object> args) -> {
                queuedActions.clear();
            }, "reset");
            actions.put("reset", reset);
        }
        return reset;
    }
    
    

    private class Action {

        private Vector3f moveVector;
        private Vector3f targetRotation;
        private float speed;
        private float distanceTravelled;
        private boolean movementDone;
        private boolean rotationDone;
        private boolean lookAt;
        private boolean instant;
        
        public Action() {
        }
        
        
    }
}

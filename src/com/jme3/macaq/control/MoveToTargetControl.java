/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.control;

import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.base.LogicOutConnection;
import com.jme3.macaq.control.util.ControlUtil;
import com.jme3.macaq.logic.components.MoveToTarget;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class MoveToTargetControl extends MacaqControl {
    
    private Action currentAction;
    
    private static final float MIN_DISTANCE = 0.1f;
    
    private LogicInConnection move;
    private LogicInConnection reset;
    private LogicOutConnection onPerformed;
    
    /**
     * Stores actions from MoveToTarget objects, by name (of MoveToTarget)
     */
    private Map<String, Action> actions = new HashMap<>();
    
    /**
     * Useless constructor for serialization purposes.
     * @param moveToTarget 
     */
    public MoveToTargetControl(MoveToTarget moveToTarget){
        
    }
    
    public LogicInConnection move(){
        if(move == null){
            move = new LogicInConnection((Map<String,Object> args) -> {
                Logger.getLogger(getClass().getSimpleName()).log(Level.INFO, String.format("Performing move "));
                currentAction = actions.get(String.format("%s", args.get("id")));
            }, "move");
        }
        return move;
    }

    @Override
    protected void controlUpdate(float tpf) {
        super.controlUpdate(tpf);
        if(currentAction != null){
            if(!currentAction.movementDone && spatial.getLocalTranslation().distance(currentAction.targetLocation) > MIN_DISTANCE){
                Vector3f moveVector = currentAction.targetLocation.subtract(spatial.getLocalTranslation()).normalizeLocal();
                spatial.move(moveVector.mult(currentAction.speed * tpf));
                if(currentAction.lookAt){
                    spatial.lookAt(currentAction.targetLocation, Vector3f.UNIT_Y);
                }
            } else {
                currentAction.movementDone = true;
            }
            
            if(currentAction.movementDone){
                Map args = new HashMap<>();
                args.put("id", currentAction.originId);
                currentAction = null;
                onPerformed().execute(args);
                
                
            }
        }
    }
    
    public LogicInConnection reset(){
        if(reset == null){
            reset = new LogicInConnection((Map<String,Object> args) -> {
                
            }, "reset");
        }
        return reset;
    }
    
    public LogicOutConnection onPerformed(){
        if(onPerformed == null){
            onPerformed = new LogicOutConnection();
        }
        return onPerformed;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
    }
    
    public void initialize(MoveToTarget moveToTarget){
        Action action = new Action();
        
        Node root = ControlUtil.findRootNode(spatial);
        Spatial targetSpatial = root.getChild(moveToTarget.getTargetSpatialName());
        action.targetLocation = targetSpatial.getLocalTranslation();
        action.speed = moveToTarget.getSpeed();
        action.lookAt = moveToTarget.isLookAt();
        action.originId = moveToTarget.getId();
        actions.put(""+moveToTarget.getId(), action);
        
        moveToTarget.onMove().attachChild(move());
        moveToTarget.onReset().attachChild(reset());
        onPerformed().attachChild(moveToTarget.performed());
    }
    
    private class Action {

        private Vector3f targetLocation;
        private float speed;
        private boolean movementDone;
        private boolean lookAt;
        private int originId;
        
        public Action() {
        }
        
        
    }
}

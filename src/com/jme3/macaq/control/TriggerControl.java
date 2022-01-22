/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.control;

import com.jme3.collision.CollisionResults;
import com.jme3.macaq.control.util.ControlUtil;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import java.util.ArrayList;
import java.util.List;
import com.jme3.macaq.logic.components.Trigger;

/**
 * Attached to a spatial that acts as the triggers physical representation
 * Created and initialized during setup
 * @author Rickard <neph1 @ github>
 */
public class TriggerControl extends MacaqControl{
    
    List<Spatial> allowedActors = new ArrayList<>();
    private String[] actorNames;
    
    private Trigger triggerComponent;
    private float resetTimer = 0;
    private float resetTime = -1f;
    private boolean singleUse = true;
    
    public TriggerControl(){
        
    }
    
    public TriggerControl(Trigger trigger){
        this.triggerComponent = trigger;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if(!enabled){
            return;
        } else if(resetTimer > 0){
            resetTimer -= tpf;
        } else if(!allowedActors.isEmpty()){
            CollisionResults collisions = new CollisionResults();
            for(Spatial s: allowedActors){
                if(s.getWorldBound().collideWith(spatial.getWorldBound(), collisions) > 0 && triggerComponent != null){
                    triggerComponent.trigger().execute(null);
                    if(resetTime > 0 && !singleUse){
                        resetTimer = resetTime;
                    }
                }
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        initialize();
    }
    
    

    public void initialize(){
        resetTime = triggerComponent.getResetTime();
        singleUse = triggerComponent.isSingleUse();
        actorNames = triggerComponent.getActorNames();
        
        // find top node (doesn't have to be root) to be able to look for allowedActors
        if(spatial != null){
            Node root = ControlUtil.findRootNode(spatial);
                    
            for(String actorName: actorNames){
                Spatial actor = root.getChild(actorName);
                if(actor != null){
                    allowedActors.add(actor);
                }
            }
        }
    }

}

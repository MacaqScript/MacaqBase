/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.logic.components.spatial;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.base.LogicOutConnection;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class MoveSpatial extends AbstractLogicalComponent{
    
    private String targetSpatialName;
    private boolean instant;
    private boolean lookAt;
    
    public MoveSpatial(){
        super();
    }
    
    public LogicInConnection move(){
        
        LogicInConnection move = inConnections.get("move");
        if(move == null){
            move = new LogicInConnection((Map<String,Object> args) -> {
                if(!enabled) return;
                
            }, "move");
            inConnections.put("move", move);
        }
        return move;
    }
    
    public LogicOutConnection onMove(){
        LogicOutConnection onMove = outConnections.get("onMove");
        if(onMove == null){
            onMove = new LogicOutConnection("onMove");
            outConnections.put("onMove", onMove);
        }
        return onMove;
    }

    public String getTargetSpatialName() {
        return targetSpatialName;
    }

    public void setTargetSpatialName(String targetSpatialName) {
        this.targetSpatialName = targetSpatialName;
    }
    
    
    
    @Override
    public void read(JmeImporter im) throws IOException{
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        targetSpatialName = ic.readString("targetSpatialName", null);
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(targetSpatialName, "targetSpatialName", null);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jme3.macaq.base;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import java.io.IOException;
import com.jme3.macaq.logic.script.ScriptConstants;
import java.util.Map;

/**
 *
 * @author Rickard
 */
public abstract class LogicConnection implements Cloneable, Savable, ExecutableLogic {

    private int id = -1;
    
    private String name = "";
    
    public LogicConnection(String name){
        this.name = name;
    }
    
    public LogicConnection(){
        
    }

    @Override
    public abstract void execute(Map<String,Object> args);

    /**
     * Get the name of this connection
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * Set the name of this connection
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    public int getId() {
        if(id == -1){
            id = ScriptConstants.getNextId();
        }
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void write(JmeExporter ex) throws IOException{
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(getId(), "id", -1);
        capsule.write(name, "name", "default");
        
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        id = ic.readInt("id", -1);
        name = ic.readString("name", "default");
    }
    
    
}

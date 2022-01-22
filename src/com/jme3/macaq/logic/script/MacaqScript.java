/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.logic.script;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.util.IntMap;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import com.jme3.macaq.logic.components.Start;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.base.LogicConnection;
import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.base.LogicOutConnection;

/**
 * Note: Must have object in scriptMap when saving
 * @author Rickard <neph1 @ github>
 */
public class MacaqScript implements Savable{
    private String name = "Macaq Script";
    
    private IntMap<AbstractLogicalComponent> scriptMap = new IntMap<>();
    Map<Integer, LogicConnection> connectionMap = new HashMap<>();
    
    public MacaqScript(String name){
        this();
        this.name = name;
        addComponent(new Start());
    }
    
    public MacaqScript(){
    }
    
    public void addComponent(AbstractLogicalComponent component){
        scriptMap.put(component.getId(), component);
        component.populateOutConnections(connectionMap);
    }
    
    public void removeComponent(int id){
        AbstractLogicalComponent component = scriptMap.get(id);
        for(LogicInConnection in : component.getInConnections().values()){
            connectionMap.remove(in.getId());
        }
        for(LogicOutConnection out: component.getOutConnections().values()){
            connectionMap.remove(out.getId());
        }
        scriptMap.remove(id);
    }

    public IntMap<AbstractLogicalComponent> getScriptMap() {
        return scriptMap;
    }

    public void setScriptMap(IntMap<AbstractLogicalComponent> scriptMap) {
        this.scriptMap = scriptMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void removeConnection(int id){
        LogicConnection connection = connectionMap.get(id);
        if(connection instanceof LogicInConnection){
            for(LogicConnection c : connectionMap.values()){
                if(c instanceof LogicOutConnection){
                    for(LogicInConnection inCon : ((LogicOutConnection)c).getConnections()){
                        if(connection.getId() == inCon.getId()){
                            ((LogicOutConnection)c).removeChild(inCon);
                            break;
                        }
                    }
                }
            }
        } else {
            LogicOutConnection logicConnection = (LogicOutConnection) connection;
            for(LogicInConnection inCon : logicConnection.getConnections()){
                logicConnection.removeChild(inCon);
            }
        }
    }
    
    public void addConnection(int from, int to){
        LogicConnection fromConnection = connectionMap.get(from);
        LogicConnection toConnection = connectionMap.get(to);
        if(fromConnection != null && toConnection != null){
            ((LogicOutConnection)fromConnection).attachChild((LogicInConnection) toConnection);
        }
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        ex.getCapsule(this).write(name, "name", null);
        ex.getCapsule(this).writeIntSavableMap(scriptMap, "scriptMap", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        name = im.getCapsule(this).readString("name", null);
        IntMap<AbstractLogicalComponent> tempMap = (IntMap<AbstractLogicalComponent>) im.getCapsule(this).readIntSavableMap("scriptMap", null);
        if(tempMap != null){
            for(IntMap.Entry<AbstractLogicalComponent> comp : tempMap){
                scriptMap.put(comp.getValue().getId(), comp.getValue());
                comp.getValue().addToConnectionMap(connectionMap);
            }
            for(IntMap.Entry<AbstractLogicalComponent> comp : tempMap){
                comp.getValue().populateOutConnections(connectionMap);
            }
            
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof MacaqScript){
            return ((MacaqScript)o).hashCode() == hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.name);
        hash = 23 * hash + Objects.hashCode(this.scriptMap);
        return hash;
    }
    
    
}

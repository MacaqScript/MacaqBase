/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jme3.macaq.logic;

import com.jme3.macaq.base.LogicalComponentInterface;
import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.base.LogicOutConnection;
import com.jme3.macaq.base.LogicConnection;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.macaq.base.ExecutableLogic;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme3.macaq.logic.script.ScriptConstants;

/**
 * Abstract class that all logic is build upon.
 * 
 * @author Rickard
 */
public abstract class AbstractLogicalComponent implements LogicalComponentInterface, Savable{

    protected HashMap<String, LogicInConnection<ExecutableLogic>> inConnections = new HashMap<>();

    protected HashMap<String, LogicOutConnection> outConnections = new HashMap<>();

    protected int id = -1;
    protected boolean enabled = true;
    private int x = -1;
    private int y = -1;
    private String name;
    
    public AbstractLogicalComponent(){
        
    }

    @Override
    public void setEnabled(boolean enable){
        this.enabled = enable;
    }

    public LogicInConnection enable(){
        LogicInConnection enable = inConnections.get("enable");
        if(enable == null){
            enable = new LogicInConnection((Map<String,Object> args) -> {
                setEnabled(true);
            }, "enable");
            inConnections.put("enable", enable);
        }
        return enable;
    }

    public LogicInConnection disable(){
        LogicInConnection disable = inConnections.get("disable");
        if(disable == null){
            disable = new LogicInConnection((Map<String,Object> args) -> {
                setEnabled(false);
            }, "disable");
            inConnections.put("disable", disable);
        }
        return disable;
    }

    @Override
    public boolean isEnabled(){
        return enabled;
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
        capsule.write(name, "name", "Default");
        capsule.write(enabled, "enabled", true);
        capsule.write(x, "x", 0);
        capsule.write(y, "y", 0);
        String[] tempList = new String[inConnections.values().size()];
        int[] idList = new int[tempList.length];
        int it = 0;
        for(String s :inConnections.keySet()){
            idList[it] = inConnections.get(s).getId();
            tempList[it++] = s;
            
        }
        capsule.write(tempList, "inConnectionsList", null);
        capsule.write(idList, "inConnectionsIds", null);
        capsule.writeStringSavableMap(outConnections, "outConnections", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException{
        InputCapsule ic = im.getCapsule(this);
        id = ic.readInt("id", -1);
        name = ic.readString("name", null);
        enabled = ic.readBoolean("enabled", enabled);
        x = ic.readInt("x", 0);
        y = ic.readInt("y", 0);
        Class[] noparams = {};
        String[] loadedInConnections = ic.readStringArray("inConnectionsList", null);
        int[] idList = ic.readIntArray("inConnectionsIds", null);
        int it = 0;
        if(loadedInConnections != null){
            for(String s : loadedInConnections){
                try {
                    Method m = this.getClass().getMethod(s, noparams);
                    LogicInConnection in = (LogicInConnection) m.invoke(this, null);
                    in.setId(idList[it++]);
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(AbstractLogicalComponent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        HashMap<String, LogicOutConnection> loadedOutConnections = (HashMap<String, LogicOutConnection>) ic.readStringSavableMap("outConnections", null);
        if(loadedOutConnections != null){
            outConnections = loadedOutConnections;
        }
        ScriptConstants.setMaxId(id);
    }
    
    public void populateOutConnections(Map<Integer, LogicConnection> scriptMap){
        for(LogicOutConnection out : outConnections.values()){
            out.populateChildrenList(scriptMap);
        }
    }
    
    public void addToConnectionMap(Map<Integer, LogicConnection> scriptMap){
//        scriptMap.put(getId(), this);
        for(LogicInConnection in: inConnections.values()){
            scriptMap.put(in.getId(), in);
            ScriptConstants.setMaxId(in.getId());
        }
        for(LogicOutConnection out: outConnections.values()){
            scriptMap.put(out.getId(), out);
            ScriptConstants.setMaxId(out.getId());
        }
    }
    
    public void removeFromConnectionMap(Map<Integer, LogicConnection> scriptMap){
//        scriptMap.remove(getId(), this);
        for(LogicInConnection in: inConnections.values()){
            scriptMap.remove(in.getId(), in);
        }
        for(LogicOutConnection out: outConnections.values()){
            scriptMap.remove(out.getId(), out);
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public HashMap<String, LogicInConnection<ExecutableLogic>> getInConnections() {
        return inConnections;
    }

    public void setInConnections(HashMap<String, LogicInConnection<ExecutableLogic>> inConnections) {
        this.inConnections = inConnections;
    }

    public HashMap<String, LogicOutConnection> getOutConnections() {
        return outConnections;
    }

    public void setOutConnections(HashMap<String, LogicOutConnection> outConnections) {
        this.outConnections = outConnections;
    }
    
    public void forceAddConnections(){
        inConnections.put("enable", enable());
        inConnections.put("disable", disable());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    protected Map<String, Object> populateArgs(Map<String, Object> args){
        if(args == null){
            args = new HashMap<>();
        }
        args.put("id", id);
        args.put("name", name);
        return args;
    }
}

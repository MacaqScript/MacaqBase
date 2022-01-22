/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jme3.macaq.base;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.util.SafeArrayList;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


/**
 *
 * @author Rickard
 */
public class LogicOutConnection extends LogicConnection {

    private SafeArrayList<LogicInConnection> children = new SafeArrayList<>(LogicInConnection.class);
    private int[] tempConnections;

    public LogicOutConnection(String name){
        super(name);
    }
    
    public LogicOutConnection(){
    }
    
    @Override
    public void execute(Map<String,Object> args){
        if(args == null){
            args = new HashMap<>();
        }
        for(int i = 0; i < children.size(); i++){
            children.get(i).execute(args);
        }
    }

    @Override
    public LogicOutConnection clone(){
        LogicOutConnection connection = new LogicOutConnection();
        connection.children = (SafeArrayList<LogicInConnection>) children.clone();
        return connection;

    }

    /**
     * Connect a node that will be triggered by this connection
     * @param child the node to be connected
     */
    public void attachChild(LogicInConnection child){
        children.add(child);
        //child.setParent(this);
    }

    /**
     * Remove a node that should no longer be triggered by this connection
     * @param child
     */
    public void removeChild(LogicInConnection child){
        children.remove(child);
        //child.setParent(null);
    }
    
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        // save outgoing connections as ints
        int[] connections = new int[children.size()];
        int id = 0;
        for(LogicInConnection conn : children){
            connections[id++] = conn.getId();
        }
        capsule.write(connections, "connections", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        tempConnections = ic.readIntArray("connections", null);
    }
    
    public void populateChildrenList(Map<Integer, LogicConnection> scriptMap){
        if(tempConnections != null){
            for(int id: tempConnections){
                children.add((LogicInConnection) scriptMap.get(id));
                
            }
        }
        tempConnections = null;
    }
    
    public int getNumConnections(){
        return children.size();
    }
    
    public List<LogicInConnection> getConnections(){
        return children;
    }
}

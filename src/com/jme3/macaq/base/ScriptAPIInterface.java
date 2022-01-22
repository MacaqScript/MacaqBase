/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.base;

import com.jme3.macaq.logic.AbstractLogicalComponent;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public interface ScriptAPIInterface {
    
    /**
     * Add a component to the script
     * @param logic
     * @return true if successful
     */
    boolean addComponent(AbstractLogicalComponent logic);
    
    /**
     * Remove a component from the script
     * @param id id of component
     * @return true if successful
     */
    boolean removeComponent(int id);
    
    /**
     * Connect two connections
     * @param from
     * @param to
     * @return true if successful
     */
    boolean addConnection(LogicConnection from, LogicConnection to);
    
    /**
     * Connect two connections
     * @param from
     * @param to
     * @return true if successful
     */
    boolean addConnection(int from, int to);
    
    /**
     * Disconnect two connections
     * @param from
     * @param to
     * @return true if successful
     */
    boolean removeConnection(LogicConnection from, LogicConnection to);
    
    /**
     * Disconnects this connection from any other
     * @param id
     * @return 
     */
    boolean removeConnection(int id);
    
    /**
     * Moves the component to the specified location
     * @param id
     * @param x
     * @param y
     * @return 
     */
    boolean moveComponent(int id, int x, int y);
    
    /**
     * 
     * @param id
     * @return 
     */
    AbstractLogicalComponent getComponent(int id);
    
    /**
     * Save the script
     */
    void save();
    
    /**
     * Load
     */
    void load();
}

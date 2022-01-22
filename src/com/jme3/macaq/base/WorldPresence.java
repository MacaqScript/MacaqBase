/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.base;

import com.jme3.scene.control.Control;

/**
 * Interface that indicates this script component has a presence in the world.
 * @author Rickard <neph1 @ github>
 */
public interface WorldPresence {
    
    Class<? extends Control> getControlType();
    
    String getSpatialName();
    
    void setControlType(Class c);
    
    void setSpatialName(String spatialName);
    
}

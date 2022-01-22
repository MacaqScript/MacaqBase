/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.control;

import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.base.LogicOutConnection;
import com.jme3.scene.control.Control;
import java.util.Map;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public interface Notifyable extends Control{
    
    
    LogicInConnection message();
    
    LogicOutConnection onPerformed();
}

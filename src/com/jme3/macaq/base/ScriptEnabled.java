/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.base;


/**
 *
 * @author Rickard
 */
public interface ScriptEnabled {
    
    public LogicInConnection trigger();
    
    public LogicInConnection enable(boolean enable);
}

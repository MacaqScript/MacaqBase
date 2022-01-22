/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.base;

import com.jme3.macaq.logic.script.MacaqScript;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public interface ScriptListener {
    
    void scriptLoaded(MacaqScript script);
    
    void scriptSaved(MacaqScript script);
    
}

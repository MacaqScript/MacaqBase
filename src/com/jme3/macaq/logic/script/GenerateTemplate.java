/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.logic.script;

import com.jme3.export.binary.BinaryExporter;
import com.jme3.macaq.logic.components.Counter;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rickard
 */
public class GenerateTemplate{
    
    public static void main(String... args){
        MacaqScript script = new MacaqScript("Macaq Script");
//        Counter c = new Counter();
//        c.setX(100);
//        c.setY(100);
//        script.addComponent(c);
        
        try {
            BinaryExporter exporter = BinaryExporter.getInstance();
            exporter.save(script, new File("./", "MqsTemplate" + "." + ScriptConstants.EXT));
        } catch (IOException ex) {
            Logger.getLogger(GenerateTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.control.util;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Util class for doing spatial look ups
 * @author Rickard <neph1 @ github>
 */
public class ControlUtil {
    
    public static Node findRootNode(Spatial spatial){
        Node root = spatial.getParent();
        while(root.getParent() != null){
            root = root.getParent();
        }
        return root;
    }
}

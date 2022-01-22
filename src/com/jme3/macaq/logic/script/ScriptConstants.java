/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.logic.script;

/**
 * Class that keeps track of next id to assign new components
 * @author Rickard <neph1 @ github>
 */
public class ScriptConstants {
    public static String PATH = "./assets/Scripts";
    public static String EXT = "mqs";
    
    private static int ID = 0;
    
    private static ScriptConstants instance;
    
    public static ScriptConstants getInstance(){
        if(instance == null){
            instance = new ScriptConstants();
        }
        return instance;
    }
    
    public static int getNextId(){
        return ID++;
    }
    
    public static void reset(){
        ID = 0;
    }
    
    public static void setMaxId(int id){
        if(id > ID){
            ID = id + 1;
        }
    }
    
}

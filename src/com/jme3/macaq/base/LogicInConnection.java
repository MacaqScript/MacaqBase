/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jme3.macaq.base;

import java.util.Map;

/**
 *
 * @author Rickard
 * @param <T>
 */
public class LogicInConnection<T extends ExecutableLogic> extends LogicConnection {

    private T operation;
    
    public LogicInConnection(String name){
        super(name);
    }
    
    public LogicInConnection(){
    }
    
    public LogicInConnection(T operation){
        this.operation = operation;
    }
    
    /**
     * This constructor is required for the editor to find the connection
     * @param operation
     * @param name 
     */
    public LogicInConnection(T operation, String name){
        super(name);
        this.operation = operation;
    }
    
    @Override
    public LogicInConnection<T> clone() throws CloneNotSupportedException{
        super.clone();
        LogicInConnection<T> connection = new LogicInConnection<T>(getName()) {

        };
        return connection;
    }

    @Override
    public void execute(Map<String,Object> args) {
        if(operation != null){
            operation.execute(args);
        }
    }

    public T getOperation() {
        return operation;
    }

    public void setOperation(T operation) {
        this.operation = operation;
    }

}

package com.jme3.macaq.logic.components;

import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.base.LogicOutConnection;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.macaq.base.ExecutableLogic;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import java.util.Map;

public class Counter extends AbstractLogicalComponent{

    int count = 0;
    int triggerOnCount = 1;
    
    public Counter(){
    }

    public LogicOutConnection onTrigger(){
        LogicOutConnection onTrigger = outConnections.get("onTrigger");
        if(onTrigger == null){
            onTrigger = new LogicOutConnection("onTrigger");
            outConnections.put("onTrigger", onTrigger);
        }
        return onTrigger;
    }
    
    public LogicInConnection<ExecutableLogic> increase(){
        LogicInConnection<ExecutableLogic> updateProgress = inConnections.get("increase");
        if(updateProgress == null){
            updateProgress = new LogicInConnection<ExecutableLogic>((Map<String,Object> args) -> {
                if(enabled){
                    count++;
                    Logger.getLogger(Counter.class.getSimpleName()).log(Level.INFO, "Progress " + count);
                    if(count == triggerOnCount){
                        onTrigger().execute(populateArgs(args));
                    }
                }
            }, "increase");
            inConnections.put("increase", updateProgress);
        }
        return updateProgress;
    }

    public int getCount(){
        return count;
    }
    
    public void setCount(int count){
        this.count = count;
    }

    public int getTriggerOnCount() {
        return triggerOnCount;
    }

    public void setTriggerOnCount(int triggerOnCount) {
        this.triggerOnCount = triggerOnCount;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(count, "count", 0);
        capsule.write(triggerOnCount, "triggerOnCount", 1);
    }
    
    @Override
    public void read(JmeImporter im) throws IOException{
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        count = ic.readInt("count", 0);
        triggerOnCount = ic.readInt("triggerOnCount", 1);
    }
    
    @Override
    public void forceAddConnections(){
        super.forceAddConnections();
        inConnections.put("increase", increase());
        outConnections.put("onTrigger", onTrigger());
    }
}

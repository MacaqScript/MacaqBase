/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.asset;

import com.jme3.asset.cache.AssetCache;
import com.jme3.asset.cache.WeakRefCloneAssetCache;
import com.jme3.macaq.logic.script.MacaqScript;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class MacaqKey extends AssetKey<MacaqScript>{
    
    
    public MacaqKey(String name) {
        super(name);
    }

    public MacaqKey() {
        super();
    }
    
    @Override
    public Class<? extends AssetCache> getCacheType(){
        return WeakRefCloneAssetCache.class;
    }
    
    @Override
    public Class<? extends AssetProcessor> getProcessorType(){
        return CloneableAssetProcessor.class;
    }
}

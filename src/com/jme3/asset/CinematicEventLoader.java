/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.asset;

import com.jme3.cinematic.events.CinematicEvent;
import com.jme3.export.binary.BinaryImporter;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class CinematicEventLoader implements AssetLoader{

    @Override
    public Object load(AssetInfo assetInfo) throws IOException {
        BinaryImporter loader = BinaryImporter.getInstance();
        loader.setAssetManager(assetInfo.getManager());
        InputStream is = assetInfo.openStream();
        CinematicEvent cinematic = null;
        cinematic = (CinematicEvent) loader.load(is);
        if(is != null){
            
            is.close();
        }
        return cinematic;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.asset;

import com.jme3.export.binary.BinaryImporter;
import java.io.IOException;
import java.io.InputStream;
import com.jme3.macaq.logic.script.MacaqScript;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class MacaqLoader implements AssetLoader{

    @Override
    public Object load(AssetInfo assetInfo) throws IOException {
        BinaryImporter loader = BinaryImporter.getInstance();
        InputStream is = assetInfo.openStream();
        MacaqScript macaqScript = null;
        macaqScript = (MacaqScript) loader.load(is);
        if(is != null){
            is.close();
        }
        return macaqScript;
    }
    
}

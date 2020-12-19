package com.henryrenyz.clib;

import com.henryrenyz.clib.modules.configReader.Config;
import com.henryrenyz.clib.modules.configReader.ResourceConfig;

public class Main {
    public static void main(String[] args) {
        ResourceConfig d = new ResourceConfig("/config.yml");
        Config l = d.getConfig();
    }
}

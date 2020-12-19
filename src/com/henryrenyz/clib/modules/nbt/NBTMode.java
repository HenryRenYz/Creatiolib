package com.henryrenyz.clib.modules.nbt;

public enum NBTMode {

    ENTITY("entity"),
    BLOCK("block"),
    STORAGE("storage");

    private String key;
    NBTMode(String key) {
        this.key = key;
    }

}

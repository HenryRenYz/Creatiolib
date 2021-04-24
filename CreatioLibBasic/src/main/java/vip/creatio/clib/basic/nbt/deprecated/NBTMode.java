package vip.creatio.clib.basic.nbt.deprecated;

public enum NBTMode {

    ENTITY("entity"),
    BLOCK("block"),
    STORAGE("storage");

    private String key;
    NBTMode(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}

package com.henryrenyz.clib.modules;

public enum MessagePrefix {

    MAIN("FORMAT.PREFIX"),
    CORE_PROTECT("HOOK.CORE_PROTECT.PREFIX"),
    SKRIPT("HOOK.SKRIPT.PREFIX");

    private final String path;

    private MessagePrefix(String path) {
        this.path = path;
    }

    protected String getPath() {
        return "MAIN." + this.path;
    }

    public String getPrefix() {
        try {
            return Message.language.getString("MAIN." + this.path);
        } catch (NullPointerException e) {
            return "/prefix invalid/";
        }
    }

}

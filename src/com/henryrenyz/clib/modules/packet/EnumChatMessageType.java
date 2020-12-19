package com.henryrenyz.clib.modules.packet;

import com.henryrenyz.clib.modules.reflection.ReflectionClass;

public enum EnumChatMessageType {
    CHAT(ReflectionClass.ChatMessageType.c.getEnumConstants()[0]),
    SYSTEM(ReflectionClass.ChatMessageType.c.getEnumConstants()[1]),
    GAME_INFO(ReflectionClass.ChatMessageType.c.getEnumConstants()[2]);

    private Object nms;

    EnumChatMessageType(Object nms) {
        this.nms = nms;
    }

    Object getNMS() {return this.nms;}
}

package com.henryrenyz.clib.modules.rawText;

import com.henryrenyz.clib.modules.exception.JsonTypeException;

/**
 * The superclass of all Json elements
 */

public interface JsonBase extends Cloneable {

    String getJSON();

    JsonType getType();

    static JsonBase toBase(String raw) {
        if ((raw.charAt(0) == '"' && raw.charAt(1) == '{') || raw.charAt(0) == '{') {
            return Json.fromJSON(raw);
        } else if ((raw.charAt(0) == '"' && raw.charAt(1) == '[') || raw.charAt(0) == '[') {
            return new JsonList(raw);
        } else {
            return new JsonText(raw);
        }
    }


    TextColor[] getInherited();

    default String toSingleLine(TextColor... inherit) {
        if (this instanceof JsonText) {
            return ((JsonText) this).getText();
        } else if (this instanceof Json) {
            return ((Json) this).singleLine(inherit);
        } else if (this instanceof JsonList) {
            return ((JsonList) this).singleLine(inherit);
        }
        throw new JsonTypeException("No Such type of Json: " + this.getClass().getSimpleName());
    }
}

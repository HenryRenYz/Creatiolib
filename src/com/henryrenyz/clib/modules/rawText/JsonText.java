package com.henryrenyz.clib.modules.rawText;

public class JsonText implements JsonBase {

    private String text;

    public JsonText(String text) {
        if (text.charAt(0) == '"' && text.charAt(text.length() - 1) == '"')
            text = text.substring(1, text.length() - 1);
        this.text = text;
    }


    public void setText(String text) {
        this.text = text;
    }
    public String getText() {return this.text;}

    @Override
    public String getJSON() {
        return '"' + this.text + '"';
    }

    @Override
    public JsonType getType() {
        return JsonType.TEXT;
    }

    @Override
    public TextColor[] getInherited() {
        return new TextColor[0];
    }

    @Override
    public JsonText clone() {
        try {
            return (JsonText) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toString() {
        return this.text;
    }
}

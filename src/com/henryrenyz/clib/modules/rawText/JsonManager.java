package com.henryrenyz.clib.modules.rawText;

import com.henryrenyz.clib.modules.nbt.NBTItem;

import java.util.Collection;

public class JsonManager extends JsonList {

    public JsonManager() {
    }

    public JsonManager(String... text) {
        for (String s : text) {
            add(JsonBase.toBase(s));
        }
    }

    public JsonManager add(JsonBase... p) {
        super.add(p);
        return this;
    }

    public JsonManager add(int Index, JsonBase p) {
        super.add(Index, p);
        return this;
    }

    public JsonManager remove(int Index) {
        super.remove(Index);
        return this;
    }

    public JsonManager remove(JsonBase... p) {
        super.remove(p);
        return this;
    }

    public JsonManager addText(String text) {
        super.add(new Json.PlainText(text));
        return this;
    }

    public JsonManager addCommandText(String text, String cmd) {
        super.add(new Json.PlainText(text).setClickEvent(new Clickable.RunCommand(cmd)));
        return this;
    }

    public JsonManager addItemHoverText(String text, NBTItem item) {
        super.add(new Json.PlainText(text).setHoverEvent(new Hoverable.ShowItem(item)));
        return this;
    }

    public JsonManager addJsonHoverText(String text, JsonBase...  hover) {
        super.add(new Json.PlainText(text).setHoverEvent(new Hoverable.ShowText(hover)));
        return this;
    }

    public JsonManager addJsonHoverText(String text, Collection<JsonBase> hover) {
        super.add(new Json.PlainText(text).setHoverEvent(new Hoverable.ShowText(hover)));
        return this;
    }

    public JsonManager addJsonHoverText(String text, String...  hover) {
        super.add(new Json.PlainText(text).setHoverEvent(new Hoverable.ShowText(hover)));
        return this;
    }

    @Override
    public String getJSON() {
        return new Json.PlainText().setExtra(this).getJSON();
    }

    @Override
    public String toString() {
        return getJSON();
    }
}

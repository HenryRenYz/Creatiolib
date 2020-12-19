package com.henryrenyz.clib.modules.rawText;

import com.henryrenyz.clib.modules.nbt.NBTItem;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public abstract class Hoverable {

    public abstract JsonType.HoverEventType getContentType();

    public abstract String getJSON();

    //Show a message when hover above
    public static class ShowText extends Hoverable {

        private JsonList content;

        public ShowText() {
            this.content = new JsonList();
        }
        public ShowText(@NotNull JsonBase... raw) {
            this.content = new JsonList(raw);
        }

        public ShowText(@NotNull Collection<JsonBase> raw) {
            this.content = new JsonList(raw);
        }

        public ShowText(@NotNull String... raw) {
            content = new JsonList(raw);
        }

        public JsonType.HoverEventType getContentType() {
            return JsonType.HoverEventType.ShowText;
        }

        public ShowText setText(JsonBase... args) {
            this.content = new JsonList(args);
            return this;
        }

        public ShowText setText(Collection<JsonBase> args) {
            this.content = new JsonList(args);
            return this;
        }

        public JsonList getList() {
            return this.content;
        }

        public String getJSON() {
            return "\"hoverEvent\":{\"action\":\"show_text\",\"value\":" + this.content.getJSON() + "}";
        }
    }

    //Show an item when hover above
    public static class ShowItem extends Hoverable {

        private NBTItem content;

        public ShowItem() {
            this.content = new NBTItem();
        }
        public ShowItem(@NotNull NBTItem item) {
            this.content = item;
        }

        public JsonType.HoverEventType getContentType() {
            return JsonType.HoverEventType.ShowItem;
        }

        public ShowItem setItem(NBTItem item) {
            this.content = item;
            return this;
        }

        public String getJSON() {
            return "\"hoverEvent\":{\"action\":\"show_item\",\"value\":\"" + this.content.getNBT() + "\"}";
        }
    }

    //Show an entity when hover above
    public static class ShowEntity extends Hoverable {

        private String content;

        public ShowEntity() {
            this.content = "";
        }

        public ShowEntity(@NotNull String entitySNbt) {
            this.content = entitySNbt;
        }

        public JsonType.HoverEventType getContentType() {
            return JsonType.HoverEventType.ShowEntity;
        }

        public ShowEntity setNBT(String entitySNbt) {
            this.content = entitySNbt;
            return this;
        }

        public String getJSON() {
            return "\"hoverEvent\":{\"action\":\"show_entity\",\"value\":\"" + this.content + "\"}";
        }
    }
}

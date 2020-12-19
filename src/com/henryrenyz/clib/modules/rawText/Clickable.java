package com.henryrenyz.clib.modules.rawText;

import org.jetbrains.annotations.NotNull;

public abstract class Clickable {

    public abstract JsonType.ClickEventType getContentType();

    public abstract String getJSON();

    //Run command when click
    public static class RunCommand extends Clickable {

        private String content;

        public RunCommand() {
            this.content = "";
        }
        public RunCommand(@NotNull String command) {
            this.content = command;
        }

        public JsonType.ClickEventType getContentType() {
            return JsonType.ClickEventType.RunCommand;
        }

        public RunCommand setCommand(String command) {
            this.content = command;
            return this;
        }

        public String getJSON() {
            return "\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + this.content + "\"}";
        }
    }

    //Open URL to player when click
    public static class OpenURL extends Clickable {

        private String content;

        public OpenURL() {
            this.content = "";
        }
        public OpenURL(@NotNull String url) {
            this.content = url;
        }

        public JsonType.ClickEventType getContentType() {
            return JsonType.ClickEventType.OpenURL;
        }

        public OpenURL setURL(String url) {
            this.content = url;
            return this;
        }

        public String getJSON() {
            return "\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + this.content + "\"}";
        }
    }

    //Overwrite player's message when click
    public static class SuggestCommand extends Clickable {

        private String content;

        public SuggestCommand() {
            this.content = "";
        }
        public SuggestCommand(@NotNull String text) {
            this.content = text;
        }

        public JsonType.ClickEventType getContentType() {
            return JsonType.ClickEventType.SuggestCommand;
        }

        public SuggestCommand setText(String text) {
            this.content = text;
            return this;
        }

        public String getJSON() {
            return "\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + this.content + "\"}";
        }
    }

    //Change book page to specified page (can only be used in book text)
    public static class ChangePage extends Clickable {

        private int content;

        public ChangePage() {
            this.content = 1;
        }
        public ChangePage(@NotNull int page) {
            this.content = page;
        }

        public JsonType.ClickEventType getContentType() {
            return JsonType.ClickEventType.ChangePage;
        }

        public ChangePage setPage(int page) {
            this.content = page;
            return this;
        }

        public String getJSON() {
            return "\"clickEvent\":{\"action\":\"change_page\",\"value\":\"" + this.content + "\"}";
        }
    }

    //Copy a text to player's clipboard
    public static class CopyToClipboard extends Clickable {

        private String content;

        public CopyToClipboard() {
            this.content = "";
        }
        public CopyToClipboard(@NotNull String text) {
            this.content = text;
        }

        public JsonType.ClickEventType getContentType() {
            return JsonType.ClickEventType.CopyToClipboard;
        }

        public CopyToClipboard setText(String text) {
            this.content = text;
            return this;
        }

        public String getJSON() {
            return "\"clickEvent\":{\"action\":\"copy_to_clipboard\",\"value\":\"" + this.content + "\"}";
        }
    }
}

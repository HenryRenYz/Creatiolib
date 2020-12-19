package com.henryrenyz.clib.modules.rawText;

import java.lang.reflect.Constructor;

public enum JsonType {

    TEXT, PARAGRAPH, List;

    public enum ContentType {
        PlainText(Json.PlainText.class),
        TranslatedText(Json.TranslatedText.class),
        ScoreboardValue(Json.ScoreboardValue.class),
        EntityNames(Json.EntityNames.class),
        Keybind(Json.Keybind.class),
        NBTValue(Json.NBTValue.class);

        private Constructor<?> CONST;
        ContentType(Class<?> clazz) {
            try {
                CONST = clazz.getConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        public Json newInstance() {
            try {
                return (Json) this.CONST.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public enum ClickEventType {
        RunCommand(Clickable.RunCommand.class),
        OpenURL(Clickable.OpenURL.class),
        SuggestCommand(Clickable.SuggestCommand.class),
        ChangePage(Clickable.ChangePage.class),
        CopyToClipboard(Clickable.CopyToClipboard.class);


        private Constructor<?> CONST;
        ClickEventType(Class clazz) {
            try {
                CONST = clazz.getConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        public Json newInstance() {
            try {
                return (Json) this.CONST.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public enum HoverEventType {
        ShowText(Hoverable.ShowText.class),
        ShowItem(Hoverable.ShowItem.class),
        ShowEntity(Hoverable.ShowEntity.class);

        private Constructor<?> CONST;
        HoverEventType(Class<?> clazz) {
            try {
                CONST = clazz.getConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        public Json newInstance() {
            try {
                return (Json) this.CONST.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}


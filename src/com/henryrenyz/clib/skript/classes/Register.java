package com.henryrenyz.clib.skript.classes;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.henryrenyz.clib.modules.rawText.JsonManager;
import com.henryrenyz.clib.modules.rawText.Json;
import org.jetbrains.annotations.Nullable;;
import org.bukkit.scoreboard.Objective;

import static org.bukkit.Bukkit.getServer;

public class Register {
    static {
        //Anti conflict
        if (getServer().getPluginManager().getPlugin("Skellett") == null) {
            Classes.registerClass(new ClassInfo<>(Objective.class, "objective")
                    .user("objectives?")
                    .name("Objective")
                    .description("Represent a scoreboard objective.")
                    .examples("<none>")
                    .defaultExpression(new EventValueExpression<>(Objective.class))
                    .parser(new Parser<Objective>() {

                        @Override
                        @Nullable
                        public Objective parse(String input, ParseContext context) {
                            return null;
                        }

                        @Override
                        public boolean canParse(ParseContext context) {
                            return true;
                        }

                        @Override
                        public String toVariableNameString(Objective obj) {
                            return obj.getName();
                        }

                        @Override
                        public String getVariableNamePattern() {
                            return "\\S+";
                        }

                        @Override
                        public String toString(Objective obj, int flags) {
                            return toVariableNameString(obj);
                        }
                    }));
        }

        Classes.registerClass(new ClassInfo<>(JsonManager.class, "jsonmanager")
                .user("jsonmanagers?")
                .name("JsonManager")
                .description("A bunch of json paragraph, can create complicated raw text")
                .examples("<none>")
                .defaultExpression(new EventValueExpression<>(JsonManager.class))
                .parser(new Parser<JsonManager>() {

                    @Override
                    @Nullable
                    public JsonManager parse(String input, ParseContext context) {
                        return (JsonManager) new JsonManager().add(Json.fromJSON(input));
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return false;
                    }

                    @Override
                    public String toString(JsonManager j, int i) {
                        return toVariableNameString(j);
                    }

                    @Override
                    public String toVariableNameString(JsonManager j) {
                        return j.getJSON();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return "\\S+";
                    }

                }));

        Classes.registerClass(new ClassInfo<>(Json.class, "jsonparagraph")
                .user("jsonparagraphs?")
                .name("JsonParagraph")
                .description("A paragraph of raw text")
                .examples("<none>")
                .defaultExpression(new EventValueExpression<>(Json.class))
                .parser(new Parser<Json>() {

                    @Override
                    @Nullable
                    public Json parse(String input, ParseContext context) {
                        return Json.fromJSON(input);
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return false;
                    }

                    @Override
                    public String toString(Json j, int i) {
                        return toVariableNameString(j);
                    }

                    @Override
                    public String toVariableNameString(Json j) {
                        return j.getJSON();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return "\\S+";
                    }

                }));
//        Classes.registerClass(new ClassInfo<>(JsonManager.class, "jsonparagraph")
//                .user("jsonparagraphs?")
//                .name("JsonParagraph")
//                .description("A paragraph of a war text")
//                .examples("<none>")
//                .defaultExpression(new EventValueExpression<>(JsonParagraph.class))
//                .parser(new Parser<JsonParagraph>() {
//
//                    @Override
//                    @Nullable
//                    public JsonParagraph parse(String input, ParseContext context) {
//                        return JsonParagraph.deserialize(input);
//                    }
//
//                    @Override
//                    public boolean canParse(ParseContext context) {
//                        return true;
//                    }
//
//                    @Override
//                    public String toString(JsonParagraph j, int i) {
//                        return toVariableNameString(j);
//                    }
//
//                    @Override
//                    public String toVariableNameString(JsonParagraph j) {
//                        return j.toJson();
//                    }
//
//                    @Override
//                    public String getVariableNamePattern() {
//                        return "\\S+";
//                    }
//
//                }));
    }
}

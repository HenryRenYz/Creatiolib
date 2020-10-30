package com.henryrenyz.creatiolib.skript.classes;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.sun.istack.internal.Nullable;
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
                            return false;
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
    }
}

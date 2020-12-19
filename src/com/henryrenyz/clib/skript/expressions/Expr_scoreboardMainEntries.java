package com.henryrenyz.clib.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.jetbrains.annotations.Nullable;;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

@Name("Main Scoreboard Entries")
@Description({"Returns a list which contains all the entries in server main scoreboard", "Be note that entry is not a entity or player, it just a string."})
@Since("0.1.00")
public class Expr_scoreboardMainEntries extends SimpleExpression<String> {

    static {
        Skript.registerExpression(Expr_scoreboardMainEntries.class, String.class, ExpressionType.COMBINED, "[the] (main|primary) scoreboard entry list");
    }


    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "[the] (server|main|primary) scoreboard entry list";
    }

    @Override
    @Nullable
    protected String[] get(Event event) {
        return Bukkit.getScoreboardManager().getMainScoreboard().getEntries().toArray(new String[0]);
    }
}
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
import org.bukkit.scoreboard.Objective;

@Name("Main Scoreboard Objective")
@Description({"Returns an objective in main scoreboard", "The return value is a Bukkit Objective, can be used in scoreboard operation syntaxes."})
@Since("0.1.00")
public class Expr_scoreboardMainObjective extends SimpleExpression<Objective> {

    static {
        Skript.registerExpression(Expr_scoreboardMainObjective.class, Objective.class, ExpressionType.COMBINED, "[the] (main|primary) scoreboard objective %string%");
    }

    private Expression<String> board;

    @Override
    public Class<? extends Objective> getReturnType() {
        return Objective.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        board = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "[the] (server|main|primary) scoreboard objective %string%";
    }

    @Override
    @Nullable
    protected Objective[] get(Event event) {
        try {
            return new Objective[] {Bukkit.getScoreboardManager().getMainScoreboard().getObjective(board.getSingle(event))};
        } catch (NullPointerException e) {
            return null;
        }
    }
}
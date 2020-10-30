package com.henryrenyz.creatiolib.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.sun.istack.internal.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.Objective;

public class Expr_scoreboardMainObjectiveList extends SimpleExpression<Objective> {

    static {
        Skript.registerExpression(Expr_scoreboardMainObjectiveList.class, Objective.class, ExpressionType.COMBINED, "[the] (main|primary) scoreboard list [with criteria %-string%]");
    }

    private Expression<String> criteria;

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
        criteria = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "[the] (server|main|primary) scoreboard list [with criteria %-string%]";
    }

    @Override
    @Nullable
    protected Objective[] get(Event event) {
        if (criteria != null) {
            return Bukkit.getScoreboardManager().getMainScoreboard().getObjectivesByCriteria(criteria.getSingle(event)).toArray(new Objective[0]);
        } else {
            return Bukkit.getScoreboardManager().getMainScoreboard().getObjectives().toArray(new Objective[0]);
        }
    }
}
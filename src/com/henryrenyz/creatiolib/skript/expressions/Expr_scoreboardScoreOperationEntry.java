package com.henryrenyz.creatiolib.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.sun.istack.internal.Nullable;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.Objective;

@Name("Scores of a Entry on Scoreboard Objective")
@Description({"Represents scores of a entry on a objective, can be set, add, remove, and delete."})
@Since("0.1.00")
public class Expr_scoreboardScoreOperationEntry extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(Expr_scoreboardScoreOperationEntry.class, Number.class, ExpressionType.COMBINED, "[the] score of entry %string% in [objective] %objective%");
    }

    private Expression<String> entry;
    private Expression<Objective> obj;

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        entry = (Expression<String>) exprs[0];
        obj = (Expression<Objective>) exprs[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "[the] score of entry %string% in [objective] %objective%";
    }

    @Override
    @Nullable
    protected Number[] get(Event event) {
        try {
            return new Number[] {obj.getSingle(event).getScore(entry.getSingle(event)).getScore()};
        } catch (NullPointerException e) {
            return null;
        }

    }

    @Override
    public void change(Event event, Object[] delta, Changer.ChangeMode mode){
        Integer score = obj.getSingle(event).getScore(entry.getSingle(event)).getScore();
        if (mode == Changer.ChangeMode.SET) {
            score = ((Number) delta[0]).intValue();
        } else if (mode == Changer.ChangeMode.ADD) {
            score += ((Number) delta[0]).intValue();
        } else if (mode == Changer.ChangeMode.REMOVE) {
            score -= ((Number) delta[0]).intValue();
        } else if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) {
            score = 0;
        }
        obj.getSingle(event).getScore(entry.getSingle(event)).setScore(score);
    }

    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.REMOVE || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(Number.class);
        }
        return null;
    }
}

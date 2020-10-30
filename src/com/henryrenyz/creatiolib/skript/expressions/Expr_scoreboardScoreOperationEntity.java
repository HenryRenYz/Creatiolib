package com.henryrenyz.creatiolib.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.sun.istack.internal.Nullable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.Objective;

public class Expr_scoreboardScoreOperationEntity extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(Expr_scoreboardScoreOperationEntity.class, Number.class, ExpressionType.COMBINED, "[the] score of entity %entity% in [objective] %objective%");
    }

    private Expression<Entity> entity;
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
        entity = (Expression<Entity>) exprs[0];
        obj = (Expression<Objective>) exprs[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "[the] score of entity %entity% in [objective] %objective%";
    }

    @Override
    @Nullable
    protected Number[] get(Event event) {
        String Entry = getEntry(event);
        return new Number[] {obj.getSingle(event).getScore(Entry).getScore()};
    }


    //Get Entity Entry ID based on whether it's entity or a player
    private String getEntry(Event event) {
        Entity e = this.entity.getSingle(event);
        if (e instanceof Player) {
            return ((Player) e).getName();
        } else {
            return e.getUniqueId().toString();
        }
    }

    @Override
    public void change(Event event, Object[] delta, Changer.ChangeMode mode){
        String Entry = getEntry(event);
        Integer score = obj.getSingle(event).getScore(Entry).getScore();
        if (mode == Changer.ChangeMode.SET) {
            score = ((Number) delta[0]).intValue();
        } else if (mode == Changer.ChangeMode.ADD) {
            score += ((Number) delta[0]).intValue();
        } else if (mode == Changer.ChangeMode.REMOVE) {
            score -= ((Number) delta[0]).intValue();
        } else if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) {
            score = 0;
        }
        obj.getSingle(event).getScore(Entry).setScore(score);
    }

    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.REMOVE || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(Number.class);
        }
        return null;
    }
}

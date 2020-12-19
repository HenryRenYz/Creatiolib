package com.henryrenyz.clib.skript.expressions;

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
import com.henryrenyz.clib.modules.Message;
import com.henryrenyz.clib.modules.MessagePrefix;
import org.jetbrains.annotations.Nullable;;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.Objective;

import java.util.logging.Level;

@Name("Scores on Scoreboard Objective")
@Description({"Represents scores of a entity or entry on a objective, can be set, add, remove, and delete."})
@Since("0.1.00")
public class Expr_scoreboardScoreOperation extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(Expr_scoreboardScoreOperation.class, Number.class, ExpressionType.COMBINED, "[the] score of (1¦entity %-entity%|2¦entry %-string%) in [objective] %objective%");
    }

    private Expression<Entity> entity;
    private Expression<String> entry;
    private Expression<Objective> obj;
    private Integer mark;

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
        entry = (Expression<String>) exprs[1];
        obj = (Expression<Objective>) exprs[2];
        this.mark = parser.mark;
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "[the] score of (1¦entity %-entity%|2¦entry %-string%) in [objective] %objective%";
    }

    @Override
    @Nullable
    protected Number[] get(Event event) {
        try {
            return new Number[] {obj.getSingle(event).getScore(getEntry(event)).getScore()};
        } catch (NullPointerException e) {
            return null;
        }
    }


    //Get Entity Entry ID based on whether it's entity or a player
    private String getEntry(Event event) {
        if (mark == 1) {
            if (entity == null) {
                Message.sendStatic(Level.SEVERE, MessagePrefix.SKRIPT, "MAIN.ERROR.IS_NULL", new String[]{"Target Entity"});
                return null;
            }
            Entity e = this.entity.getSingle(event);
            if (e instanceof Player) {
                return e.getName();
            } else {
                return e.getUniqueId().toString();
            }
        } else {
            if (entry == null) {
                Message.sendStatic(Level.SEVERE, MessagePrefix.SKRIPT, "MAIN.ERROR.IS_NULL", new String[]{"Target Entry"});
                return null;
            }
            return entry.getSingle(event);
        }
    }

    @Override
    public void change(Event event, Object[] delta, Changer.ChangeMode mode){
        String Entry = getEntry(event);
        if (Entry == null) return;
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

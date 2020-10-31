package com.henryrenyz.creatiolib.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sun.istack.internal.Nullable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.Objective;

@Name("Has Entity Score Set on Objective")
@Description({"Whether a entity's score is set on an objective."})
@Since("0.1.00")
public class Cond_scoreboardScoreExistEntity extends Condition {

    static {
        Skript.registerCondition(Cond_scoreboardScoreExistEntity.class, "[the] score of entry %string% in [objective] %objective% (1¦is set|2¦is(n't| not) set)");
    }

    private Expression<Entity> entity;
    private Expression<Objective> obj;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.entity = (Expression<Entity>) expressions[0];
        this.obj = (Expression<Objective>) expressions[1];
        this.setNegated(parser.mark == 1);
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "[the] score of entry %string% in [objective] %objective% (1¦is set|2¦is(n't| not) set)";
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
    public boolean check(Event event) {
        String Entry = getEntry(event);
        Object score = obj.getSingle(event).getScore(Entry);
        if (score != null) {
            return this.isNegated();
        } else {
            return !this.isNegated();
        }
    }
}


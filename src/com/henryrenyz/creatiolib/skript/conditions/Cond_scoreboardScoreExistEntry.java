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
import org.bukkit.event.Event;
import org.bukkit.scoreboard.Objective;

@Name("Has Entry Score Set on Objective")
@Description({"Whether a entry's score is set on an objective."})
@Since("0.1.00")
public class Cond_scoreboardScoreExistEntry extends Condition {

    static {
        Skript.registerCondition(Cond_scoreboardScoreExistEntry.class, "[the] score of entry %string% in [objective] %objective% (1¦is set|2¦is(n't| not) set)");
    }

    private Expression<String> entry;
    private Expression<Objective> obj;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.entry = (Expression<String>) expressions[0];
        this.obj = (Expression<Objective>) expressions[1];
        this.setNegated(parser.mark == 1);
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "[the] score of entry %string% in [objective] %objective% (1¦is set|2¦is(n't| not) set)";
    }

    @Override
    public boolean check(Event event) {
        try {
            if (obj.getSingle(event).getScore(entry.getSingle(event)).isScoreSet()) {
                return this.isNegated();
            } else {
                return !this.isNegated();
            }
        } catch (NullPointerException e) {
            return !this.isNegated();
        }
    }
}


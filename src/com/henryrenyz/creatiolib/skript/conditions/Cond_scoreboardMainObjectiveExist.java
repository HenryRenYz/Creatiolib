package com.henryrenyz.creatiolib.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sun.istack.internal.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

@Name("Is Main Scoreboard Objective Exist")
@Since("0.1.00")
public class Cond_scoreboardMainObjectiveExist extends Condition {

    static {
        Skript.registerCondition(Cond_scoreboardMainObjectiveExist.class, "(main|primary) scoreboard [objective] %string% (1¦exist[s]|2¦do[es](n't| not) exist)");
    }

    private Expression<String> name;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.name = (Expression<String>) expressions[0];
        this.setNegated(parser.mark == 1);
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "(main|primary) scoreboard [objective] %string% (1¦exist[s]|2¦do[es](n't| not) exist)";
    }

    @Override
    public boolean check(Event event) {
        Object obj = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(name.getSingle(event));
        if (obj != null) {
            return this.isNegated();
        } else {
            return !this.isNegated();
        }
    }
}


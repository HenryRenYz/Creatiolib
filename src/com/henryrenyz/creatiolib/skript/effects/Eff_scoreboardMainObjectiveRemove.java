package com.henryrenyz.creatiolib.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sun.istack.internal.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.Objective;

@Name("Remove existing main scoreboard objective")
@Description({"Remove an objective from main scoreboard(vanilla scoreboard)."})
@Examples({"main scoreboard unregister objective \"PlayerCount\"", "\tprimary scoreboard remove objective {_objective}"})
@Since("0.1.00")
public class Eff_scoreboardMainObjectiveRemove extends Effect {

    static {
        Skript.registerEffect(Eff_scoreboardMainObjectiveRemove.class, "(main|primary) scoreboard (remove|unregister) [existing] objective %objective%");
    }

    private Expression<Objective> board;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.board = (Expression<Objective>) expressions[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "(server|main|primary) scoreboard (remove|unregister) [existing] objective %objective%";
    }

    @Override
    protected void execute(Event event) {
        board.getSingle(event).unregister();
    }
}


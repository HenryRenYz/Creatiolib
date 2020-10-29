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

@Name("Create new main scoreboard objective")
@Description({"Create a new objective in main scoreboard(vanilla scoreboard)."})
@Examples({"main scoreboard create objective \"PlayerCount\" with criteria \"dummy\" and display name \"Player - Count\""})
@Since("0.1.00")
public class Eff_scoreboardMainObjectiveCreate extends Effect {

    static {
        Skript.registerEffect(Eff_scoreboardMainObjectiveCreate.class, "(server|main|primary) scoreboard (add|create|register) [new] objective [named] %string% [with] criteria %string% [and|with] [display] name %string%");
    }

    private Expression<String> board;
    private Expression<String> criteria;
    private Expression<String> name;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.board = (Expression<String>) expressions[0];
        this.criteria = (Expression<String>) expressions[1];
        this.name = (Expression<String>) expressions[2];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "(server|main|primary) scoreboard (add|create|register) [new] objective [named] %string% [with] criteria %string% [and|with] [display] name %string%";
    }

    @Override
    protected void execute(Event event) {
        Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective(board.getSingle(event),criteria.getSingle(event),name.getSingle(event));
    }
}


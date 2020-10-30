package com.henryrenyz.creatiolib.skript.modules.CoreProtect;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.henryrenyz.creatiolib.modules.hook_CoreProtect;
import com.sun.istack.internal.Nullable;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Location;
import org.bukkit.event.Event;

public class EffCP_logInteraction extends Effect {

    static {
        Skript.registerEffect(EffCP_logInteraction.class, "log [core[ ]protect] [block] interaction [registered] as %string% at %location%");
    }

    private Expression<String> string;
    private Expression<Location> location;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.string = (Expression<String>) expressions[0];
        this.location = (Expression<Location>) expressions[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "log [core[ ]protect] [block] interaction [registered] as %string% at %location%";
    }

    @Override
    protected void execute(Event event) {
        CoreProtectAPI CoreProtect = hook_CoreProtect.getCoreProtect();
        CoreProtect.logInteraction(string.getSingle(event), location.getSingle(event));
    }
}


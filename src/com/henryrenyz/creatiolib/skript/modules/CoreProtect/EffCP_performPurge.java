package com.henryrenyz.creatiolib.skript.modules.CoreProtect;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import com.henryrenyz.creatiolib.modules.hook_CoreProtect;
import com.sun.istack.internal.Nullable;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.event.Event;

public class EffCP_performPurge extends Effect {

    static {
        Skript.registerEffect(EffCP_performPurge.class, "purge core[ ]protect data (earlier than|before) %timespan%");
    }

    private Expression<Timespan> timespan;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.timespan = (Expression<Timespan>) expressions[0];
         return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "purge core[ ]protect data (earlier than|before) %timespan%";
    }

    @Override
    protected void execute(Event event) {
        CoreProtectAPI CoreProtect = hook_CoreProtect.getCoreProtect();
        if (CoreProtect == null)  return;
        else {
            int Timespan = (int)timespan.getSingle(event).getMilliSeconds()/1000;
            CoreProtect.performPurge(Timespan);
        }
    }
}


package com.henryrenyz.creatiolib.skript.modules.CoreProtect;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.henryrenyz.creatiolib.modules.hook_CoreProtect;
import com.sun.istack.internal.Nullable;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class EffCP_logCommand extends Effect {

    static {
        Skript.registerEffect(EffCP_logCommand.class, "log [core[ ]protect] [player] command as %player% with [cmd|command] %string%");
    }

    private Expression<Player> player;
    private Expression<String> string;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.player = (Expression<Player>) expressions[0];
        this.string = (Expression<String>) expressions[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "log [core[ ]protect] [player] command as %player% with [cmd|command] %string%";
    }

    @Override
    protected void execute(Event event) {
        CoreProtectAPI CoreProtect = hook_CoreProtect.getCoreProtect();
        if (CoreProtect == null)  return;
        else {
            CoreProtect.logCommand(player.getSingle(event), string.getSingle(event));
        }
    }
}


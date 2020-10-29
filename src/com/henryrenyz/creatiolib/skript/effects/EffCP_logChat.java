package com.henryrenyz.creatiolib.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.henryrenyz.creatiolib.plugins.API_CoreProtect;
import com.sun.istack.internal.Nullable;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class EffCP_logChat extends Effect {

    static {
        Skript.registerEffect(EffCP_logChat.class, "log [core[ ]protect] [player] chat[ting] as %player% with [msg|message] %string%");
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
        return "log [core[ ]protect] [player] chat[ting] as %player% with [msg|message] %string%";
    }

    @Override
    protected void execute(Event event) {
        if (player == null)  return;
        if (string == null)  return;

        CoreProtectAPI CoreProtect = API_CoreProtect.getCoreProtect();
        if (CoreProtect == null)  return;
        else {
            CoreProtect.logChat(player.getSingle(event), string.getSingle(event));
        }
    }
}


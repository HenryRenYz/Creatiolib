package com.henryrenyz.creatiolib.skript.modules.CoreProtect;

import com.henryrenyz.creatiolib.modules.hook_CoreProtect;
import com.sun.istack.internal.Nullable;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.block.data.BlockData;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

public class EffCP_logPlacement extends Effect {

    static {
        Skript.registerEffect(EffCP_logPlacement.class, "log [core[ ]protect] [block] place[ |ment] [registered] as %string% at %location% placed [a] %string% [[with] [block]data %-blockdata%]");
    }

    private Expression<String> string;
    private Expression<Location> location;
    private Expression<String> material;
    private Expression<BlockData> blockdata;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.string = (Expression<String>) expressions[0];
        this.location = (Expression<Location>) expressions[1];
        this.material = (Expression<String>) expressions[2];
        this.blockdata = (Expression<BlockData>) expressions[3];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "log [core[ ]protect] [block] place[ |ment] [registered] as %player% at %location% placed %material% [[with] [block]data %-blockdata%]";
    }

    @Override
    protected void execute(Event event) {
        CoreProtectAPI CoreProtect = hook_CoreProtect.getCoreProtect();
        if (CoreProtect == null)  return;
        else {
            BlockData data = null;
            Material mat = Material.getMaterial(material.getSingle(event));
            if (blockdata != null) {
                data = blockdata.getSingle(event);
            }
            CoreProtect.logPlacement(string.getSingle(event), location.getSingle(event), mat, data);
        }
    }
}


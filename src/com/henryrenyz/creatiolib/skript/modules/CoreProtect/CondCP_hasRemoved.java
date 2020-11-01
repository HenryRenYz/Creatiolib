package com.henryrenyz.creatiolib.skript.modules.CoreProtect;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import com.henryrenyz.creatiolib.Creatio;
import com.henryrenyz.creatiolib.modules.hook_CoreProtect;
import com.sun.istack.internal.Nullable;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

public class CondCP_hasRemoved extends Condition {

    static {
        Skript.registerCondition(CondCP_hasRemoved.class, "[core[ ]protect] %string% (1¦[have|has|had]|1¦[hav|has|had](n't| not)) removel %block% before %timespan% [ago] offset [by] %timespan%");
    }

    private Expression<String> string;
    private Expression<Block> block;
    private Expression<Timespan> timespan1;
    private Expression<Timespan> timespan2;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.string = (Expression<String>) expressions[0];
        this.block = (Expression<Block>) expressions[1];
        this.timespan1 = (Expression<Timespan>) expressions[2];
        this.timespan2 = (Expression<Timespan>) expressions[3];
        this.setNegated(parser.mark == 1);
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "[core[ ]protect] %string% (1¦[have|has|had]|1¦[hav|has|had](n't| not)) removed %block% before %timespan% [ago] offset [by] %timespan%";
    }

    @Override
    public boolean check(Event event) {
        if (Creatio.hooked_Coreprotect) {
            CoreProtectAPI CoreProtect = hook_CoreProtect.getCoreProtect();
            int T1 = (int)timespan1.getSingle(event).getMilliSeconds()/1000;
            int T2 = (int)timespan2.getSingle(event).getMilliSeconds()/1000;
            boolean enable = CoreProtect.hasRemoved(string.getSingle(event),block.getSingle(event),T1,T2);
            if (enable == true) return this.isNegated();
            else return !this.isNegated();
        } else {
            return false;
        }
    }
}


package com.henryrenyz.creatiolib.skript.conditions;

import com.henryrenyz.creatiolib.plugins.API_CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.event.Event;
import com.sun.istack.internal.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

public class CondCP_isEnabled extends Condition {

    static {
        Skript.registerCondition(CondCP_isEnabled.class, "core[ ]protect (1¦is|2¦is(n't| not)) enable[d]");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.setNegated(parser.mark == 1);
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "core[ ]protect (is|is(n't| not)) enable[d]";
    }

    @Override
    public boolean check(Event arg0) {
        CoreProtectAPI CoreProtect = API_CoreProtect.getCoreProtect();
        boolean enable = CoreProtect.isEnabled();
        if (enable == true) {
            return this.isNegated();
        } else {
            return !this.isNegated();
        }
    }
}


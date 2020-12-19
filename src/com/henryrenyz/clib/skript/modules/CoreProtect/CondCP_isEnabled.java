package com.henryrenyz.clib.skript.modules.CoreProtect;

import com.henryrenyz.clib.Creatio;
import com.henryrenyz.clib.modules.hook_CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

public class CondCP_isEnabled extends Condition {

    static {
        Skript.registerCondition(CondCP_isEnabled.class, "core[ ]protect (1¦is|2¦is(n't| not)) enable[d]");
    }

    private Boolean enable;

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
    public boolean check(Event event) {
        if (Creatio.getInstance().hookedCP()) {
            CoreProtectAPI CoreProtect = hook_CoreProtect.getCoreProtect();
            enable = CoreProtect.isEnabled();
        } else {
            enable = false;
        }
        if (enable) {
            return this.isNegated();
        } else {
            return !this.isNegated();
        }
    }
}


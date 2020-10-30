package com.henryrenyz.creatiolib.skript.modules.CoreProtect;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import com.henryrenyz.creatiolib.modules.hook_CoreProtect;
import com.sun.istack.internal.Nullable;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

import java.util.List;

public class ExprCP_blockLookup extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprCP_blockLookup.class, String.class, ExpressionType.COMBINED, "[the] core[ ]protect block lookup (result|index) %number% of %block% [from] last %timespan%");
    }

    private Expression<Number> index;
    private Expression<Block> block;
    private Expression<Timespan> timespan;

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        index = (Expression<Number>) exprs[0];
        block = (Expression<Block>) exprs[1];
        timespan = (Expression<Timespan>) exprs[2];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "[the] core[ ]protect block lookup (result|index) %number% of %block% [from] last %timespan%";
    }

    @Override
    @Nullable
    protected String[] get(Event event) {
        CoreProtectAPI CoreProtect = hook_CoreProtect.getCoreProtect();
        int Timespan = (int)timespan.getSingle(event).getMilliSeconds()/1000;
        List<String[]> list = CoreProtect.blockLookup(block.getSingle(event), Timespan);
        if (list != null) {
            try {
                return list.get(index.getSingle(event).intValue());
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
        }
        return null;
    }
}
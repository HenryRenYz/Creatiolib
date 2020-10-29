package com.henryrenyz.creatiolib.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import com.henryrenyz.creatiolib.plugins.API_CoreProtect;
import com.sun.istack.internal.Nullable;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

import java.util.List;

public class Expr_blockLookup extends SimpleExpression<List> {

    static {
        Skript.registerExpression(Expr_blockLookup.class, List.class, ExpressionType.COMBINED, "[the] core[ ]protect block lookup [result] of %block% [from] last %timespan%");
    }

    private Expression<Block> block;
    private Expression<Timespan> timespan;

    @Override
    public Class<? extends List> getReturnType() {
        return List.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        block = (Expression<Block>) exprs[0];
        timespan = (Expression<Timespan>) exprs[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "[the] core[ ]protect block lookup [result] of %block% [from] last %timespan%";
    }

    @Override
    @Nullable
    protected List[] get(Event event) {
        CoreProtectAPI CoreProtect = API_CoreProtect.getCoreProtect();
        int Timespan = (int)timespan.getSingle(event).getMilliSeconds()/1000;
        List<String[]> list = CoreProtect.blockLookup(block.getSingle(event), Timespan);
        if (list != null) {
            return new List[] {list};
        }
        return null;
    }
}
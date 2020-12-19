package com.henryrenyz.clib.skript.modules.RawText;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.henryrenyz.clib.modules.rawText.JsonManager;
import com.henryrenyz.clib.modules.rawText.Json;
import org.jetbrains.annotations.Nullable;;
import org.bukkit.event.Event;

@Name("Json Manager")
@Description({"Represents a json manager, can be add, remove, delete and reset"})
@Since("0.1.10")
public class Expr_JsonManager extends SimpleExpression<JsonManager> {
    static {
        Skript.registerExpression(Expr_JsonManager.class, JsonManager.class, ExpressionType.COMBINED, "[the] json manager %jsonmanager%");
    }

    private Expression<JsonManager> manager;

    @Override
    public Class<? extends JsonManager> getReturnType() {
        return JsonManager.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        manager = (Expression<JsonManager>) exprs[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "[the] json manager %jsonmanager%";
    }

    @Override
    @Nullable
    protected JsonManager[] get(Event event) {
        return new JsonManager[]{manager.getSingle(event)};
    }

    @Override
    public void change(Event event, Object[] delta, Changer.ChangeMode mode){
        JsonManager m = manager.getSingle(event);
        for (Object d : delta) {
            if (d instanceof Json) {
                if (mode == Changer.ChangeMode.ADD) {
                    m.add((Json) d);
                } else if (mode == Changer.ChangeMode.REMOVE) {
                    m.remove((Json) d);
                } else if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) {
                    m.clear();
                }
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.REMOVE || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(Json.class);
        }
        return null;
    }
}

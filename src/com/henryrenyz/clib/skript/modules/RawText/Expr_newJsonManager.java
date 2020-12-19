package com.henryrenyz.clib.skript.modules.RawText;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.henryrenyz.clib.modules.rawText.JsonManager;
import org.jetbrains.annotations.Nullable;;
import org.bukkit.event.Event;

@Name("New Json Manager")
@Description({"Create a new json manager that holds multiple json paragraph"})
@Since("0.1.10")
public class Expr_newJsonManager extends SimpleExpression<JsonManager> {
    static {
        Skript.registerExpression(Expr_newJsonManager.class, JsonManager.class, ExpressionType.COMBINED, "[a] new json manager");
    }


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
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "[a] new json manager";
    }

    @Override
    @Nullable
    protected JsonManager[] get(Event event) {
        return new JsonManager[]{new JsonManager()};
    }
}

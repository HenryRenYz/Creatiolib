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
import com.henryrenyz.clib.modules.rawText.Json;
import org.jetbrains.annotations.Nullable;;
import org.bukkit.event.Event;

@Name("Json Paragraph")
@Description({"Represents a json paragraph, can be add, remove, delete and reset", "The modification can only use string."})
@Since("0.1.10")
public class Expr_JsonParagraph extends SimpleExpression<Json> {
    static {
        Skript.registerExpression(Expr_JsonParagraph.class, Json.class, ExpressionType.COMBINED, "[the] json paragraph %jsonparagraph%");
    }

    private Expression<Json> para;

    @Override
    public Class<? extends Json> getReturnType() {
        return Json.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        para = (Expression<Json>) exprs[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "[the] json manager %jsonmanager%";
    }

    @Override
    @Nullable
    protected Json[] get(Event event) {
        return new Json[]{para.getSingle(event)};
    }

    @Override
    public void change(Event event, Object[] delta, Changer.ChangeMode mode){
        Json m = para.getSingle(event);
        for (Object d : delta) {
            if (d instanceof Json) {
                if (mode == Changer.ChangeMode.ADD) {
                    m.addExtra((Json) d);
                } else if (mode == Changer.ChangeMode.REMOVE) {
                    m.removeExtra((Json) d);
                } else if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) {
                    m.getExtra().clear();
                }
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.REMOVE || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(String.class);
        }
        return null;
    }
}

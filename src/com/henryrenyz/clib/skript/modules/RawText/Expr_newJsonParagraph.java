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
import com.henryrenyz.clib.modules.rawText.Json;
import org.jetbrains.annotations.Nullable;;
import org.bukkit.event.Event;

@Name("New Json Paragraph")
@Description({"Create a new json paragraph with a preset text or a raw text"})
@Since("0.1.10")
public class Expr_newJsonParagraph extends SimpleExpression<Json> {
    static {
        Skript.registerExpression(Expr_newJsonParagraph.class, Json.class, ExpressionType.COMBINED, "[a] new json paragraph [(1¦with text %-string%|2¦from raw text %-string%)]");
    }

    private Expression<String> text;
    private Integer mark;

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
        text = (Expression<String>) exprs[0];
        text = (Expression<String>) exprs[1];
        this.mark = parser.mark;
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "[a] new json paragraph [(1¦with text %-string%)|(2¦from raw [text] %-string%)]";
    }

    @Override
    @Nullable
    protected Json[] get(Event event) {
        if (text != null) {
            if (mark == 1) {
                return new Json[]{new Json.PlainText(text.getSingle(event))};
            } else {
                return new Json[]{Json.fromJSON(text.getSingle(event))};
            }
        } else {
            return new Json[]{new Json.PlainText()};
        }
    }
}

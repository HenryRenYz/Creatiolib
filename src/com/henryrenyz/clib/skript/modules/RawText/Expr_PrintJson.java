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
import com.henryrenyz.clib.modules.rawText.Json;
import org.jetbrains.annotations.Nullable;;
import org.bukkit.event.Event;

@Name("Print Json")
@Description({"Print the string form of json from manager or paragraph"})
@Since("0.1.10")
public class Expr_PrintJson extends SimpleExpression<String> {
    static {
        Skript.registerExpression(Expr_PrintJson.class, String.class, ExpressionType.COMBINED, "[the] json of (1¦manager %-jsonmanager%|2¦paragraph %-jsonparagraph%)");
    }

    private Expression<JsonManager> manager;
    private Expression<Json> paragraph;
    private Integer mark;

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
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        manager = (Expression<JsonManager>) exprs[0];
        paragraph = (Expression<Json>) exprs[1];
        mark = parser.mark;
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "[the] json of (1¦manager %-jsonmanager%|2¦paragraph %-jsonparagraph%)";
    }

    @Override
    @Nullable
    protected String[] get(Event event) {
        if (mark == 1) {
            return new String[]{manager.getSingle(event).getJSON()};
        } else {
            return new String[]{paragraph.getSingle(event).getJSON()};
        }
    }
}

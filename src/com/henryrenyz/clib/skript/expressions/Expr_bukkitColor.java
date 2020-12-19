package com.henryrenyz.clib.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.jetbrains.annotations.Nullable;;
import org.bukkit.Color;
import org.bukkit.event.Event;

@Name("Bukkit Color")
@Description({"Color that used in Bukkit API.", "%string% can be set to \"RGB\" or \"BRG\""})
@Examples({"generate \"REDSTONE\" public particles at player offset by 1,1,1 and extra 0 with data \"RGB\" color 123,123,123 and size 2"})
@Since("0.1.00")
public class Expr_bukkitColor extends SimpleExpression<Color> {

    static {
        Skript.registerExpression(Expr_bukkitColor.class, Color.class, ExpressionType.COMBINED, "[the] [bukkit] %string% color %number%,%number%,%number%");
    }

    private Expression<String> type;
    private Expression<Number> val1;
    private Expression<Number> val2;
    private Expression<Number> val3;

    @Override
    public Class<? extends Color> getReturnType() {
        return Color.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        type = (Expression<String>) exprs[0];
        val1 = (Expression<Number>) exprs[1];
        val2 = (Expression<Number>) exprs[2];
        val3 = (Expression<Number>) exprs[3];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "[the] [bukkit] (\"RGB\"|\"BGR\") color of %number%,[ ]%number%[,| and][ ]%number%";
    }

    @Override
    @Nullable
    protected Color[] get(Event event) {
        Integer Val1 = val1.getSingle(event).intValue();
        Integer Val2 = val2.getSingle(event).intValue();
        Integer Val3 = val3.getSingle(event).intValue();
        if ((Val1 <= 255) && (Val1 >= 0) && (Val2 <= 255) && (Val2 >= 0) && (Val3 <= 255) && (Val3 >= 0)) {
            if ((type.getSingle(event)).equalsIgnoreCase("RGB")) {
                return new Color[] {Color.fromRGB(Val1,Val2,Val3)};

            } else if ((type.getSingle(event)).equalsIgnoreCase("BGR")) {
                return new Color[] {Color.fromBGR(Val1,Val2,Val3)};

            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
package com.henryrenyz.creatiolib.skript.expressions;

import org.bukkit.Material;
import org.bukkit.event.Event;

import com.sun.istack.internal.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.inventory.ItemStack;

public class Expr_getBukkitMaterial extends SimpleExpression<Material> {

    static {
        Skript.registerExpression(Expr_getBukkitMaterial.class, Material.class, ExpressionType.COMBINED, "[the] [bukkit] material [type|name] of %itemstack%");
    }

    private Expression<ItemStack> item;

    @Override
    public Class<? extends Material> getReturnType() {
        return Material.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        item = (Expression<ItemStack>) exprs[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "[the] [bukkit] material [type|name] of %item%";
    }

    @Override
    @Nullable
    protected Material[] get(Event event) {
        Material material = item.getSingle(event).getType();
        if (material != null) {
            return new Material[] {material};
        }
        return null;
    }
}
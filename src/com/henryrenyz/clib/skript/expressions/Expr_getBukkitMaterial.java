package com.henryrenyz.clib.skript.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.Material;
import org.bukkit.event.Event;

import org.jetbrains.annotations.Nullable;;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.inventory.ItemStack;

@Name("Bukkit Material")
@Description({"The material(item type) used in Bukkit API, this is different to Skript's item"})
@Since("0.1.00")
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
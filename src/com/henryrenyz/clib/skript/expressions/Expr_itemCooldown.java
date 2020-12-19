package com.henryrenyz.clib.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.henryrenyz.clib.modules.packet.Packet;
import org.jetbrains.annotations.Nullable;;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

@Name("Item Cooldown")
@Description({"The cooldown when you used ender pearl and shield, not the weapon cooldown.", "Adding \"client-side\" will send a packet to player, but you can only use \"set\" in this case.", "World changes will remove client-side item cooldown effect."})
@Since("0.1.01")
public class Expr_itemCooldown extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(Expr_itemCooldown.class, Number.class, ExpressionType.COMBINED, "[the] %itemstack% [(1¦(client-side|client|visual))] [item] cooldown of %player%");
    }

    private Expression<ItemStack> item;
    private Expression<Player> player;
    private boolean clientside;

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        clientside = parser.mark == 1;
        item = (Expression<ItemStack>) exprs[0];
        player = (Expression<Player>) exprs[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "[the] %itemstack% [(1¦(client-side|client|visual))] [item] cooldown of %player%";
    }

    @Override
    @Nullable
    protected Number[] get(Event event) {
        return new Number[] {player.getSingle(event).getCooldown(item.getSingle(event).getType())};
    }

    @Override
    public void change(Event event, Object[] delta, Changer.ChangeMode mode){
        Integer tick = player.getSingle(event).getCooldown(item.getSingle(event).getType());
        if (mode == Changer.ChangeMode.SET) {
            if (clientside) {
                Packet.out.SetCooldown(item.getSingle(event).getType(), ((Number) delta[0]).intValue()).send(player.getSingle(event));
                return;
            } else {
                tick = ((Number) delta[0]).intValue();
                player.getSingle(event).setCooldown(item.getSingle(event).getType(), ((Number) delta[0]).intValue());
            }
        } else if (mode == Changer.ChangeMode.ADD) {
            tick += ((Number) delta[0]).intValue();
        } else if (mode == Changer.ChangeMode.REMOVE) {
            tick -= ((Number) delta[0]).intValue();
            tick = (tick >= 0) ? tick : 0;
        } else if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) {
            tick = 0;
        }
        player.getSingle(event).setCooldown(item.getSingle(event).getType(), tick);
    }

    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.REMOVE || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(Number.class);
        }
        return null;
    }
}

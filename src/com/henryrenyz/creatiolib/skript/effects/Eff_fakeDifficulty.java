package com.henryrenyz.creatiolib.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.henryrenyz.creatiolib.packets.EnumMinecraftParticle;
import com.henryrenyz.creatiolib.packets.Packets;
import com.sun.istack.internal.Nullable;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@Name("Fake Server Difficulty")
@Description({"Send a difficulty packet to player, this is client-side."})
@Since("0.1.01")
public class Eff_fakeDifficulty extends Effect {

    static {
        Skript.registerEffect(Eff_fakeDifficulty.class, "send fake difficulty %difficulty% to %players% [with lock %boolean%]");
    }

    private Expression<Difficulty> dif;
    private Expression<Player> player;
    private Expression<Boolean> isLocked;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.dif = (Expression<Difficulty>) expressions[0];
        this.player = (Expression<Player>) expressions[1];
        this.isLocked = (Expression<Boolean>) expressions[2];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "send fake difficulty %difficulty% to %players% [with lock %boolean%]";
    }

    @Override
    protected void execute(Event event) {
        Boolean locked = (isLocked != null) ? isLocked.getSingle(event) : false;
        Object packet = Packets.PlayOutServerDifficulty(dif.getSingle(event),locked);
        Packets.sendPacket(player.getAll(event),packet);
    }
}


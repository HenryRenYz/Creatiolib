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
import com.sun.istack.internal.Nullable;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.henryrenyz.creatiolib.packets.Packets;

@Name("Generate Packet Particle")
@Description({"Send a particle packet to player, this is client side.","setting particle name to something like \"REDSTONE,-0.01,-2,-0.01\" allows you to generate wierd particles."})
@Examples({"gen 120 \"flame\" packet particles at event-player for all players offset by 1, 2, 3 with speed 0.15 force true"
        ,"\tgenerate 55 \"Item_Crack\" packet particles at event-player for {_player::*} offset by 0.1,0.2,0.3 with speed 0.5 force false with data {_itemstack}"
        ,"\tspawn 55 \"minecraft:dust,-0.01,-0.01,-0.01,2\" packet particles at event-player for event-player offset by 0.1,0.2,0.1 with speed 123 force true"})
@Since("0.1.00")
public class Eff_genParticlePacket extends Effect {

    static {
        Skript.registerEffect(Eff_genParticlePacket.class, "(spawn|show|gen|generate) [%-number%] %string% packet particle[s] at %location% for %players% offset by %number%,%number%,%number% (with|and) (speed|extra) %number% force %boolean% [with data %-object%]");
    }

    private Expression<Number> number;
    private Expression<String> name;
    private Expression<Location> location;
    private Expression<Player> player;
    private Expression<Number> offsetX;
    private Expression<Number> offsetY;
    private Expression<Number> offsetZ;
    private Expression<Number> extra;
    private Expression<Boolean> force;
    private Expression<Object> data;
    private Integer Number;
    private String particle;
    private String[] List;
    private Object[] Data = {1f,1f,1f,1f};

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.number = (Expression<Number>) expressions[0];
        this.name = (Expression<String>) expressions[1];
        this.location = (Expression<Location>) expressions[2];
        this.player = (Expression<Player>) expressions[3];
        this.offsetX = (Expression<Number>) expressions[4];
        this.offsetY = (Expression<Number>) expressions[5];
        this.offsetZ = (Expression<Number>) expressions[6];
        this.extra = (Expression<Number>) expressions[7];
        this.force = (Expression<Boolean>) expressions[8];
        this.data = (Expression<Object>) expressions[9];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "(spawn|show|gen|generate) [%-number%] %string% packet particle[s] at %location% for %players% offset by %number%,%number%,%number% (with|and) (speed|extra) %number% force %boolean% [with data %-object%]";
    }

    @Override
    protected void execute(Event event) {
        if (number.getSingle(event) != null) {
            Number = number.getSingle(event).intValue();
        } else {
            Number = 1;
        }
        List = name.getSingle(event).split(",");
        if (data != null) { Data[0] = data.getSingle(event);}

        if (List[0].contains("minecraft:")) {
            particle = List[0].toUpperCase();
            particle = particle.replace("MINECRAFT:","").toUpperCase();
        } else {
            particle = EnumMinecraftParticle.valueOf(List[0].toUpperCase()).getKey().toUpperCase();
        }
        if (particle.equalsIgnoreCase("DUST")) {
            Data[0] = Float.parseFloat(List[1]);
            Data[1] = Float.parseFloat(List[2]);
            Data[2] = Float.parseFloat(List[3]);
            Data[3] = Float.parseFloat(List[4]);
        }

        Object Packet = Packets.PlayServerWorldParticles(location.getSingle(event), particle, offsetX.getSingle(event).floatValue(), offsetY.getSingle(event).floatValue(), offsetZ.getSingle(event).floatValue(), extra.getSingle(event).floatValue(), Number, force.getSingle(event), Data);
        Packets.sendPacket(player.getAll(event),Packet);
    }
}


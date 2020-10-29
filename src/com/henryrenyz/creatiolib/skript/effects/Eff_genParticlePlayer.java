package com.henryrenyz.creatiolib.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.sun.istack.internal.Nullable;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;


public class Eff_genParticlePlayer extends Effect {

    static {
        Skript.registerEffect(Eff_genParticlePlayer.class, "(spawn|show|gen|generate) [%-number%] %string% (player|private|client) particle[s] at %location% for %players% offset by %number%,%number%,%number% (with|and) (speed|extra) %number% [with data %-object% [and size %-number%]]");
    }

    private Expression<Number> number;
    private Expression<String> name;
    private Expression<Location> location;
    private Expression<Player> player;
    private Expression<Number> offsetX;
    private Expression<Number> offsetY;
    private Expression<Number> offsetZ;
    private Expression<Number> extra;
    private Expression<Object> data;
    private Expression<Number> size;
    private Integer Number;
    private Float Size = 1f;
    private Particle particle;
    private Object Data = null;

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
        this.data = (Expression<Object>) expressions[8];
        this.size = (Expression<Number>) expressions[9];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "(spawn|show|gen|generate) [%-number%] %string% (player|private|client) particle[s] at %location% for %players% offset by %number%,%number%,%number% (with|and) (speed|extra) %number% [with data %-object% [and size %-number%]]";
    }

    @Override
    protected void execute(Event event) {
        if (number != null) {
            Number = number.getSingle(event).intValue();
        } else {
            Number = 1;
        }
        if (Particle.valueOf(name.getSingle(event)) instanceof Particle) {
            particle = Particle.valueOf(name.getSingle(event));
        } else {
            particle = Particle.ASH;
        }

        //Redstone Particles
        if (particle == Particle.REDSTONE) {
            if ((data== null) || !(data.getSingle(event) instanceof Color)) {
                Data = new Particle.DustOptions(Color.RED,1);
            } else {
                if (size != null) {
                    Size = size.getSingle(event).floatValue();
                }
                Data = new Particle.DustOptions(((Color) data.getSingle(event)),Size);
            }

            //Item Crack Particles
        } else if (particle == Particle.ITEM_CRACK) {
            if ((data == null) || !(data.getSingle(event) instanceof ItemStack)) {
                Data = new ItemStack(Material.STONE);
            } else {
                Data = (ItemStack) data.getSingle(event);
            }

            //Block Crack, Block Dust and Falling Dust Particles
        } else if (particle == Particle.BLOCK_CRACK || particle == Particle.BLOCK_DUST || particle == Particle.FALLING_DUST) {
            if ((data == null) || !(data.getSingle(event) instanceof BlockData)) {
                Data = Material.STONE.createBlockData();
            } else {
                Data = (BlockData) data.getSingle(event);
            }
        }

        for (Player p : player.getAll(event)) {
            p.spawnParticle(particle, location.getSingle(event),Number,offsetX.getSingle(event).doubleValue(),offsetY.getSingle(event).doubleValue(),offsetZ.getSingle(event).doubleValue(),extra.getSingle(event).doubleValue(),Data);
        }
    }
}


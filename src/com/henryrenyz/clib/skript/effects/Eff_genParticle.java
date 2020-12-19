package com.henryrenyz.clib.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.henryrenyz.clib.modules.Message;
import com.henryrenyz.clib.modules.MessagePrefix;
import com.henryrenyz.clib.modules.packet.EnumMinecraftParticle;
import com.henryrenyz.clib.modules.packet.Packet;
import org.jetbrains.annotations.Nullable;;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.logging.Level;

@Name("Generate Particle")
@Description({"Generate particle using special mechanism."})
@Examples({"generate \"Flame\" public particles at event-location offset by 1,1,1 and speed 1"})
@Since("0.1.00")
public class Eff_genParticle extends Effect {

    static {
        Skript.registerEffect(Eff_genParticle.class, "(spawn|show|gen|generate) [%-number%] %string% " +
                "(1¦(world|public|server)|2¦(player|private|client)|3¦packet) particle[s] at %location% [for %-players%]" +
                " (offset by %-number%,%-number%,%-number%|with vector %-vector%) (with|and) (speed|extra) %number% [with" +
                " data %-object% [and size %-number%]]");
    }

    private Expression<Number> number;
    private Expression<String> name;
    private Expression<Location> location;
    private Expression<Player> player;
    private Expression<Number> offsetX;
    private Expression<Number> offsetY;
    private Expression<Number> offsetZ;
    private Expression<Vector> vector;
    private Expression<Number> extra;
    private Expression<Object> data;
    private Expression<Number> size;
    private Float[] offset = new Float[3];
    private Integer mark;
    private Integer Number;
    private Float Size;
    private Particle particle;
    private Object Data;
    private Float[] Delta = new Float[]{0f,0f,0f,0f};

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
        this.vector = (Expression<Vector>) expressions[7];
        this.extra = (Expression<Number>) expressions[8];
        this.data = (Expression<Object>) expressions[9];
        this.size = (Expression<Number>) expressions[10];
        this.mark = parser.mark;
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "(spawn|show|gen|generate) [%-number%] %string% " +
                "(1¦(world|public|server)|2¦(player|private|client)|3¦packet) particle[s] at %location% [for %-players%]" +
                " (offset by %-number%,%-number%,%-number%|with vector %-vector%) (with|and) (speed|extra) %number% [with" +
                " data %-object% [and size %-number%]]";
    }

    @Override
    protected void execute(Event event) {
        if (vector != null) {
            offset[0] = ((Double) vector.getSingle(event).getX()).floatValue();
            offset[1] = ((Double) vector.getSingle(event).getY()).floatValue();
            offset[2] = ((Double) vector.getSingle(event).getZ()).floatValue();
        } else {
            offset[0] = (offsetX.getSingle(event)).floatValue();
            offset[1] = (offsetY.getSingle(event)).floatValue();
            offset[2] = (offsetZ.getSingle(event)).floatValue();
        }

        Number = (number == null)? 1 : number.getSingle(event).intValue();
        Size = (size == null)? 1 : size.getSingle(event).floatValue();
        Data = (data == null)? null : data.getSingle(event);

        if (Number < 0) {
            Message.sendStatic(Level.SEVERE, MessagePrefix.SKRIPT, "MAIN.ERROR.NEGATIVE", new String[]{"Amount of Particle"});
            return;
        }

        if (Particle.valueOf(name.getSingle(event).toUpperCase()) instanceof Particle) {
            particle = Particle.valueOf(name.getSingle(event).toUpperCase());
        } else {
            Message.sendStatic(Level.SEVERE, MessagePrefix.SKRIPT, "MAIN.HOOK.SKRIPT.ERROR.GEN_PARTICLE.PARTICLE", new String[]{name.getSingle(event)});
            return;
        }

            //Redstone Particles
        if (particle == Particle.REDSTONE) {
            if (!(Data instanceof Color)) {
                if ((mark != 3) || ((mark == 3) && !(Data instanceof String))) {
                    invalidDataValue();
                    return;
                } else {
                    Delta = convertData(Data.toString().split(","));
                    if (Delta.length < 4) {
                        invalidDataValue();
                        return;
                    }
                }
            } else {
                if (mark != 3) {
                    Data = new Particle.DustOptions((Color) Data,Size);
                } else {
                    Delta = new Float[]{(float) ((Color) Data).getRed(), (float) ((Color) Data).getGreen(), (float) ((Color) Data).getBlue(), Size};
                }
            }

            //Item Crack Particles
        } else if (particle == Particle.ITEM_CRACK) {
            if (!(Data instanceof ItemStack)) {
                invalidDataValue();
                return;
            }
            //Block Crack, Block Dust and Falling Dust Particles
        } else if (particle == Particle.BLOCK_CRACK || particle == Particle.BLOCK_DUST || particle == Particle.FALLING_DUST) {
            if (!(Data instanceof BlockData)) {
                invalidDataValue();
                return;
            }
        }

        switch (mark) {
            case 1:
                location.getSingle(event).getWorld().spawnParticle(particle,location.getSingle(event),Number,offsetX.getSingle(event).doubleValue(),offsetY.getSingle(event).doubleValue(),offsetZ.getSingle(event).doubleValue(),extra.getSingle(event).doubleValue(),Data);
                break;
            case 2:
                if (player == null) {
                    Message.sendStatic(Level.SEVERE, MessagePrefix.SKRIPT, "MAIN.ERROR.IS_NULL", new String[]{"Target Players"});
                    return;
                }

                for (Player p : player.getAll(event)) {
                    p.spawnParticle(particle, location.getSingle(event),Number,offsetX.getSingle(event).doubleValue(),offsetY.getSingle(event).doubleValue(),offsetZ.getSingle(event).doubleValue(),extra.getSingle(event).doubleValue(),Data);
                }
                break;
            case 3:
                if (player == null) {
                    Message.sendStatic(Level.SEVERE, MessagePrefix.SKRIPT, "MAIN.ERROR.IS_NULL", new String[]{"Target Players"});
                    return;
                }

                String particles = EnumMinecraftParticle.valueOf(particle.name().toUpperCase()).getKey().toUpperCase();
                Packet.out.WorldParticles(location.getSingle(event), particles, offsetX.getSingle(event).floatValue(), offsetY.getSingle(event).floatValue(), offsetZ.getSingle(event).floatValue(), extra.getSingle(event).floatValue(), Number, true, Delta).send(player.getAll(event));
                break;
        }
    }

    private Float[] convertData(String[] ori) {
        Float[] Data = new Float[4];
        for (int a = 0; a < ori.length; a++) {
            Data[a] = Float.parseFloat(ori[a]);
        }
        return Data;
    }

    private void invalidDataValue() {
        Data = (Data != null) ? Data.toString() : "a null value";
        Message.sendStatic(Level.SEVERE, MessagePrefix.SKRIPT, "MAIN.HOOK.SKRIPT.ERROR.GEN_PARTICLE.DATA", new String[]{Data.toString()});
    }
}


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
import org.jetbrains.annotations.Nullable;;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@Name("Play Better Sound")
@Description({"Play sound at a location to players, this can play resource pack sound as well.", "%players% can be set to multiple players."})
@Examples({"playsound \"creatio.sound_1\" at event-player to all players with volume 0.5 and pitch 0.25"})
@Since("0.1.00")
public class Eff_playSound extends Effect {

    static {
        Skript.registerEffect(Eff_playSound.class, "playsound %string% at %location% [to %-players%] [(in|from) %-soundcategory%] [(and|with) volume %-number%] [(and|with) pitch %-number%]");
    }

    private Expression<String> sound;
    private Expression<Location> location;
    private Expression<Player> player;
    private Expression<SoundCategory> category;
    private Expression<Number> volume;
    private Expression<Number> pitch;
    private Float Volume;
    private Float Pitch;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.sound = (Expression<String>) expressions[0];
        this.location = (Expression<Location>) expressions[1];
        this.player = (Expression<Player>) expressions[2];
        this.category = (Expression<SoundCategory>) expressions[3];
        this.volume = (Expression<Number>) expressions[4];
        this.pitch = (Expression<Number>) expressions[5];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "playsound %string% at %location% [to %-players%] [(in|from) %-soundcategory%] [(and|with) volume %-number%] [(and|with) pitch %-number%]";
    }

    @Override
    protected void execute(Event event) {

        if (volume == null) {
            Volume = 1f;
        } else {
            Volume = volume.getSingle(event).floatValue();
        }

        if (pitch == null) {
            Pitch = 1f;
        } else {
            Pitch = pitch.getSingle(event).floatValue();
        }

        if (player != null && category == null) {
            if (category == null) {
                for (Player p : player.getAll(event)) {
                    p.playSound(location.getSingle(event),sound.getSingle(event),Volume,Pitch);
                }
            } else {
                for (Player p : player.getAll(event)) {
                    p.playSound(location.getSingle(event),sound.getSingle(event),category.getSingle(event),Volume,Pitch);
                }
            }
        } else if (player == null && category == null) {
            if (category == null) {
                location.getSingle(event).getWorld().playSound(location.getSingle(event), sound.getSingle(event), Volume, Pitch);
            } else {
                location.getSingle(event).getWorld().playSound(location.getSingle(event), sound.getSingle(event), category.getSingle(event), Volume, Pitch);
            }
        }
    }
}


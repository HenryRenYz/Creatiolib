package com.henryrenyz.clib.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.henryrenyz.clib.modules.Message;
import com.henryrenyz.clib.modules.MessagePrefix;
import com.henryrenyz.clib.modules.packet.EnumGameState;
import com.henryrenyz.clib.modules.packet.Packet;
import org.jetbrains.annotations.Nullable;;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.logging.Level;

@Name("Fake Server Difficulty")
@Description({"Send a difficulty packet to player, this is client-side."})
@Since("0.1.01")
public class Eff_chageGameState extends Effect {

    static {
        Skript.registerEffect(Eff_chageGameState.class, "change game state %string% of %players% to %number%");
    }

    private Expression<String> state;
    private Expression<Player> player;
    private Expression<Number> value;
    private EnumGameState State;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.state = (Expression<String>) expressions[0];
        this.player = (Expression<Player>) expressions[1];
        this.value = (Expression<Number>) expressions[2];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "change game state %string% of %players% to %number%";
    }

    @Override
    protected void execute(Event event) {
        State = EnumGameState.valueOf(state.getSingle(event));
        if (State == null) {
            Message.sendStatic(Level.SEVERE, MessagePrefix.SKRIPT, "MAIN.ERROR.INVALID", new String[]{state.getSingle(event)});
            return;
        }
        Packet.out.GameStateChange(State, value.getSingle(event).floatValue()).send(player.getAll(event));
    }
}


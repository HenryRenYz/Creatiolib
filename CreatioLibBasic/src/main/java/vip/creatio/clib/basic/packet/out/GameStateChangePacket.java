package vip.creatio.clib.basic.packet.out;

import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import vip.creatio.clib.basic.tools.Wrapper;
import vip.creatio.clib.basic.packet.Packet;
import net.minecraft.server.PacketPlayOutGameStateChange;

//Game Event in Official
public class GameStateChangePacket extends Packet<PacketPlayOutGameStateChange> {

    private static final Var<PacketPlayOutGameStateChange.a> STATE = Reflection.field(PacketPlayOutGameStateChange.class, "m");
    private static final Var<Float> VALUE = Reflection.field(PacketPlayOutGameStateChange.class, "n");

    private final GameState state;
    private final float value;

    GameStateChangePacket(PacketPlayOutGameStateChange nms) {
        super(nms);
        this.state = GameState.convert(STATE.get(nms));
        this.value = VALUE.getFloat(nms);
    }

    public GameStateChangePacket(GameState state, final float value) {
        super(new PacketPlayOutGameStateChange(state.unwrap(), value));
        this.state = state;
        this.value = value;
    }

    public GameStateChangePacket(int stateId, final float value) {
        super(new PacketPlayOutGameStateChange(GameState.toOriginal(stateId), value));
        this.state = GameState.convert(stateId);
        this.value = value;
    }

    @Override
    public String toString() {
        return "GameStateChange{state=" + state.name() + ",value=" + value + '}';
    }

    public GameState getState() {
        return this.state;
    }

    public float getValue() {
        return this.value;
    }

    public enum GameState implements Wrapper<PacketPlayOutGameStateChange.a /* GameState */> {
        NO_RESPAWN_BLOCK_AVAILABLE(0, PacketPlayOutGameStateChange.a),
        START_RAINING(1, PacketPlayOutGameStateChange.b),
        STOP_RAINING(2, PacketPlayOutGameStateChange.c),
        CHANGE_GAME_MODE(3, PacketPlayOutGameStateChange.d),
        WIN_GAME(4, PacketPlayOutGameStateChange.e),
        DEMO_EVENT(5, PacketPlayOutGameStateChange.f),
        ARROW_HIT_PLAYER(6, PacketPlayOutGameStateChange.g),
        RAIN_LEVEL_CHANGE(7, PacketPlayOutGameStateChange.h),
        THUNDER_LEVEL_CHANGE(8, PacketPlayOutGameStateChange.i),
        PUFFER_FISH_STING(9, PacketPlayOutGameStateChange.j),
        GUARDIAN_ELDER_EFFECT(10, PacketPlayOutGameStateChange.k),
        IMMEDIATE_RESPAWN(11, PacketPlayOutGameStateChange.l);

        private final int id;
        private final PacketPlayOutGameStateChange.a original;

        GameState(int id, PacketPlayOutGameStateChange.a ori) {
            this.original = ori;
            this.id = id;
        }

        public PacketPlayOutGameStateChange.a unwrap() {
            return this.original;
        }

        @Override
        public Class<? extends PacketPlayOutGameStateChange.a> wrappedClass() {
            return PacketPlayOutGameStateChange.a.class;
        }


        public static PacketPlayOutGameStateChange.a toOriginal(int id) {
            return convert(id).original;
        }

        public static GameState convert(int id) {
            for (GameState e : GameState.values()) {
                if (e.id == id) return e;
            }
            throw new EnumConstantNotPresentException(GameState.class, Integer.toString(id));
        }

        public static GameState convert(PacketPlayOutGameStateChange.a nms) {
            for (GameState e : GameState.values()) {
                if (e.original == nms) {
                    return e;
                }
            }
            throw new EnumConstantNotPresentException(GameState.class, nms.toString());
        }

        public int getId() {
            return id;
        }
    }
}

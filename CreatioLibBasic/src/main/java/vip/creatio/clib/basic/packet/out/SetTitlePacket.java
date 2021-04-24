package vip.creatio.clib.basic.packet.out;

import net.minecraft.server.IChatBaseComponent;
import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import vip.creatio.clib.basic.packet.Packet;
import vip.creatio.clib.basic.tools.Wrapper;
import vip.creatio.clib.basic.chat.Component;
import net.minecraft.server.PacketPlayOutTitle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SetTitlePacket extends Packet<PacketPlayOutTitle> {

    private static final Var<PacketPlayOutTitle.EnumTitleAction> ACTION = Reflection.field(PacketPlayOutTitle.class, "a");
    private static final Var<IChatBaseComponent> COMPONENT = Reflection.field(PacketPlayOutTitle.class, "b");
    private static final Var<Integer> FADE_IN = Reflection.field(PacketPlayOutTitle.class, "c");
    private static final Var<Integer> STAY = Reflection.field(PacketPlayOutTitle.class, "d");
    private static final Var<Integer> FADE_OUT = Reflection.field(PacketPlayOutTitle.class, "e");

    private final Action action;
    private final Component comp;
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    SetTitlePacket(PacketPlayOutTitle nms) {
        super(nms);
        this.action = Action.convert(ACTION.get(nms));
        this.comp = Component.wrap(COMPONENT.get(nms));
        this.fadeIn = FADE_IN.getInt(nms);
        this.stay = STAY.getInt(nms);
        this.fadeOut = FADE_OUT.getInt(nms);
    }

    public SetTitlePacket(Action action, @Nullable Component comp, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        super(new PacketPlayOutTitle(action.wrapped, comp == null ? null : comp.unwrap(), fadeInTicks, stayTicks, fadeOutTicks));
        this.action = action;
        this.comp = comp;
        this.fadeIn = fadeInTicks;
        this.stay = stayTicks;
        this.fadeOut = fadeOutTicks;
    }

    public SetTitlePacket(Action action, @NotNull Component comp) {
        this(action, comp, -1, -1, -1);
    }

    public SetTitlePacket(int fadeInTicks, int stayTicks, int fadeOutTicks) {
        this(Action.TIMES, null, fadeInTicks, stayTicks, fadeOutTicks);
    }

    public SetTitlePacket(Action action) {
        this(action, null, -1, -1, -1);
    }

    public Action getAction() {
        return action;
    }

    public Component getComp() {
        return comp;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public enum Action implements Wrapper<PacketPlayOutTitle.EnumTitleAction> {
        TITLE(PacketPlayOutTitle.EnumTitleAction.TITLE),
        SUBTITLE(PacketPlayOutTitle.EnumTitleAction.SUBTITLE),
        ACTIONBAR(PacketPlayOutTitle.EnumTitleAction.ACTIONBAR),
        TIMES(PacketPlayOutTitle.EnumTitleAction.TIMES),
        CLEAR(PacketPlayOutTitle.EnumTitleAction.CLEAR),
        RESET(PacketPlayOutTitle.EnumTitleAction.RESET);

        private final PacketPlayOutTitle.EnumTitleAction wrapped;

        Action(PacketPlayOutTitle.EnumTitleAction wrapped) {
            this.wrapped = wrapped;
        }

        public static Action convert(PacketPlayOutTitle.EnumTitleAction nms) {
            for (Action a : values()) {
                if (a.wrapped == nms) {
                    return a;
                }
            }
            throw new EnumConstantNotPresentException(Action.class, nms.name());
        }

        @Override
        public PacketPlayOutTitle.EnumTitleAction unwrap() {
            return wrapped;
        }

        @Override
        public Class<? extends PacketPlayOutTitle.EnumTitleAction> wrappedClass() {
            return PacketPlayOutTitle.EnumTitleAction.class;
        }
    }
}

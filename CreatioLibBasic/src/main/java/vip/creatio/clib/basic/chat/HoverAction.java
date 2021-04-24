package vip.creatio.clib.basic.chat;

import vip.creatio.clib.basic.tools.Wrapper;
import net.minecraft.server.ChatHoverable;
import net.minecraft.server.IChatBaseComponent;

import java.util.HashMap;
import java.util.Map;

public class HoverAction<T extends Wrapper<?>> implements Wrapper<ChatHoverable.EnumHoverAction<?>> {
    public static HoverAction<Component> SHOW_TEXT =
            new HoverAction<>(ChatHoverable.EnumHoverAction.SHOW_TEXT);
    public static HoverAction<HoverEvent.ItemStackInfo> SHOW_ITEM =
            new HoverAction<>(ChatHoverable.EnumHoverAction.SHOW_ITEM);
    public static HoverAction<HoverEvent.EntityTooltipInfo> SHOW_ENTITY =
            new HoverAction<>(ChatHoverable.EnumHoverAction.SHOW_ENTITY);

    private static final Map<String, HoverAction<?>> LOOKUP = new HashMap<>();

    static {
        LOOKUP.put("SHOW_TEXT", SHOW_TEXT);
        LOOKUP.put("SHOW_ITEM", SHOW_ITEM);
        LOOKUP.put("SHOW_ENTITY", SHOW_ENTITY);
    }

    private final ChatHoverable.EnumHoverAction<?> action;

    HoverAction(ChatHoverable.EnumHoverAction<?> action) {
        this.action = action;
    }

    static Wrapper<?> wrap(Object obj) {
        if (obj instanceof IChatBaseComponent) {
            return Component.wrap((IChatBaseComponent) obj);
        }
        if (obj instanceof ChatHoverable.c /* ItemStackInfo */) {
            return new HoverEvent.ItemStackInfo((ChatHoverable.c) obj);
        }
        if (obj instanceof ChatHoverable.b /* EntityTooltipInfo */) {
            return new HoverEvent.EntityTooltipInfo((ChatHoverable.b) obj);
        }
        throw new RuntimeException("Invalid Object: " + obj);
    }

    public boolean isAllowedFromServer() {
        return action.a();
    }

    public String getName() {
        return action.b();
    }

    public static HoverAction<?> getByName(String name) {
        return LOOKUP.get(name.toUpperCase());
    }

    @SuppressWarnings("unchecked")
    T cast(Object obj) {
        return (T) obj;
    }

    @Override
    public String toString() {
        return action.toString();
    }

    @Override
    public ChatHoverable.EnumHoverAction<?> unwrap() {
        return action;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends ChatHoverable.EnumHoverAction<?>> wrappedClass() {
        return (Class) ChatHoverable.EnumHoverAction.class;
    }
}

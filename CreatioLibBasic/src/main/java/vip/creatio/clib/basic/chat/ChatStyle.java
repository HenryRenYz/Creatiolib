package vip.creatio.clib.basic.chat;

import vip.creatio.clib.basic.util.BukkitUtil;
import vip.creatio.clib.basic.tools.Wrapper;
import net.minecraft.server.ChatClickable;
import net.minecraft.server.ChatHoverable;
import net.minecraft.server.ChatModifier;
import net.minecraft.server.EnumChatFormat;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Wrapper class of ChatModifier(Style)
 * The difference is that ChatModifier is immutable,
 * and ChatStyle is mutable.
 */
public class ChatStyle implements Wrapper<ChatModifier> {

    public static final ChatModifier EMPTY = ChatModifier.a;
    public static final NamespacedKey DEFAULT_FONT = NamespacedKey.minecraft("default");

    private ChatModifier modifier;

    ChatStyle(ChatModifier modifier) {
        this.modifier = modifier;
    }

    @Nullable ChatColor getColor() {
        return new ChatColor(modifier.getColor());
    }

    public boolean isBold() {
        return modifier.isBold();
    }

    public boolean isItalic() {
        return modifier.isItalic();
    }

    public boolean isStrikethrough() {
        return modifier.isStrikethrough();
    }

    public boolean isUnderlined() {
        return modifier.isUnderlined();
    }

    public boolean isObfuscated() {
        return modifier.isRandom();
    }

    public boolean isEmpty() {
        return modifier.g /* isEmpty */ ();
    }

    public @Nullable ClickEvent getClickEvent() {
        ChatClickable clickable = modifier.getClickEvent();
        if (clickable == null) return null;
        return new ClickEvent(modifier.getClickEvent());
    }

    public @Nullable HoverEvent getHoverEvent() {
        ChatHoverable hoverable = modifier.getHoverEvent();
        return hoverable == null ? null : new HoverEvent(hoverable);
    }

    public @Nullable String getInsertion() {
        return modifier.getInsertion();
    }

    public NamespacedKey getFont() {
        return BukkitUtil.toBukkit(modifier.getFont());
    }

    public ChatStyle withColor(@Nullable ChatColor color) {
        this.modifier = modifier.setColor(color == null ? null : color.unwrap());
        return this;
    }

    public ChatStyle withColor(@Nullable ChatFormat legacyColor) {
        this.modifier = modifier.setColor(legacyColor == null ? null : legacyColor.unwrap());
        return this;
    }

    public ChatStyle withBold(@Nullable Boolean bool) {
        this.modifier = modifier.setBold(bool);
        return this;
    }

    public ChatStyle withItalic(@Nullable Boolean bool) {
        this.modifier = modifier.setItalic(bool);
        return this;
    }

    public ChatStyle withStrikethrough(@Nullable Boolean bool) {
        this.modifier = modifier.setStrikethrough(bool);
        return this;
    }

    public ChatStyle withUnderline(@Nullable Boolean bool) {
        this.modifier = modifier.setUnderline(bool);
        return this;
    }

    public ChatStyle withObfuscated(@Nullable Boolean bool) {
        this.modifier = modifier.setRandom(bool);
        return this;
    }

    public ChatStyle withClickEvent(@Nullable ClickEvent event) {
        this.modifier = modifier.setChatClickable(event == null ? null : event.unwrap());
        return this;
    }

    public ChatStyle withHoverEvent(@Nullable HoverEvent event) {
        this.modifier = modifier.setChatHoverable(event == null ? null : event.unwrap());
        return this;
    }

    public ChatStyle withInsertion(@Nullable String insertion) {
        this.modifier = modifier.setInsertion(insertion);
        return this;
    }

    public ChatStyle applyFormat(@NotNull ChatFormat legacy) {
        this.modifier = modifier.b(legacy.unwrap());
        return this;
    }

    public ChatStyle applyFormats(ChatFormat... legacies) {
        EnumChatFormat[] unwrapped = new EnumChatFormat[legacies.length];
        for (int i = 0; i < legacies.length; i++) {
            unwrapped[i] = legacies[i].unwrap();
        }
        this.modifier = modifier.a(unwrapped);
        return this;
    }

    public ChatStyle applyTo(@NotNull ChatStyle style) {
        this.modifier = modifier.setChatModifier(style.modifier);
        return this;
    }

    @Override
    public String toString() {
        return modifier.toString();
    }

    @Override
    public int hashCode() {
        return modifier.hashCode() * 31;
    }

    @Override
    public ChatModifier unwrap() {
        return modifier;
    }

    @Override
    public Class<? extends ChatModifier> wrappedClass() {
        return ChatModifier.class;
    }
}

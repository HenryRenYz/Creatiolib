package vip.creatio.clib.basic.chat;

import com.google.common.collect.Streams;
import vip.creatio.clib.basic.tools.Wrapper;
import net.minecraft.server.*;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Component implements Iterable<Component>, Message, Wrapper<IChatBaseComponent> {

    final IChatMutableComponent component;
    
    Component(IChatMutableComponent component) {
        this.component = component;
    }

    public static Component create() {
        return TextComponent.EMPTY.plainCopy();
    }

    public static Component of(String msg) {
        return new TextComponent(msg);
    }

    public static Component of(int val) {
        return new TextComponent(Integer.toString(val));
    }

    public static Component of(double val) {
        return new TextComponent(Double.toString(val));
    }

    public static Component of(float val) {
        return new TextComponent(Float.toString(val));
    }

    public static Component of(long val) {
        return new TextComponent(Long.toString(val));
    }

    public Component setStyle(ChatStyle style) {
        component.setChatModifier(style.unwrap());
        return this;
    }

    public ChatStyle getStyle() {
        return new ChatStyle(component.getChatModifier());
    }

    public Component append(String content) {
        return append(new TextComponent(content));
    }

    public String getContents() {
        return component.getText();
    }

    public List<Component> getSiblings() {
        return component.getSiblings().stream().map(Component::wrap).collect(Collectors.toList());
    }

    public Component plainCopy() {
        return this;
    }

    public Component copy() {
        IChatMutableComponent raw = component.mutableCopy();
        return Component.wrap(raw);
    }

    public Component append(Component component) {
        this.component.addSibling(component.unwrap());
        return this;
    }

    public Component append(Component component, Component... components) {
        append(component);
        for (Component c : components) {
            this.component.addSibling(c.unwrap());
        }
        return this;
    }

    public Component withStyle(UnaryOperator<ChatStyle> op) {
        setStyle(op.apply(getStyle()));
        return this;
    }

    public Component withStyle(ChatStyle style) {
        setStyle(style.applyTo(style));
        return this;
    }

    public Component withStyle(ChatFormat... format) {
        setStyle(getStyle().applyFormats(format));
        return this;
    }

    public Component withStyle(ChatFormat format) {
        setStyle(getStyle().applyFormat(format));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Component) {
            return unwrap().equals(((Component) o).unwrap());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return unwrap().hashCode() * 31;
    }

    @Override
    public String toString() {
        return unwrap().toString();
    }

    @Override
    public IChatMutableComponent unwrap() {
        return component;
    }

    @Override
    public Class<? extends IChatBaseComponent> wrappedClass() {
        return component.getClass();
    }

    //############## Mirror of ChatStyle ################//

    public @Nullable ChatColor getColor() {
        return getStyle().getColor();
    }

    public boolean isBold() {
        return getStyle().isBold();
    }

    public boolean isItalic() {
        return getStyle().isItalic();
    }

    public boolean isStrikethrough() {
        return getStyle().isStrikethrough();
    }

    public boolean isUnderlined() {
        return getStyle().isUnderlined();
    }

    public boolean isObfuscated() {
        return getStyle().isObfuscated();
    }

    public boolean isEmpty() {
        return getStyle().isEmpty();
    }

    public @Nullable ClickEvent getClickEvent() {
        return getStyle().getClickEvent();
    }

    public @Nullable HoverEvent getHoverEvent() {
        return getStyle().getHoverEvent();
    }

    public @Nullable String getInsertion() {
        return getStyle().getInsertion();
    }

    public NamespacedKey getFont() {
        return getStyle().getFont();
    }

    public Component withColor(@Nullable ChatColor color) {
        return withStyle(getStyle().withColor(color));
    }

    public Component withColor(@Nullable ChatFormat legacyColor) {
        return withStyle(getStyle().withColor(legacyColor));
    }

    public Component withColor(@Nullable String format) {
        return withStyle(getStyle().withColor(ChatColor.parseColor(format)));
    }

    public Component withColor(int rgb) {
        return withStyle(getStyle().withColor(ChatColor.fromRgb(rgb)));
    }

    public Component withColor(@Nullable Color color) {
        return withColor(color.asRGB());
    }

    public Component withBold(@Nullable Boolean bool) {
        return withStyle(getStyle().withBold(bool));
    }

    public Component withStrikethrough(@Nullable Boolean bool) {
        return withStyle(getStyle().withStrikethrough(bool));
    }

    public Component withUnderline(@Nullable Boolean bool) {
        return withStyle(getStyle().withUnderline(bool));
    }

    public Component withObfuscated(@Nullable Boolean bool) {
        return withStyle(getStyle().withObfuscated(bool));
    }

    public Component withClickEvent(@Nullable ClickEvent event) {
        return withStyle(getStyle().withClickEvent(event));
    }
    public Component onClick(@Nullable ClickEvent event) {
        return withClickEvent(event);
    }

    public Component withHoverEvent(@Nullable HoverEvent event) {
        return withStyle(getStyle().withHoverEvent(event));
    }
    public Component onHover(@Nullable HoverEvent event) {
        return withHoverEvent(event);
    }

    public Component withInsertion(@Nullable String insertion) {
        return withStyle(getStyle().withInsertion(insertion));
    }

    //###################################################//

    //################# IChatBaseComponent ##############//

    public Stream<Component> stream() {
        return Streams.concat(Stream.of(this), this.getSiblings().stream().flatMap(Component::stream));
    }

    @Override
    public @NotNull Iterator<Component> iterator() {
        return this.stream().iterator();
    }

    public String getString() {
        return unwrap().getString();
    }

    public String getString(int index) {
        return unwrap().a(index);
    }

    public String toSingleLine() {
        StringBuilder sb = new StringBuilder();
        appendSingleLine(sb);
        return sb.toString();
    }

    protected void appendSingleLine(StringBuilder sb) {
        if (isEmpty()) return;
        if (isBold()) sb.append("§l");
        if (isItalic()) sb.append("§o");
        if (isStrikethrough()) sb.append("§m");
        if (isUnderlined()) sb.append("§n");
        if (isObfuscated()) sb.append("§k");
        ChatColor color = getColor();
        if (color != null) sb.append('§').append(ChatFormat.getNearest(color.getRGB()).getCharacter());
        sb.append(getContents());
        getSiblings().forEach(c -> appendSingleLine(sb));
    }

    public static Component wrap(@Nullable IChatBaseComponent raw) {
        if (raw instanceof ChatBaseComponent) {
            if (raw instanceof ChatComponentText) {
                return new TextComponent((ChatComponentText) raw);
            }
            if (raw instanceof ChatComponentSelector) {
                return new SelectorComponent((ChatComponentSelector) raw);
            }
            if (raw instanceof ChatComponentScore) {
                return new ScoreComponent((ChatComponentScore) raw);
            }
            if (raw instanceof ChatComponentNBT) {
                if (raw instanceof ChatComponentNBT.a) {
                    return new NbtComponent.Block((ChatComponentNBT.a) raw);
                }
                if (raw instanceof ChatComponentNBT.b) {
                    return new NbtComponent.Entity((ChatComponentNBT.b) raw);
                }
                if (raw instanceof ChatComponentNBT.c) {
                    return new NbtComponent.Storage((ChatComponentNBT.c) raw);
                }
            }
            if (raw instanceof ChatMessage) {
                return new TranslatableComponent((ChatMessage) raw);
            }
            return new Component((IChatMutableComponent) raw);
        }
        return null;
    }

    public String toJson() {
        return toJson(this);
    }

    public static String toJson(Component component) {
        return IChatBaseComponent.ChatSerializer.a(component.unwrap());
    }

    @Nullable
    public static Component fromJson(String string) {
        return wrap(IChatBaseComponent.ChatSerializer.a(string));
    }

    //###################################################//

    //##################### String ######################//

    public int length() {
        return getString().length();
    }

    public char charAt(int index) {
        return getString().charAt(index);
    }

    public Component replace(CharSequence from, CharSequence to) {
        Component copy = create();
        for (Component comp : this) {
            if (comp instanceof TextComponent) {
                copy.append(comp.getContents().replace(from, to));
            } else {
                copy.append(comp.copy());
            }
        }
        return copy;
    }

    public Component replace(char from, char to) {
        Component copy = create();
        for (Component comp : this) {
            if (comp instanceof TextComponent) {
                copy.append(comp.getContents().replace(from, to));
            } else {
                copy.append(comp.copy());
            }
        }
        return copy;
    }

    public Component replaceAll(String fromRegex, String toRegex) {
        return replaceAll(Pattern.compile(fromRegex), toRegex);
    }

    public Component replaceAll(Pattern pattern, String toRegex) {
        Component copy = create();
        for (Component comp : this) {
            if (comp instanceof TextComponent) {
                copy.append(pattern.matcher(comp.getContents()).replaceAll(toRegex));
            } else {
                copy.append(comp.copy());
            }
        }
        return copy;
    }

    public Component replaceAll(String fromRegex, Function<Matcher, String> replacer) {
        Component copy = create();
        Pattern regex = Pattern.compile(fromRegex);
        for (Component comp : this) {
            if (comp instanceof TextComponent) {
                copy.append(replacer.apply(regex.matcher(comp.getContents())));
            } else {
                copy.append(comp.copy());
            }
        }
        return copy;
    }

    //###################################################//
}

package vip.creatio.clib.basic.chat;

import vip.creatio.clib.basic.tools.Wrapper;
import net.minecraft.server.EnumChatFormat;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum ChatFormat implements Wrapper<EnumChatFormat> {
    BLACK,
    DARK_BLUE,
    DARK_GREEN,
    DARK_AQUA,
    DARK_RED,
    DARK_PURPLE,
    GOLD,
    GRAY,
    DARK_GRAY,
    BLUE,
    GREEN,
    AQUA,
    RED,
    LIGHT_PURPLE,
    YELLOW,
    WHITE,
    OBFUSCATED,
    BOLD,
    STRIKETHROUGH,
    UNDERLINE,
    ITALIC,
    RESET;

    private static final Map<EnumChatFormat, ChatFormat> PAIR =
            Arrays.stream(values()).collect(Collectors.toMap(c -> c.original, c -> c));
    private final EnumChatFormat original;

    ChatFormat() {
        this.original = EnumChatFormat.b /* getByName */ (this.name());
    }

    public int getId() {
        return original.b();
    }

    public boolean isFormat() {
        return original.isFormat();
    }

    public boolean isColor() {
        return original.d();
    }

    public char getCharacter() {
        return original.character;
    }

    public @Nullable Integer getColor() {
        return original.e();
    }

    public String getName() {
        return original.f();
    }

    @Override
    public String toString() {
        return original.toString();
    }

    public static String stripFormatting(@Nullable String name) {
        return EnumChatFormat.a(name);
    }

    public static @Nullable ChatFormat getByName(String name) {
        return name == null ? null : PAIR.get(EnumChatFormat.b(name));
    }

    public static ChatFormat getById(int id) {
        return PAIR.get(EnumChatFormat.a(id));
    }


    @Override
    public EnumChatFormat unwrap() {
        return original;
    }

    @Override
    public Class<? extends EnumChatFormat> wrappedClass() {
        return EnumChatFormat.class;
    }


    public static ChatFormat getNearest(int rgb) {
        ChatFormat smallest = RESET;
        int     c,
                c1 = Integer.MAX_VALUE;

        for (ChatFormat clr : values()) {
            if (clr.getColor() != null && clr.getCharacter() != 'r') {
                int color = clr.getColor();
                c = ((color >> 16) & 0xFF) + ((color >> 8) & 0xFF) + ((color) & 0xFF);
                if (c < c1) {
                    smallest = clr;
                    c1 = c;
                }
            }
        }
        return smallest;
    }
}

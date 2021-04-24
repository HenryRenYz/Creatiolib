package vip.creatio.clib.basic.chat;

import vip.creatio.clib.basic.tools.Wrapper;
import vip.creatio.accessor.Reflection;
import vip.creatio.accessor.Var;
import net.minecraft.server.ChatHexColor;
import org.bukkit.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatColor implements Wrapper<ChatHexColor> {

    private final ChatHexColor color;

    ChatColor(ChatHexColor color) {
        this.color = color;
    }

    public static ChatColor parseColor(String ori) {
        return new ChatColor(ChatHexColor.a /* parseColor */ (ori));
    }

    public static ChatColor fromRgb(int rgb) {
        return new ChatColor(ChatHexColor.a /* fromRgb */ (rgb));
    }

    public static ChatColor fromLegacyFormat(ChatFormat format) {
        return new ChatColor(ChatHexColor.a(format.unwrap()));
    }

    public String serialize() {
        return color.b /* serialize */ ();
    }

    @Override
    public String toString() {
        return color.toString();
    }

    @Override
    public int hashCode() {
        return color.hashCode() * 31;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ChatColor) {
            return color.equals(((ChatColor) o).color);
        }
        return false;
    }

    @Override
    public ChatHexColor unwrap() {
        return color;
    }

    @Override
    public Class<? extends ChatHexColor> wrappedClass() {
        return ChatHexColor.class;
    }

    private static final Var<Integer> RGB = Reflection.field(ChatHexColor.class, "rgb");
    public int getRGB() {
        return RGB.get();
    }

    public Color getColor() {
        return Color.fromRGB(getRGB());
    }


    //Convert color in json to String type, e.g 123456 --> §x§1§2§3§4§5§6
    public static String hexToColorCode(String hexColor) {
        if (hexColor.startsWith("#")) hexColor = hexColor.substring(1);
        if (hexColor.length() != 6) throw new StringIndexOutOfBoundsException("hex color string should have 6 chars!");
        StringBuilder b = new StringBuilder("§x");
        for (char c : hexColor.toCharArray()) {
            b.append("§").append(c);
        }
        return b.toString();
    }

    private static final Pattern hexCode = Pattern.compile("§x(§[0-9a-fA-F]){6}");
    public static String colorCodeToHex(String ori) {
        Matcher mt = hexCode.matcher(ori);
        if (mt.find()) {
            return '#' + ori.substring(3, 15).replaceAll("§", "");
        }
        throw new RuntimeException("No such color: " + ori);
    }

    public static String wipeHex(String msg) {
        Matcher mt = hexCode.matcher(msg);
        while (mt.find()) {
            msg = msg.replaceAll(mt.group(0), ChatColor.colorCodeToHex(mt.group(0)));
        }
        return msg;
    }
}

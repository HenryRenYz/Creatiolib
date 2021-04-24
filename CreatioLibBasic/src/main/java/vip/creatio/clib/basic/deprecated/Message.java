package vip.creatio.clib.basic.deprecated;

import vip.creatio.clib.basic.config.Configs;
import vip.creatio.clib.basic.packet.out.ChatPacket;
import vip.creatio.clib.basic.packet.out.SetTitlePacket;
import vip.creatio.common.StringUtil;
import vip.creatio.clib.basic.chat.ChatColor;
import vip.creatio.clib.basic.chat.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"UnusedReturnTypes", "unused"})
public final class Message {

    public static final Prefix MAIN_PREFIX = new Prefix("MAIN.FORMAT.PREFIX");

    static FileConfiguration language;
    private static final Pattern hex = Pattern.compile("\\{(#[0-9a-fA-F]{6}?)}");
    private static final Pattern var = Pattern.compile("%([0-9]{1,2}?)%");
    private static final Component ENDL = Component.of("\n");
    private static String WARN = "&e";
    private static String ERROR = "&c";
    private static String NORMAL = "&7";
    private static String SUCCESS = "&a";
    private static String HIGHLIGHT = "&6";

    //No default constructor
    private Message() {}

    public static void initialization(JavaPlugin plugin, String lang) {
        Configs.updateConfig(plugin, "lang/" + lang + ".yml", -1);
        language = Configs.load(plugin, "lang/" + lang + ".yml");
        WARN = charReplace(language.getString("MAIN.FORMAT.WARN"));
        ERROR = charReplace(language.getString("MAIN.FORMAT.ERROR"));
        NORMAL = charReplace(language.getString("MAIN.FORMAT.NORMAL"));
        SUCCESS = charReplace(language.getString("MAIN.FORMAT.SUCCESS"));
        HIGHLIGHT = charReplace(language.getString("MAIN.FORMAT.HIGHLIGHT"));
    }



    public static void debug(@NotNull String msg) {
        debug(Level.SEVERE, msg);
    }

    public static void debug(@NotNull Level level, @NotNull String msg) {
        Bukkit.getLogger().log(level, charReplace("&6[&c&lCreatioLib - Debug&6] " + msg));
    }

    public static void internal(@NotNull String msg) {
        internal(Level.SEVERE, msg);
    }

    public static void internal(@NotNull Level level, @NotNull String msg) {
        Bukkit.getLogger().log(level, charReplace("&6[&e&lCreatioLib &8- &c&lInternal&6] " + msg));
    }



    public static void send(@Nullable Player player, String... message) {
        if (player == null) {
            send(Level.INFO, message);
            return;
        }
        Component component = Component.create();
        for (int i = 0; i < message.length - 1; i++) {
            component.append(message[i] + "\n");
        }
        component.append(message[message.length - 1]);
        new ChatPacket(component).send(player);
    }
    public static void send(@Nullable Player player, Component... components) {
        if (player == null) {
            send(Level.INFO, components);
            return;
        }
        Component comp = Component.create();
        for (int i = 0; i < components.length - 1; i++) {
            comp.append(components[i], ENDL);
        }
        comp.append(components[components.length - 1]);
        new ChatPacket(comp).send(player);
    }



    public static void send(@NotNull Level level, String... message) {
        for (String s : message) {
            Bukkit.getLogger().log(level, ChatColor.wipeHex(charReplace(s)));
        }
    }
    public static void send(@NotNull Level level, Component... components) {
        for (Component c : components) {
            Bukkit.getLogger().log(level, charReplace(c.getContents()));
        }
    }



    public static void sendBar(@NotNull Player player, @NotNull String content) {
        sendBar(player, Component.of(content));
    }

    public static void sendBar(@NotNull Player player, @NotNull Component comp) {
        new SetTitlePacket(SetTitlePacket.Action.ACTIONBAR, comp).send(player);
    }

    public static void sendTitle(@NotNull Player player, @NotNull String content) {
        sendTitle(player, Component.of(content));
    }

    public static void sendTitle(@NotNull Player player, @NotNull Component comp) {
        new SetTitlePacket(SetTitlePacket.Action.TITLE, comp).send(player);
    }

    public static void sendSubTitle(@NotNull Player player, @NotNull String content) {
        sendSubTitle(player, Component.of(content));
    }

    public static void sendSubTitle(@NotNull Player player, @NotNull Component comp) {
        new SetTitlePacket(SetTitlePacket.Action.SUBTITLE, comp).send(player);
    }




    public static String getStatic(@NotNull String path) {
        return language.getString(path);
    }


    public static void sendStatic(@NotNull String path, @Nullable Player player) {
        sendStatic0(Level.INFO, MAIN_PREFIX, path, player);
    }

    public static void sendStatic(@NotNull String path, @Nullable Player player, String... vars) {
        sendStatic0(Level.INFO, MAIN_PREFIX, path, player, vars);
    }

    public static void sendStatic(@Nullable Prefix prefix, @NotNull String path, @Nullable Player player) {
        sendStatic0(Level.INFO, prefix, path, player);
    }

    public static void sendStatic(@Nullable Prefix prefix, @NotNull String path, @Nullable Player player, String... vars) {
        sendStatic0(Level.INFO, prefix, path, player, vars);
    }


    public static void sendStatic(@NotNull Level level, @NotNull String path) {
        sendStatic0(level, MAIN_PREFIX, path, null);
    }

    public static void sendStatic(@NotNull Level level, @NotNull String path, String... vars) {
        sendStatic0(level, MAIN_PREFIX, path, null, vars);
    }

    public static void sendStatic(@NotNull Level level, @Nullable Prefix prefix, @NotNull String path) {
        sendStatic0(level, prefix, path, null);
    }

    public static void sendStatic(@NotNull Level level, @Nullable Prefix prefix, @NotNull String path, String... vars) {
        sendStatic0(level, prefix, path, null, vars);
    }

    private static void sendStatic0(@NotNull Level level, @Nullable Prefix prefix, @NotNull String path, @Nullable Player player, String... vars) {
        String[] msg = fromPath(prefix, path, vars);
        if (player == null) send(level, msg);
        else send(player, msg);
    }

    public static String[] fromPath(@NotNull String path, @Nullable String... vars) {
        return fromPath(MAIN_PREFIX, path, vars);
    }

    public static String[] fromPath(@Nullable Prefix prefix, @NotNull String path) {
        return fromPath(prefix, path, (String) null);
    }

    public static String[] fromPath(@NotNull String path) {
        return fromPath(MAIN_PREFIX, path, (String) null);
    }

    public static String[] fromPath(@Nullable Prefix prefix, @NotNull String path, @Nullable String... vars) {
        Prefix pf = prefix == null ? MAIN_PREFIX : prefix;
        Object var = language.get(path);
        var = (var == null) ? path : var;
        if (var instanceof List) {
            List<String> temp = new ArrayList<>();
            for (String s : (List<String>) var) {
                temp.add(StringUtil.backTrim(varReplace(pf.getPrefix(), s, vars)));
            }
            return temp.toArray(new String[0]);
        } else {
            return new String[]{StringUtil.backTrim(varReplace(pf.getPrefix(), var.toString(), vars))};
        }
    }

    public static String charReplace(String message) {
        if (message == null) return "/null/";

        message = message.replace('&', '§');
        message = StringUtil.replaceAll(message, "/§", "&");
        message = StringUtil.replaceAll(message, "%w%", WARN);
        message = StringUtil.replaceAll(message, "%e%", ERROR);
        message = StringUtil.replaceAll(message, "%n%", NORMAL);
        message = StringUtil.replaceAll(message, "%s%", SUCCESS);
        message = StringUtil.replaceAll(message, "%h%", HIGHLIGHT);

        Matcher mt = hex.matcher(message);
        while (mt.find()) {
            message = StringUtil.replaceAll(
                    message,
                    StringUtil.replaceAll(mt.group(0),"\\{", "\\\\{"),
                    ChatColor.hexToColorCode(mt.group(1))
            );
        }
        return message;
    }

    private static String varReplace(@NotNull String prefix, String message, String... vars) {
        message = charReplace(StringUtil.replaceAll(message, "%prefix%", prefix));
        Matcher mt = var.matcher(message);
        while(mt.find()) {
            int i = Integer.parseInt(mt.group(1));
            if (vars.length > i) message = StringUtil.replaceAll(message,mt.group(0), vars[i]);
        }
        return message;
    }

    public static class Prefix {
        private String prefix;
        private final String path;

        public Prefix(String path) {
            this.path = path;
        }

        public String getPrefix() {
            if (prefix == null)
                prefix = charReplace(language.getString(path));
            return prefix;
        }

        public String getPath() {
            return path;
        }
    }
}

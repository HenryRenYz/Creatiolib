package com.henryrenyz.clib.modules;

import com.henryrenyz.clib.modules.packet.EnumChatMessageType;
import com.henryrenyz.clib.modules.packet.Packet;
import com.henryrenyz.clib.modules.rawText.Json;
import com.henryrenyz.clib.modules.rawText.TextColor;
import com.henryrenyz.clib.modules.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.henryrenyz.clib.Creatio.getInstance;

@SuppressWarnings("unchecked")
public class Message {

    static FileConfiguration language;
    private static final Pattern hex = Pattern.compile("\\{(#[0-9a-fA-F]{6}?)}");
    private static final Pattern var = Pattern.compile("%([0-9]{1,2}?)%");
    private static String WARN = "&e";
    private static String ERROR = "&c";
    private static String NORMAL = "&7";
    private static String SUCCESS = "&a";

    //No default constructor
    private Message() {}

    public static void initialization(JavaPlugin plugin, String lang) {
        Config.updateConfig(plugin, "lang/" + lang + ".yml", -1);
        language = Config.load(plugin, "lang/" + lang + ".yml");
        WARN = charReplace(language.getString("MAIN.FORMAT.WARN"));
        ERROR = charReplace(language.getString("MAIN.FORMAT.ERROR"));
        NORMAL = charReplace(language.getString("MAIN.FORMAT.NORMAL"));
        SUCCESS = charReplace(language.getString("MAIN.FORMAT.SUCCESS"));
    }

    public static void debug(String msg) {
        debug(Level.SEVERE, msg);
    }

    public static void debug(Level level, String msg) {
        getInstance().getLogger().log(level, charReplace("&6[&c&lCreatioLib - Debug&6] " + msg));
    }

    public static void send(Player player, String... message) {
        for (String s : message) {
            Packet.out.Chat(Json.wrapSingleLine(s).toString(), EnumChatMessageType.SYSTEM, null).send(player);
        }
    }
    public static void send(Player player, Json... json) {
        for (Json j : json) {
            if (j != null) Packet.out.Chat(j.getJSON(), EnumChatMessageType.SYSTEM, null).send(player);
        }
    }


    public static void send(Level level, String... message) {
        for (String s : message) {
            getInstance().getLogger().log(level, StringUtil.wipeHex(charReplace(s)));
        }
    }
    public static void send(Level level, Json... json) {
        for (Json j : json) {
            getInstance().getLogger().log(level, charReplace(j.toSingleLine()));
        }
    }

    public static String getStatic(String path) {
        return language.getString(path);
    }


    public static void sendStatic(String path, @Nullable Player player) {
        broadcastStatic(Level.INFO, MessagePrefix.MAIN, path, player);
    }

    public static void sendStatic(String path, @Nullable Player player, String... vars) {
        broadcastStatic(Level.INFO, MessagePrefix.MAIN, path, player, vars);
    }

    public static void sendStatic(MessagePrefix prefix, String path, @Nullable Player player) {
        broadcastStatic(Level.INFO, prefix, path, player);
    }

    public static void sendStatic(MessagePrefix prefix, String path, @Nullable Player player, String... vars) {
        broadcastStatic(Level.INFO, prefix, path, player, vars);
    }


    public static void sendStatic(Level level, String path) {
        broadcastStatic(level, MessagePrefix.MAIN, path, null);
    }

    public static void sendStatic(Level level, String path, String... vars) {
        broadcastStatic(level, MessagePrefix.MAIN, path, null, vars);
    }

    public static void sendStatic(Level level, MessagePrefix prefix, String path) {
        broadcastStatic(level, prefix, path, null);
    }

    public static void sendStatic(Level level, MessagePrefix prefix, String path, String... vars) {
        broadcastStatic(level, prefix, path, null, vars);
    }

    private static void broadcastStatic(Level level, MessagePrefix prefix, String path, @Nullable Player player, String... vars) {
        String[] msg = singleReplace(prefix, path, vars);

        boolean isConsole = player == null;
        for (String m : msg) {
            if (isConsole) send(level, StringUtil.wipeHex(m)); else send(player, Json.wrapSingleLine(m));
        }
    }

    public static String[] singleReplace(@Nullable MessagePrefix prefix, @NotNull String path, @Nullable String... vars) {
        MessagePrefix pf = prefix == null ? MessagePrefix.MAIN : prefix;
        Object var = language.get(path);
        var = (var == null) ? path : var;
        if (var instanceof List) {
            List<String> temp = new ArrayList<>();
            for (String s : (List<String>) var) {
                temp.add(varReplace(pf.getPrefix(), s, vars));
            }
            return temp.toArray(new String[0]);
        } else {
            return new String[]{varReplace(pf.getPrefix(), var.toString(), vars)};
        }
    }

    public static String charReplace(String message) {
        message = message.replaceAll("&", "§")
                .replaceAll("/§", "&")
                .replaceAll("%w%", WARN)
                .replaceAll("%e%", ERROR)
                .replaceAll("%n%", NORMAL)
                .replaceAll("%s%", SUCCESS);
        Matcher mt = hex.matcher(message);
        while (mt.find()) {
            message = message.replaceAll(mt.group(0).replaceAll("\\{", "\\\\{"), TextColor.hexToColorCode(mt.group(1)));
        }
        return message;
    }

    private static String varReplace(String prefix, String message, String... vars) {
        message = charReplace(message.replaceAll("%prefix%", prefix));
        Matcher mt = var.matcher(message);
        while(mt.find()) {
            int i = Integer.parseInt(mt.group(1));
            if (vars.length > i) message = message.replaceAll(mt.group(0), vars[i]);
        }
        return message;
    }
}

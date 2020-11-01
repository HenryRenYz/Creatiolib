package com.henryrenyz.creatiolib.modules;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

public class Messager {

    private static FileConfiguration language;
    private static Pattern pattern = Pattern.compile("\\{\\#[0-9a-fA-F_]+\\}");
    private static Properties p = System.getProperties();
    private static String PREFIX = "&8&l[&b&lCreatio&3&lLib&8&l] &r";
    private static String WARN = "&e";
    private static String ERROR = "&c";
    private static String NORMAL = "&7";
    private static String SUCCESS = "&a";

    public static void initialization(JavaPlugin plugin, String lang) {
        Config.updateConfig(plugin, "lang/" + lang + ".yml", -1);
        language = Config.load(plugin, "lang/" + lang + ".yml");
        PREFIX = language.getString("MAIN.FORMAT.PREFIX");
        WARN = language.getString("MAIN.FORMAT.WARN");
        ERROR = language.getString("MAIN.FORMAT.ERROR");
        NORMAL = language.getString("MAIN.FORMAT.NORMAL");
        SUCCESS = language.getString("MAIN.FORMAT.SUCCESS");
    }

    public static void logConsole(String[] message) {
        String[] msg = varReplace(message, null);
        for (String m : msg) {
            Bukkit.getLogger().info(m);
        }
    }

    public static void logStaticConsole(String path, String[] vars) {
        Object var = language.get(path);
        String[] msg;
        if (var instanceof List) {
            msg = varReplace(((List<String>) var).toArray(new String[((List) var).size()]), vars);
        } else {
            msg = varReplace(new String[]{var.toString()}, vars);
        }
        for (String m : msg) {
            Bukkit.getLogger().info(m);
        }
    }

    public static void sendMessage(String[] message, Player player) {
        String[] msg = varReplace(message, null);
        for (String m : msg) {
            player.sendMessage(m);
        }
    }

    public static void sendStaticMessage(String path, Player player, String[] vars) {
        Object var = language.get(path);
        String[] msg;
        if (var instanceof List) {
            msg = varReplace(((List<String>) var).toArray(new String[((List) var).size()]), vars);
        } else {
            msg = varReplace(new String[]{var.toString()}, vars);
        }
        for (String m : msg) {
            player.sendMessage(m);
        }
    }

    private static String[] varReplace(String[] message, String[] vars) {
        for (int k = 0; k < message.length; k++) {
            message[k] = (message[k]).replaceAll("%prefix%", PREFIX);
            message[k] = (message[k]).replaceAll("%w%", WARN);
            message[k] = (message[k]).replaceAll("%e%", ERROR);
            message[k] = (message[k]).replaceAll("%n%", NORMAL);
            message[k] = (message[k]).replaceAll("%s%", SUCCESS);
            message[k] = (message[k]).replaceAll("&", "§");
            message[k] = (message[k]).replaceAll("/§", "&");
            if (vars != null) {
                if (vars.length >= 1) message[k] = (message[k]).replaceAll("%0%", vars[0]);
                if (vars.length >= 2) message[k] = (message[k]).replaceAll("%1%", vars[1]);
                if (vars.length >= 3) message[k] = (message[k]).replaceAll("%2%", vars[2]);
                if (vars.length >= 4) message[k] = (message[k]).replaceAll("%3%", vars[3]);
            }
        }
        return message;
        /*
        Matcher matcher = pattern.matcher(message);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            matcher.appendReplacement(buffer, p.getProperty(color.substring(2,color.length() - 1),"null"));
        }
        matcher.appendTail(buffer);
         */
    }
}

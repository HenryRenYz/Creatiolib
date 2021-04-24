package vip.creatio.clib.basic.tools;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import vip.creatio.clib.basic.chat.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import vip.creatio.clib.basic.chat.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatMsgManager extends MsgManager {

    protected final Pattern vars;
    protected final Map<String, String> replaceMap = new HashMap<>();
    protected final Map<Pattern, String> replaceMapRegex = new HashMap<>();
    protected Configuration language;

    public FormatMsgManager(Configuration language, String prefix, String varsPattern, String hexPattern) {
        super(prefix, hexPattern);
        this.language = language;
        this.vars = Pattern.compile(varsPattern);
    }

    public FormatMsgManager(Configuration language, String prefix, String varsPattern) {
        super(prefix);
        this.language = language;
        this.vars = Pattern.compile(varsPattern);
    }

    public FormatMsgManager(Configuration language, String prefix) {
        super(prefix);
        this.language = language;
        this.vars = Pattern.compile("%([0-9]{1,2})%");
    }

    public FormatMsgManager(Configuration language) {
        this.language = language;
        this.vars = Pattern.compile("%([0-9]{1,2})%");
    }

    private String[] getString(String path) {
        if (language.isList(path)) {
            List<String> list = language.getStringList(path);
            return list.toArray(new String[0]);
        } else {
            Object obj = language.get(path);
            if (obj == null) return new String[0];
            return new String[]{obj.toString()};
        }
    }

    public void setLanguage(Configuration lang) {
        this.language = lang;
    }

    public void addReplacer(String from, String to) {
        addReplacer(from, to, false);
    }

    public void addReplacer(String from, String to, boolean isRegex) {
        if (isRegex)
            replaceMapRegex.put(Pattern.compile(from), to);
        else
            replaceMap.put(from, to);
    }

    public void addReplacerToPath(String from, String path) {
        addReplacerToPath(from, path, false);
    }

    public void addReplacerToPath(String from, String path, boolean isRegex) {
        if (isRegex)
            replaceMapRegex.put(Pattern.compile(from), getString(path)[0]);
        else
            replaceMap.put(from, getString(path)[0]);
    }

    public void sendStatic(@NotNull Level lvl, @NotNull String path, String... vars) {
        for (String s : fromPath0(path, vars)) {
            log(lvl, s);
        }
    }

    public void sendStatic(@NotNull String path, String... vars) {
        sendStatic(Level.INFO, path, vars);
    }

    public void sendStatic(@NotNull ConsoleCommandSender sender, String path, String... vars) {
        send(sender, fromPath0(path, vars));
    }

    public void sendStatic(@NotNull BlockCommandSender sender, String path, String... vars) {
        send(sender, fromPath0(path, vars));
    }

    public void sendStatic(@NotNull CommandSender sender, String path, String... vars) {
        send(sender, fromPath0(path, vars));
    }

    public void sendStatic(@NotNull Player player, String path, String... vars) {
        send(player, fromPath(path, vars));
    }

    public void sendStaticTitle(@NotNull Player player, String path, String... vars) {
        sendTitle(player, fromPath0(path, vars)[0]);
    }

    public void sendStaticSubTitle(@NotNull Player player, String path, String... vars) {
        sendSubTitle(player, fromPath0(path, vars)[0]);
    }

    public void sendStaticBar(@NotNull Player player, String path, String... vars) {
        sendBar(player, fromPath0(path, vars)[0]);
    }

    public String[] fromPath(@NotNull String path, String... vars) {
        String[] msg = fromPath0(path, vars);
        for (int i = 0; i < msg.length; i++) {
            msg[i] = replaceChars(msg[i]);
        }
        return msg;
    }

    private String[] fromPath0(@NotNull String path, String... vars) {
        String[] msg = getString(path);
        for (int i = 0; i < msg.length; i++) {
            msg[i] = replaceVars(msg[i], vars);
        }
        return msg;
    }

    public String replaceChars(String msg) {
        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            msg = msg.replace(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<Pattern, String> entry : replaceMapRegex.entrySet()) {
            msg = entry.getKey().matcher(msg).replaceAll(entry.getValue());
        }

        return super.replaceChars(msg);
    }

    public Component replaceChars(Component comp) {
        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            comp = comp.replace(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<Pattern, String> entry : replaceMapRegex.entrySet()) {
            comp = comp.replaceAll(entry.getKey(), entry.getValue());
        }

        return super.replaceChars(comp);
    }

    protected String replaceVars(@NotNull String msg, String... vars) {
        Matcher mt = this.vars.matcher(msg);
        while (mt.find()) {
            int i = Integer.parseInt(mt.group(1));
            if (vars.length > i)
                msg = msg.replace(mt.group(0), vars[i]);
        }
        return msg;
    }
}

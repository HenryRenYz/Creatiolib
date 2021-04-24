package vip.creatio.clib.basic.tools;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import vip.creatio.clib.basic.chat.ChatColor;
import vip.creatio.clib.basic.chat.Component;
import vip.creatio.clib.basic.packet.out.ChatPacket;
import vip.creatio.clib.basic.packet.out.SetTitlePacket;

import java.util.logging.Level;
import java.util.regex.Pattern;

public class MsgManager implements MsgSender {

    private String prefix = "";
    private String prefixPlaceholder = "";

    private String hex = "\\{#([0-9a-fA-F])}";
    private Pattern hexPattern = Pattern.compile(hex);

    public MsgManager() {
        setPrefix("");
    }

    public MsgManager(@NotNull String prefix) {
        setPrefix(prefix);
    }

    public MsgManager(@NotNull String prefix, @NotNull String hexPattern) {
        setPrefix(prefix);
        setHexPattern(hexPattern);
    }

    public final void setPrefix(@NotNull String prefix) {
        setPrefix(prefix, "%prefix%");
    }

    public final void setPrefix(@NotNull String prefix, @NotNull String placeholder) {
        this.prefix = replaceColors(prefix);
        this.prefixPlaceholder = placeholder;
    }

    public final void setHexPattern(@NotNull String hexPattern) {
        this.hex = hexPattern;
        this.hexPattern = Pattern.compile(this.hex);
    }

    @Override
    public void send(@NotNull Player player, Component... components) {
        for (Component c : components) {
            new ChatPacket(replaceChars(c)).send(player);
        }
    }

    @Override
    public void send(@NotNull BlockCommandSender sender, String... msg) {
        for (String s : msg) {
            sender.sendMessage(replaceChars(s));
        }
    }

    @Override
    public void send(@NotNull ConsoleCommandSender sender, String... msg) {
        for (String str : msg) {
            log(str);
        }
    }

    @Override
    public void sendBar(@NotNull Player player, @NotNull Component components) {
        for (Component c : components) {
            new SetTitlePacket(SetTitlePacket.Action.ACTIONBAR, replaceChars(c)).send(player);
        }
    }

    @Override
    public void sendTitle(@NotNull Player player, @NotNull Component components) {
        for (Component c : components) {
            new SetTitlePacket(SetTitlePacket.Action.TITLE, replaceChars(c)).send(player);
        }
    }

    @Override
    public void sendSubTitle(@NotNull Player player, @NotNull Component components) {
        for (Component c : components) {
            new SetTitlePacket(SetTitlePacket.Action.SUBTITLE, replaceChars(c)).send(player);
        }
    }

    @Override
    public void log(Level lvl, String msg) {
        Bukkit.getLogger().log(lvl, replaceChars(msg));
    }

    public String replaceColors(String msg) {
        return hexPattern
                .matcher(msg)
                .replaceAll(r -> ChatColor.hexToColorCode(r.group(1)))
                .replace('&', '§')
                .replace("\\§", "&");
    }

    public String replaceChars(String msg) {
        String str = replaceColors(msg);
        if (prefix != null) str = str.replace(prefixPlaceholder, prefix);
        return str;
    }

    public Component replaceChars(Component comp) {
        Component str = comp
                .replaceAll(hex, m -> m.replaceAll(r -> ChatColor.hexToColorCode(r.group(1))))
                .replace('&', '§')
                .replace("\\§", "&");
        if (prefix != null) str = str.replace(prefixPlaceholder, prefix);
        return str;
    }

    @Override
    public void debug(String msg) {
        log(Level.CONFIG, msg);
    }

    @Override
    public void intern(String msg) {
        log(Level.WARNING, msg);
    }
}

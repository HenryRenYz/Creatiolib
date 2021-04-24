package vip.creatio.clib.basic.tools;

import org.bukkit.command.CommandSender;
import vip.creatio.clib.basic.chat.Component;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface MsgSender extends ConsoleLogger {

    void send(@NotNull Player player, Component... components);

    default void send(@NotNull Player player, String... msg) {
        Component[] comp = new Component[msg.length];
        for (int i = 0; i < msg.length; i++) {
            comp[i] = Component.of(msg[i]);
        }
        send(player, comp);
    }

    void send(@NotNull BlockCommandSender sender, String... msg);

    default void send(@NotNull BlockCommandSender sender, Component... components) {
        String[] str = new String[components.length];
        for (int i = 0; i < components.length; i++) {
            str[i] = components[i].toSingleLine();
        }
        send(sender, str);
    }

    void send(@NotNull ConsoleCommandSender sender, String... msg);

    default void send(@NotNull ConsoleCommandSender sender, Component... components) {
        String[] str = new String[components.length];
        for (int i = 0; i < components.length; i++) {
            str[i] = components[i].toSingleLine();
        }
        send(sender, str);
    }

    default void send(@NotNull CommandSender sender, Component... components) {
        if (sender instanceof Player) {
            send((Player) sender, components);
        } else if (sender instanceof ConsoleCommandSender) {
            send((ConsoleCommandSender) sender, components);
        } else if (sender instanceof BlockCommandSender) {
            send((BlockCommandSender) sender, components);
        }
    }

    default void send(@NotNull CommandSender sender, String... msg) {
        if (sender instanceof Player) {
            send((Player) sender, msg);
        } else if (sender instanceof ConsoleCommandSender) {
            send((ConsoleCommandSender) sender, msg);
        } else if (sender instanceof BlockCommandSender) {
            send((BlockCommandSender) sender, msg);
        }
    }

    void sendBar(@NotNull Player player, @NotNull Component comp);

    default void sendBar(@NotNull Player player, String msg) {
        sendBar(player, Component.of(msg));
    }

    void sendTitle(@NotNull Player player, @NotNull Component comp);

    default void sendTitle(@NotNull Player player, String msg) {
        sendTitle(player, Component.of(msg));
    }

    void sendSubTitle(@NotNull Player player, @NotNull Component comp);

    default void sendSubTitle(@NotNull Player player, String msg) {
        sendSubTitle(player, Component.of(msg));
    }

}

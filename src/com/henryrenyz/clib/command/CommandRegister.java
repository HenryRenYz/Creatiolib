package com.henryrenyz.clib.command;

import com.henryrenyz.clib.Creatio;
import com.henryrenyz.clib.modules.reflection.ReflectionConstructor;
import com.henryrenyz.clib.modules.reflection.ReflectionMethod;
import com.henryrenyz.clib.test.InGameTest;
import org.apache.commons.lang3.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandRegister {

    private static final SimpleCommandMap COMMAND_MAP = (SimpleCommandMap) ReflectionMethod.CraftServer_getCommandMap.run(Creatio.getInstance().getServer());
    private final Map<String, Command> REGISTERED_COMMANDS = new HashMap<>();
    private final String FALLBACK_PREFIX;

    public static final List<String> COMPLETER_DIRECTIONS_4 = Collections.unmodifiableList(Arrays.asList("south", "north", "west", "east"));
    public static final List<String> COMPLETER_DIRECTIONS_6 = Collections.unmodifiableList(Arrays.asList("south", "north", "west", "east", "up", "down"));
    public static final List<String> SMALL_INT = Collections.unmodifiableList(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));


    public CommandRegister(String FALLBACK_PREFIX) {
        this.FALLBACK_PREFIX = FALLBACK_PREFIX;
    }

    //Command register
    public void init() {
        register(Main.register());
        register(InGameTest.register());
    }

    public void register(Command cmd) {
        COMMAND_MAP.register(cmd.getName(), FALLBACK_PREFIX, cmd);
        REGISTERED_COMMANDS.put(cmd.getName(), cmd);
    }

    public void unregister(Command cmd) {
        unregister(cmd.getName());
    }

    public void registerAll(Collection<Command> commands) {
        for (Command c : commands) {
            register(c);
        }
    }

    public void unregister(String lable) {
        Command cmd = REGISTERED_COMMANDS.get(lable);
        Validate.notNull(cmd, "Command " + lable + " never registered, can't be unregister!");
        cmd.unregister(COMMAND_MAP);
        REGISTERED_COMMANDS.remove(lable);
    }

    public void clearCommands() {
        for (String key : REGISTERED_COMMANDS.keySet()) {
            unregister(key);
        }
    }

    public Command getCommand(String name) {
        return REGISTERED_COMMANDS.get(name);
    }

    public Collection<Command> getCommands() {
        return Collections.unmodifiableCollection(REGISTERED_COMMANDS.values());
    }

    public static SimpleCommandMap getCommandMap() {
        return COMMAND_MAP;
    }

    public static PluginCommand create(@NotNull String name) {
        return (PluginCommand) ReflectionConstructor.PluginCommand.run(name, Creatio.getInstance());
    }
}

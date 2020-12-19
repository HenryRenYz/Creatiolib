package com.henryrenyz.clib.command;

import com.henryrenyz.clib.MultiblockAPI.MachineAPI.Machine;
import com.henryrenyz.clib.MultiblockAPI.MultiblockStructure;
import com.henryrenyz.clib.MultiblockAPI.RegisteredStructure;
import com.henryrenyz.clib.MultiblockAPI.StructureManager;
import com.henryrenyz.clib.modules.rawText.Clickable;
import com.henryrenyz.clib.modules.rawText.Hoverable;
import com.henryrenyz.clib.modules.rawText.Json;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.*;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.EnumUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

import static com.henryrenyz.clib.Creatio.getInstance;
import static com.henryrenyz.clib.modules.Message.*;
import static com.henryrenyz.clib.modules.Message.singleReplace;
import static com.henryrenyz.clib.modules.util.ArrayUtil.get;

public final class Main implements CommandBuilder {

    private static final String name = "creatiolib";
    private static final String description = "Main command of CreatioLib.";
    private static final List<String> aliases = Arrays.asList("creatio", "clib");

    private static final List<String>
        TC = Collections.unmodifiableList(Arrays.asList("structures", "reload")),
            TC_STR = Collections.unmodifiableList(Arrays.asList("list", "create", "remove", "save", "info")),
                TC_STR_LIST = Collections.unmodifiableList(Arrays.asList("registered", "multiblock", "active", "machine")),
                TC_STR_INFO = Collections.unmodifiableList(Arrays.asList("registered", "multiblock")),
            TC_RLD = Collections.unmodifiableList(Arrays.asList("structures", "all"))

    ;

    //No default constructor
    private Main() {}

    public static PluginCommand register() {
        PluginCommand cmd = CommandRegister.create(name);
        cmd.setDescription(description);
        cmd.setAliases(aliases);
        cmd.setPermission(null);
        cmd.setPermissionMessage(null);
        cmd.setExecutor(new Executor());
        cmd.setTabCompleter(new Completer());
        return cmd;
    }

    private static class Executor implements CommandExecutor {

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
            Player executor = (sender instanceof ConsoleCommandSender) ? null : (Player) sender;
            if (!sender.hasPermission("creatio.admin")) {
                sendStatic("MAIN.NO_PERM", executor);
                return true;
            }

            //max index args can have
            int l = args.length - 1;

            //creatio
            if (l >= 0) {

                //creatio reload
                if (args[0].equalsIgnoreCase(TC.get(1))) {

                    //creatio reload *
                    if (l == 0) {
                        sendStatic("MAIN.RELOAD.CONFIG", executor);
                        getInstance().loadAllConfig();
                        return true;
                    }
                    //creatio reload all
                    else if (args[1].equalsIgnoreCase(TC_RLD.get(1))) {
                        sendStatic("MAIN.RELOAD.CONFIG", executor);
                        getInstance().loadAllConfig();
                        return true;
                    }
                    //creatio reload structures
                    else if (args[1].equalsIgnoreCase(TC_RLD.get(0))) {

                        //creatio reload structures *name*
                        if (l >= 2) {
                            for (int i = 2; i < args.length; i++) {
                                File f = new File(getInstance().getDataFolder(), args[i]);
                                sendStatic("MAIN.RELOAD.SINGLE", executor, args[i]);
                                if (f.exists()) {

                                } else {
                                    sendStatic("MAIN.RELOAD.DONT_EXIST", executor, args[i]);
                                }
                            }
                        }
                        sendStatic("MAIN.RELOAD.STRUCTURE_CONFIG", executor);
                        int[] load_success = StructureManager.loadAll();
                        sendStatic("MAIN.RELOAD.DONE", executor, Integer.toString(load_success[0]), Integer.toString(load_success[1]));
                        return true;
                    } else {
                        sendStatic("COMMAND.USAGE.RELOAD", executor);
                        return true;
                    }
                }

                //creatio structures
                else if (args[0].equalsIgnoreCase(TC.get(0))) {
                    if (l >= 1) {

                        //creatio structures create
                        if (args[1].equalsIgnoreCase(TC_STR.get(1))) {

                            //creatio structures create *name* *direction*
                            if (l >= 3) {
                                if (EnumUtils.isValidEnum(BlockFace.class, args[3].toUpperCase())) {
                                    assert executor != null;
                                    Block b = executor.getTargetBlockExact(6);
                                    if (b != null) {
                                        MultiblockStructure m = new MultiblockStructure(args[2], b, BlockFace.valueOf(args[3].toUpperCase()));
                                        if (m.getDisabledReason() != null) {
                                            sendStatic("COMMAND.USAGE.STRUCTURES.FAILED", executor, m.getDisabledReason().name());
                                        } else {
                                            sendStatic("MAIN.MULTIBLOCK_API.CREATE", executor, m.getNameSpace());
                                        }
                                    } else {
                                        sendStatic("COMMAND.USAGE.STRUCTURES.NO_TARGET", executor);
                                    }
                                } else {
                                    sendStatic("MAIN.ERROR.INVALID", executor, args[2]);
                                    return true;
                                }
                            } else {
                                sendStatic("COMMAND.USAGE.STRUCTURES.CREATE", executor);
                                return true;
                            }
                        }
                        //creatio structures remove
                        else if (args[1].equalsIgnoreCase(TC_STR.get(2))) {
                            assert executor != null;
                            Block b = executor.getTargetBlockExact(6);
                            if (b != null) {
                                for (MultiblockStructure m : StructureManager.getMultiblockStructures()) {
                                    if (m.getBlockList().contains(b)) {
                                        m.delete();
                                        sendStatic("MAIN.MULTIBLOCK_API.REMOVE", executor, m.getName(), m.getNameSpace());
                                        return true;
                                    }
                                }
                            }
                            sendStatic("COMMAND.USAGE.STRUCTURES.NO_TARGET", executor);
                            return true;
                        }
                        //creatio structures list
                        else if (args[1].equalsIgnoreCase(TC_STR.get(0))) {

                            //creatio structures list *
                            if (l == 1) {
                                send(executor, listRegisteredStructures(1));
                                send(executor, listMultiblockStructures(1));
                                send(executor, listActiveStructures(1));
                                send(executor, listMachines(1));
                                return true;
                            } else {
                                //creatio structures list *page*
                                int page = 1;
                                try {
                                    if (args.length == 4) page = Integer.parseInt(args[3]);
                                } catch (NumberFormatException e) {
                                    sendStatic("MAIN.ERROR.NOT_NUM", executor, args[3]);
                                }

                                //creatio structures list registered
                                if (args[2].equalsIgnoreCase(TC_STR_LIST.get(0))) {
                                    send(executor, listRegisteredStructures(page));
                                    return true;
                                }
                                //creatio structures list multiblock
                                if (args[2].equalsIgnoreCase(TC_STR_LIST.get(1))) {
                                    send(executor, listMultiblockStructures(page));
                                    return true;
                                }
                                //creatio structures list active
                                if (args[2].equalsIgnoreCase(TC_STR_LIST.get(2))) {
                                    send(executor, listActiveStructures(page));
                                    return true;
                                }
                                //creatio structures list machine
                                if (args[2].equalsIgnoreCase(TC_STR_LIST.get(3))) {
                                    send(executor, listMachines(page));
                                    return true;
                                }
                                sendStatic("COMMAND.USAGE.STRUCTURES.LIST", executor);
                                return true;
                            }
                        }
                        //creatio structures save
                        else if (args[1].equalsIgnoreCase(TC_STR.get(3))) {
                            StructureManager.saveMultiblockStructure();
                            sendStatic("MAIN.MULTIBLOCK_API.SAVE", executor);
                        }
                        //creatio structures info
                        else if (args[1].equalsIgnoreCase(TC_STR.get(4))) {
                            if (l == 3) {
                                //creatio structures info registered
                                if (args[2].equalsIgnoreCase(TC_STR_INFO.get(0))) {
                                    for (RegisteredStructure reg : StructureManager.getRegisteredStructures()) {
                                        if (reg.getNameSpace().equals(args[3])) {
                                            send(executor, registeredInfo(reg));
                                            return true;
                                        }
                                    }
                                    sendStatic("MAIN.MULTIBLOCK_API.INFO.REGISTERED.NOT_EXIST", executor);
                                    return true;
                                }
                                //creatio structures info multiblock
                                else if (args[2].equalsIgnoreCase(TC_STR_INFO.get(1))) {
                                    for (MultiblockStructure m : StructureManager.getMultiblockStructures()) {
                                        if (args[3].equals(m.getUUID().toString())) {
                                            send(executor, multiblockInfo(m));
                                            return true;
                                        }
                                    }
                                    sendStatic("MAIN.MULTIBLOCK_API.INFO.STRUCTURE.NOT_EXIST", executor, args[2]);
                                    return true;
                                }
                                sendStatic("COMMAND.USAGE.STRUCTURES.INFO.USAGE", executor);
                            }
                            //creatio structures info * (info of the structure player looking at)
                            else {
                                assert executor != null;
                                Block b = executor.getTargetBlockExact(6);
                                if (b != null) {
                                    for (MultiblockStructure m : StructureManager.getMultiblockStructures()) {
                                        if (m.getBlockList().contains(b)) {
                                            send(executor, multiblockInfo(m));
                                            return true;
                                        }
                                    }
                                }
                                sendStatic("MAIN.MULTIBLOCK_API.INFO.STRUCTURES.NOT_FOUND", executor);
                            }
                            return true;
                            //sendStatic("COMMAND.USAGE.STRUCTURES.INFO.USAGE", executor);
                        }
                        //creatio structures ?
                        else {
                            sendStatic("COMMAND.USAGE.STRUCTURES.USAGE", executor);
                        }
                    } else {
                        sendStatic("COMMAND.USAGE.STRUCTURES.USAGE", executor);
                        return true;
                    }
                }
            } else {
                sendStatic("COMMAND.USAGE.CREATIO", executor);
            }
            return true;
        }
    }

    private static class Completer implements TabCompleter {

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
            if (!sender.isOp()) return null;
            int l = args.length - 1;

            //creatio *
            if (l == 0) return TC;

            //creatio structures
            if (TC.get(0).equalsIgnoreCase(get(args, 0))) {

                //creatio structures list
                if (TC_STR.get(0).equalsIgnoreCase(get(args, 1))) {

                    //creatio structures list registered
                    /*if (TC_STR_LIST.get(0).equalsIgnoreCase(get(args, 2))) {
                        if (l == 3) {
                            List<String> ll = new ArrayList<>();
                            for (RegisteredStructure m : StructureManager.getRegisteredStructures()) {
                                ll.add(m.getNameSpace());
                            }
                            return ll;
                        }
                    }*/

                    //creatio structures list * *int*
                    if (l == 3) return CommandRegister.SMALL_INT;
                    return (l == 2) ? TC_STR_LIST : new ArrayList<>();
                }

                //creatio structures create
                if (TC_STR.get(1).equalsIgnoreCase(get(args, 1))) {
                    //creatio structures create *name*
                    if (l == 2) {
                        List<String> ll = new ArrayList<>();
                        for (RegisteredStructure m : StructureManager.getRegisteredStructures()) {
                            ll.add(m.getNameSpace());
                        }
                        return ll;
                    }
                    //creatio structures create *name* *direction*
                    if (l == 3) {
                        return CommandRegister.COMPLETER_DIRECTIONS_4;
                    }
                }

                //creatio structures info
                if (TC_STR.get(4).equalsIgnoreCase(get(args, 1))) {
                    //creatio structures info registered *name*
                    if (TC_STR_INFO.get(0).equalsIgnoreCase(get(args, 2))) {
                        List<String> ll = new ArrayList<>();
                        for (RegisteredStructure m : StructureManager.getRegisteredStructures()) {
                            ll.add(m.getNameSpace());
                        }
                        return (l == 3) ? ll : new ArrayList<>();
                    }
                    //creatio structures info multiblock *uuid*
                    if (TC_STR_INFO.get(1).equalsIgnoreCase(get(args, 2))) {
                        List<String> ll = new ArrayList<>();
                        for (MultiblockStructure m : StructureManager.getMultiblockStructures()) {
                            ll.add(m.getUUID().toString());
                        }
                        return (l == 3) ? ll : new ArrayList<>();
                    }
                    return (l == 2) ? TC_STR_INFO : new ArrayList<>();
                }

                return (l == 1) ? TC_STR : new ArrayList<>();
            }

            //creatio reload
            if (TC.get(1).equalsIgnoreCase(get(args, 0))) return (l == 1) ? TC_RLD : new ArrayList<>();

            return new ArrayList<>();
        }
    }

    //List Registered Structures
    private static Json[] listRegisteredStructures(int page) {
        Json[] text = new Json[Math.min(StructureManager.getRegisteredMachine().size(), 15) + 3];
        if (StructureManager.getRegisteredStructures().size() == 0) {
            text[0] = new Json.PlainText(singleReplace(null, "MAIN.MULTIBLOCK_API.LIST.NOT_FOUND", "Registered Structures")[0]);
        } else {
            text[0] = new Json.PlainText(singleReplace(null,"MAIN.MULTIBLOCK_API.LIST.HEADER", "Registered Structures")[0]);
            int k = 1;
            for (RegisteredStructure m : StructureManager.getRegisteredStructures()) {
                text[k] = new Json.PlainText("  §l" + k + ". ")
                        .addExtra(new Json.PlainText("§e§l<i> §a" + m.getNameSpace())
                                .setClickEvent(new Clickable.RunCommand("/creatio structures info registered " + m.getNameSpace()))
                                .setHoverEvent(new Hoverable.ShowText(new Json.PlainText("§eView structure details"))));
                if (k >= 15) break;
                k++;
            }
            String cmdPrev = (page == 1) ? null : "/creatio structures list registered " + (page - 1);
            String cmdNex = (k < 15) ? null : "/creatio structures list registered " + (page + 1);
            text[k] = craftBottom(cmdPrev, cmdNex);
            text[k + 1] = new Json.PlainText(singleReplace(null,"MAIN.MULTIBLOCK_API.LIST.FOOTER", Integer.toString(k - 1))[0]);
        }
        return text;
    }

    //List Multiblock Structures
    private static Json[] listMultiblockStructures(int page) {
        Json[] text = new Json[Math.min(StructureManager.getMultiblockStructures().size(), 15) + 3];
        if (StructureManager.getMultiblockStructures().size() == 0) {
            text[0] = new Json.PlainText(singleReplace(null,"MAIN.MULTIBLOCK_API.LIST.NOT_FOUND", "Multiblock Structures")[0]);
        } else {
            text[0] = new Json.PlainText(singleReplace(null,"MAIN.MULTIBLOCK_API.LIST.HEADER", "Multiblock Structures")[0]);
            int k = 1;
            for (MultiblockStructure m : StructureManager.getMultiblockStructures()) {
                text[k] = new Json.PlainText("  §l" + k + ". ")
                        .addExtra(new Json.PlainText("§e§l<i> §a" + m.getNameSpace() + " §f- " + m.getName())
                                .setClickEvent(new Clickable.RunCommand("/creatio structures info multiblock " + m.getUUID()))
                                .setHoverEvent(new Hoverable.ShowText(new Json.PlainText("§eView structure details"))));
                if (k >= 15) break;
                k++;
            }
            String cmdPrev = (page == 1) ? null : "/creatio structures list multiblock " + (page - 1);
            String cmdNex = (k < 15) ? null : "/creatio structures list multiblock " + (page + 1);
            text[k + 1] = new Json.PlainText(singleReplace(null,"MAIN.MULTIBLOCK_API.LIST.FOOTER", Integer.toString(k - 1))[0]);
            text[k] = craftBottom(cmdPrev, cmdNex);
        }
        return text;
    }

    //List Active Structures
    private static Json[] listActiveStructures(int page) {
        System.out.println(StructureManager.getActiveStructures().size());
        Json[] text = new Json[Math.min(StructureManager.getActiveStructures().size(), 15) + 3];
        if (StructureManager.getActiveStructures().size() == 0) {
            text[0] = new Json.PlainText(singleReplace(null, "MAIN.MULTIBLOCK_API.LIST.NOT_FOUND", "Active Structures")[0]);
        } else {
            text[0] = new Json.PlainText(singleReplace(null,"MAIN.MULTIBLOCK_API.LIST.HEADER", "Active Structures")[0]);
            int k = 1;
            for (MultiblockStructure m : StructureManager.getActiveStructures()) {
                text[k] = new Json.PlainText("  §l" + k + ". ")
                        .addExtra(new Json.PlainText("§e§l<i> §a" + m.getNameSpace() + " §f- " + m.getName())
                                .setClickEvent(new Clickable.RunCommand("/creatio structures info multiblock " + m.getUUID()))
                                .setHoverEvent(new Hoverable.ShowText(new Json.PlainText("§eView structure details"))));
                if (k >= 15) break;
                k++;
            }
            String cmdPrev = (page == 1) ? null : "/creatio structures list active " + (page - 1);
            String cmdNex = (k < 15) ? null : "/creatio structures list active " + (page + 1);
            text[k + 1] = new Json.PlainText(singleReplace(null,"MAIN.MULTIBLOCK_API.LIST.FOOTER", Integer.toString(k - 1))[0]);
            text[k] = craftBottom(cmdPrev, cmdNex);
        }
        return text;
    }

    //List Machines
    private static Json[] listMachines(int page) {
        Json[] text = new Json[Math.min(StructureManager.getMachines().size(), 15) + 3];
        if (StructureManager.getMachines().size() == 0) {
            text[0] = new Json.PlainText(singleReplace(null, "MAIN.MULTIBLOCK_API.LIST.NOT_FOUND", "Machines")[0]);
        } else {
            text[0] = new Json.PlainText(singleReplace(null,"MAIN.MULTIBLOCK_API.LIST.HEADER", "Machines")[0]);
            int k = 1;
            for (Machine m : StructureManager.getMachines()) {
                text[k] = new Json.PlainText("  §l" + k + ". ")
                        .addExtra(new Json.PlainText("§e§l<i> §a" + m.getNameSpace() + " §f- " + m.getHookedStructure().getName())
                                .setClickEvent(new Clickable.RunCommand("/creatio structures info multiblock " + m.getHookedStructure().getUUID()))
                                .setHoverEvent(new Hoverable.ShowText(new Json.PlainText("§eView structure details"))));
                if (k >= 15) break;
                k++;
            }
            String cmdPrev = (page == 1) ? null : "/creatio structures list machine " + (page - 1);
            String cmdNex = (k < 15) ? null : "/creatio structures list machine " + (page + 1);
            text[k + 1] = new Json.PlainText(singleReplace(null,"MAIN.MULTIBLOCK_API.LIST.FOOTER", Integer.toString(k - 1))[0]);
            text[k] = craftBottom(cmdPrev, cmdNex);
        }
        return text;
    }

    private static Json craftBottom(@Nullable String prevCommand, @Nullable String sufCommand) {
        Json json = new Json.PlainText();
        if (prevCommand == null) json.addExtra(new Json.PlainText("  §7<<< "));
        else json.addExtra(new Json.PlainText("  §2§l<<< ").setClickEvent(new Clickable.RunCommand(prevCommand)));
        json.addExtra(new Json.PlainText("§3|"));
        if (sufCommand == null) json.addExtra(new Json.PlainText(" §7>>>"));
        else json.addExtra(new Json.PlainText(" §2§l>>>").setClickEvent(new Clickable.RunCommand(sufCommand)));
        return json;
    }

    private static String genTpCmdFromLoc(Location loc) {
        return "/tp @s " +
                loc.getBlockX() + ' ' +
                loc.getBlockY() + ' ' +
                loc.getBlockZ();
    }

    private static Json[] multiblockInfo(MultiblockStructure str) {
        Json[] text = new Json[10];
        text[0] = new Json.PlainText(singleReplace(null,"MAIN.MULTIBLOCK_API.INFO.STRUCTURES.HEADER", str.getNameSpace())[0]);
        text[1] = new Json.PlainText("  §7UUID: §e" + str.getUUID())
                .setHoverEvent(new Hoverable.ShowText(new Json.PlainText("§eCopy UUID")))
                .setClickEvent(new Clickable.SuggestCommand(str.getUUID().toString()));
        text[2] = new Json.PlainText("  §7Name space: §f" + str.getNameSpace());
        text[3] = new Json.PlainText("  §7Custom name: §f" + str.getName());
        text[4] = new Json.PlainText("  §7Is flipped: §f" + str.isFlipped());
        text[5] = new Json.PlainText("  §7Direction: §f" + str.getFacing().name());
        text[6] = new Json.PlainText("  §7Corner block: §f")
                .addExtra(new Json.PlainText
                        ("[§a" + str.getCorner().getX() + "§7, §a" + str.getCorner().getY() + "§7, §a" + str.getCorner().getZ() + "§f]")
                        .setHoverEvent(new Hoverable.ShowText(new Json.PlainText("§eCopy coords")))
                        .setClickEvent(new Clickable.SuggestCommand(genTpCmdFromLoc(str.getCorner().getLocation())))
                .addExtra(new Json.PlainText
                        (" [§2" + str.getEndCorner().getX() + "§7, §2" + str.getEndCorner().getY() + "§7, §2" + str.getEndCorner().getZ() + "§f]")
                        .setHoverEvent(new Hoverable.ShowText(new Json.PlainText("§eCopy coords")))
                        .setClickEvent(new Clickable.SuggestCommand(genTpCmdFromLoc(str.getCorner().getLocation())))));
        text[7] = new Json.PlainText(" §7 Center block: §f")
                .addExtra(new Json.PlainText
                (" [§b" + str.getCore().getX() + "§7, §b" + str.getCore().getY() + "§7, §b" + str.getCore().getZ() + "§f]")
                .setHoverEvent(new Hoverable.ShowText(new Json.PlainText("§eCopy coords")))
                .setClickEvent(new Clickable.SuggestCommand(genTpCmdFromLoc(str.getCore().getLocation()))));
        text[8] = new Json.PlainText("  §fIn active state: §e" + StructureManager.getActiveStructures().contains(str));
        text[9] = new Json.PlainText("  §8Hash Code: §7" + str.hashCode());
        return text;
    }

    private static Json[] registeredInfo(RegisteredStructure reg) {
        Json[] text = new Json[7];
        text[0] = new Json.PlainText(singleReplace(null,"MAIN.MULTIBLOCK_API.INFO.STRUCTURES.HEADER", reg.getNameSpace())[0]);
        text[1] = new Json.PlainText("  §7Name space: §f" + reg.getNameSpace());
        text[2] = new Json.PlainText("  §7Default name: §f" + reg.getName());
        text[3] = new Json.PlainText("  §7Flippable: §f" + reg.isFlippable());
        text[4] = new Json.PlainText("  §7Center material: §f" + reg.getCenter());
        text[5] = new Json.PlainText("  §7Hooked machine class: §f" + reg.getMachineClass().getCanonicalName());
        text[6] = new Json.PlainText("  §7Size(lhw): §f" + reg.getCorner()[0] + " " + reg.getCorner()[1] + " " + reg.getCorner()[2]);
        return text;
    }

//    private static String MultiblockStructureJSON(int Index, MultiblockStructure m) {
//        String msg = Messager.getStatic("MAIN.MULTIBLOCK_API.LIST.CONTENT");
//        msg = msg.replaceAll("%0%", "%1$s").replaceAll("%1%", "%2$s");
//        JsonParagraph j = new JsonParagraph(new Content.PlainText(m.getName()))
//                .setColor("orange")
//                .setBold(true)
//                .setExtra(new ArrayList<>(new JsonParagraph(new Content.PlainText("§7(" + m.getNameSpace() + "§7)"))))
//                .setHoverEvent(new HoverEvent.ShowText(new ArrayList<>(new JsonParagraph(new Content.PlainText("Click to show details"))
//                        .setColor("#9042f5")))).setBold(true)
//                .setClickEvent(new ClickEvent.RunCommand("/creatio structures info" + m.getUUID()));
//        JsonParagraph f = new JsonParagraph(new Content.TranslatedText(" §2 %1$s §a%2$s",
//                new JsonParagraph[]{new JsonParagraph(Integer.toString(Index)), j}));
//        return f.getJSON();

}

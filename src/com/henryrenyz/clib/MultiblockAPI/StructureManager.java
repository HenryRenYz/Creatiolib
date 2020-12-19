package com.henryrenyz.clib.MultiblockAPI;

import com.henryrenyz.clib.MultiblockAPI.MachineAPI.Machine;
import com.henryrenyz.clib.modules.Serializer;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.henryrenyz.clib.Creatio.getInstance;

public class StructureManager {

    private static final List<RegisteredStructure> registered_structures = new LinkedList<>();
    private static final List<MultiblockStructure> multiblock_structures = new LinkedList<>();
    private static final List<MultiblockStructure> active_structures = new LinkedList<>();
    private static final List<Machine> machine = new LinkedList<>();
    private static final List<Class<?>> registered_machine = new LinkedList<>();

    public static int[] loadAll() {
        List<FileConfiguration> conf = new ArrayList<>();
        int k = 0;
        int kd = 0;

        for (File f : new File(getInstance().getDataFolder(), "multiblock_structures").listFiles((dir, name) ->
                new File(dir,name).isFile() && !name.contains("data"))) {
            conf.add(YamlConfiguration.loadConfiguration(f));
            k++;
        }

        for (FileConfiguration c : conf) {
            if ((loadRegisteredStructure(c))) {
                kd++;
            }
        }

        loadMultiblockStructure();

        return new int[]{k,kd};
    }

    public static boolean loadRegisteredStructure(FileConfiguration c) {
        for (String m : c.getConfigurationSection("STRUCTURES").getKeys(false)) {
            Map<String, Object> map = Serializer.readStructure(c, m, false), map1 = new HashMap<>();
            map1.put("data", map);
            map1.put("id", m);
            RegisteredStructure.deserialize(map1);
        }
//                Messager.StaticMessage(Level.SEVERE, "MAIN.ERROR.INVALID", new String[]{c.getString("STRUCTURES." + m + ".CENTER_BLOCK")});
//                Messager.StaticMessage(Level.SEVERE, "MAIN.RELOAD.CORRUPT", new String[]{m});
        return true;
    }

    public static boolean loadMultiblockStructure() {
        File f = new File(getInstance().getDataFolder(), "multiblock_structures/data/saved_structures.yml");
        if (f.exists()) {
            multiblock_structures.clear();
            FileConfiguration c = YamlConfiguration.loadConfiguration(f);
            if (c.getConfigurationSection("DATA") != null) {
                for(String m : c.getConfigurationSection("DATA").getKeys(false)) {

                    Map<String, Object> map = Serializer.readStructure(c, m, true);
                    MultiblockStructure.deserialize(map);
                }
            }
        }
        return false;
    }

    public static boolean saveMultiblockStructure() {
        if (multiblock_structures.size() > 0) {
//          File[] fl = new File(plugin.getDataFolder(), "multiblock_structures/data").listFiles();
//            if (fl.length > 30) {
//                fl[fl.length - 1].delete();
//            }
            File f = new File(getInstance().getDataFolder(), "multiblock_structures/data/saved_structures.yml");
            try {
                if (f.exists()) {
                    f.delete();
                }
                File v = new File(getInstance().getDataFolder(), "multiblock_structures/data/saved_structures.yml");
                v.getParentFile().mkdirs();
                v.createNewFile();

                FileConfiguration c = YamlConfiguration.loadConfiguration(f);
                int k = 0;
                for (MultiblockStructure m : multiblock_structures) {
                    c.set("DATA."+m.getUUID(), m.serialize());
                    k++;

                }
                c.save(v);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static void releaseChunks() {
        for (MultiblockStructure m : multiblock_structures) {
            for (Chunk ch : m.getForceChunks()) {
                ch.setForceLoaded(false);
            }
        }
    }

    public static void registerMachine(Class<?> cls) {
        if (!registered_machine.contains(cls)) {
            registered_machine.add(cls);
        }
    }

    public static Class<?> getMachineClass(String name) {
        for (Class<?> cls : registered_machine) {
            if (cls.getSimpleName().equalsIgnoreCase(name)) {
                return cls;
            }
        }
        return null;
    }

    public static List<RegisteredStructure> getRegisteredStructures() {
        return registered_structures;
    }

    public static List<MultiblockStructure> getMultiblockStructures() {
        return multiblock_structures;
    }

    public static List<MultiblockStructure> getActiveStructures() {
        return active_structures;
    }

    public static List<Machine> getMachines() {
        return machine;
    }

    public static List<Class<?>> getRegisteredMachine() {
        return registered_machine;
    }
}

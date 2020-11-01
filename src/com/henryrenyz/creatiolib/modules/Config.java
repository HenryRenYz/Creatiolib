package com.henryrenyz.creatiolib.modules;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class Config {

    public static FileConfiguration load(JavaPlugin plugin, String name) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void updateConfig(JavaPlugin plugin, String name, int version) {
        File file = new File(plugin.getDataFolder(), name);
        if (file.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            if ((version > config.getInt("version")) || (version == -1)) {
                try {
                    Messager.logConsole(new String[]{"&8&l[&b&lCreatio&3&lLib&8&l] &aFile &6&l&n" + name + "&a is out-of-date, auto updating..."});
                    if (version != -1) config.set("version", version);
                    updateYML(file, YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource(name))));
                } catch (IOException e) {
                    Messager.logConsole(new String[]{"&8&l[&b&lCreatio&3&lLib&8&l] &4File &6&l&n" + name + "&4 auto update failed, stack trace:"});
                    e.printStackTrace();
                    return;
                }
                Messager.logConsole(new String[]{"&8&l[&b&lCreatio&3&lLib&8&l] &fFile &6&l&n" + name + "&f successfully updated."});
            } else {
                return;
            }
        } else {
            plugin.saveResource(name, false);
        }
    }

    public static void updateYML(File old, FileConfiguration sample) throws IOException {
        FileConfiguration config = YamlConfiguration.loadConfiguration(old);
        List<String> keys = Arrays.asList(config.getKeys(true).toArray(new String[config.getKeys(true).size()]));
        List<String> ori = Arrays.asList(sample.getKeys(true).toArray(new String[sample.getKeys(true).size()]));

        for (String value : ori) {
            if (!keys.contains(value)) config.set(value, sample.get(value));
        }
        config.save(old);
    }
}

package vip.creatio.clib.basic.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public final class Configs {

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
                    if (version != -1) {
                        Bukkit.getLogger().log(Level.INFO, "&8&l[&b&lCreatio&3&lLib&8&l] &aFile &6&l&n" + name + "&a is out-of-date, auto updating...");
                        config.set("version", version);
                        config.save(file);
                    }
                    //byte[] data = new byte[1024];
                    //new FileInputStream(file).read(data);
                    updateYML(file, YamlConfiguration.loadConfiguration(new InputStreamReader(Objects.requireNonNull(plugin.getResource(name)))));
                } catch (IOException e) {
                    Bukkit.getLogger().log(Level.SEVERE, "&8&l[&b&lCreatio&3&lLib&8&l] &4File &6&l&n" + name + "&4 load failed, stack trace:");
                    e.printStackTrace();
                    return;
                }
                if (version != -1) Bukkit.getLogger().log(Level.INFO, "&8&l[&b&lCreatio&3&lLib&8&l] &fFile &6&l&n" + name + "&f successfully updated.");
            }
        } else {
            plugin.saveResource(name, false);
        }
    }

    public static void updateYML(File old, FileConfiguration sample) throws IOException {
        FileConfiguration config = YamlConfiguration.loadConfiguration(old);
        List<String> keys = Arrays.asList(config.getKeys(true).toArray(new String[0]));
        String[] ori = sample.getKeys(true).toArray(new String[0]);

        for (String value : ori) {
            if (!keys.contains(value)) config.set(value, sample.get(value));
        }
        config.options().copyDefaults(true);
        config.save(old);
    }

    public static void saveResources(JavaPlugin plugin, String dest, String name) {
        File file = new File(plugin.getDataFolder(), dest);
        if (file.exists()) file.delete();
        else if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        InputStream is = plugin.getResource(name);
        if (is != null) {
            try {
                FileOutputStream os = new FileOutputStream(file, false);
                int len;
                byte[] buf = new byte[4096];
                while ((len = is.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Bukkit.getLogger().warning("Failed to save resource " + name + "!");
        }
    }
}


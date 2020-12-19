package com.henryrenyz.clib;

import com.henryrenyz.clib.modules.Message;
import org.bukkit.Bukkit;
import org.bukkit.util.Consumer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;

import static com.henryrenyz.clib.Creatio.*;

public class Updater implements Runnable {

    @Override
    public void run() {
        //https://github.com/HenryRenYz/CreatioLib/releases
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Bukkit.getLogger().warning("&8&l[&b&lCreatio&3&lLib&8&l] &4Updater delay is interrupted.");
            return;
        }
        new Getter(85307).getVersion(version -> {
            String present = getInstance().getDescription().getVersion();
            if (require_update(version, present)) {
                Message.sendStatic(Level.INFO, "MAIN.UPDATE", present, version);
            }
        });
    }

    private boolean require_update(String ver1, String ver2) {
        Float[] Ver1 = getVersion(ver1);
        Float[] Ver2 = getVersion(ver2);
        for (int k = 0; k < Ver1.length; k++) {
            if (Ver1[k] > Ver2[k]) return true;
        }
        return false;
    }

    private Float[] getVersion(String a) {
        String[] split = a.split("-")[0].split("[.]");
        Float[] result = new Float[split.length];
        int k = 0;
        for (String val : split) {
            result[k] = Float.parseFloat(val);
            k++;
        }
        return result;
    }

    static final class Getter {

        private final int resourceId;

        public Getter(int resourceId) {
            this.resourceId = resourceId;
        }

        public void getVersion(final Consumer<String> consumer) {
            Bukkit.getScheduler().runTaskAsynchronously(getInstance(), () -> {
                try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                    if (scanner.hasNext()) {
                        consumer.accept(scanner.next());
                    }
                } catch (IOException exception) {
                    getInstance().getLogger().info("&8&l[&b&lCreatio&3&lLib&8&l] &4Cannot found update: " + exception.getMessage());
                }
            });
        }
    }
}

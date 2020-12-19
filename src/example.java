import com.henryrenyz.clib.modules.classLoader.ExternalInterface;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * Example external Java file, can be load and unload in-game
 * just like a script.
 * The file can be compiled through
 *      "/creatio java compile <FileName>"
 * Replace <FileName> to example for this file, this has always
 * to be direct file name, rather than a path. For this reason
 * no files under java_class folder should have the same names.
 *
 * Implements of ExternalInterface gives two methods that calls
 * when class is loading or unloading.
 *
 * Arguments plugin gives JavaPlugin object, which can be used
 * to register events and something else.
 *
 * NOTE: Since the external classes connect through reflection,
 * implementing a function through several classes were not
 * recommended, so have some inner class or non-public class can
 * be a good choice.
 */
public class example implements ExternalInterface {

    @Override
    public void load(JavaPlugin plugin) {
        System.out.println("EXTERNAL CLASS: Hello world!");
    }

    @Override
    public void unload(JavaPlugin plugin) {
        System.out.println("EXTERNAL CLASS: Goodbye world!");
    }
}

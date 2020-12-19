package com.henryrenyz.clib.modules.classLoader;

import com.henryrenyz.clib.Creatio;
import org.bukkit.plugin.java.JavaPlugin;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class ExternalLoader {

    private static final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private static final StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
    private static final List<String> compile_option;

    private static final File classPath = new File(Creatio.getInstance().getDataFolder(), "java_class");
    private static final Map<String, Class<?>> EXTERNAL_CLASS = new HashMap<>();

    static {
        if (classPath.exists()) {
            classPath.mkdirs();
            try {
                File example = new File(classPath, "example.java");
                example.createNewFile();
                FileWriter writer = new FileWriter(example);
                writer.write(
                        "import com.henryrenyz.clib.modules.classLoader.ExternalInterface;\n" +
                        "import org.bukkit.plugin.java.JavaPlugin;\n" +
                        "\n" +
                        "/*\n" +
                        " * Example external Java file, can be load and unload in-game\n" +
                        " * just like a script.\n" +
                        " * The file can be compiled through\n" +
                        " *      \"/creatio java compile <FileName>\"\n" +
                        " * Replace <FileName> to example for this file, this has always\n" +
                        " * to be direct file name, rather than a path. For this reason\n" +
                        " * no files under java_class folder should have the same names.\n" +
                        " *\n" +
                        " * Implements of ExternalInterface gives two methods that calls\n" +
                        " * when class is loading or unloading.\n" +
                        " *\n" +
                        " * Arguments plugin gives JavaPlugin object, which can be used\n" +
                        " * to register events and something else.\n" +
                        " *\n" +
                        " * NOTE: Since the external classes connect through reflection,\n" +
                        " * implementing a function through several classes were not\n" +
                        " * recommended, so have some inner class or non-public class can\n" +
                        " * be a good choice.\n" +
                        " */\n" +
                        "public class example implements ExternalInterface {\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public void load(JavaPlugin plugin) {\n" +
                        "        System.out.println(\"EXTERNAL CLASS: Hello world!\");\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public void unload(JavaPlugin plugin) {\n" +
                        "        System.out.println(\"EXTERNAL CLASS: Goodbye world!\");\n" +
                        "    }\n" +
                        "}");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<String> coption = new ArrayList<>();
        coption.add("-d");
        coption.add(new File(classPath, "compiled").getPath());
        compile_option = Collections.unmodifiableList(coption);
    }

    public static void compileAll(JavaPlugin plugin) {
        File[] fileList = classPath.listFiles(
                file1 -> file1.isFile() && file1.getName().endsWith(".java") && !file1.getName().startsWith("-"));
        File[] files = compile(fileList);
        Class<?>[] clazz = loadClass(files);
        for (Class c : clazz) {
            EXTERNAL_CLASS.put(c.getSimpleName(), c);
        }
    }

    public static File[] compile(File... fileList) {
        Iterable<? extends JavaFileObject> units = manager.getJavaFileObjects(fileList);
        compiler.getTask(null, manager, null, compile_option, null, units).call();
        return new File(classPath, "compiled").listFiles(
                filter -> filter.getName().endsWith(".class") && !filter.getName().startsWith("-"));
    }

    public static Class<?>[] loadClass(File... file) {
        try {
            URL[] url = new URL[file.length];
            for (int i = 0; i < file.length; i++) {
                url[i] = file[i].toURI().toURL();
            }
            ClassLoader loader = new URLClassLoader(url);
            Class<?>[] clazz = new Class[file.length];
            for (int i = 0; i < file.length; i++) {
                clazz[i] = loader.loadClass(file[i].getName());
            }
            return clazz;
        } catch (MalformedURLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}

package vip.creatio.clib.basic.tools;

import java.util.logging.Level;

public interface ConsoleLogger {

    void log(Level lvl, String msg);

    default void log(Level lvl,  String... msgs) {
        for (String s : msgs) {
            log(lvl, s);
        }
    }

    default void log(String msg) {
        log(Level.INFO, msg);
    }

    default void log(String... msgs) {
        for (String s : msgs) {
            log(s);
        }
    }

    void debug(String msg);

    void intern(String msg);

    default void warn(String msg) {
        log(Level.WARNING, msg);
    }

    default void err(String msg) {
        log(Level.SEVERE, msg);
    }
}

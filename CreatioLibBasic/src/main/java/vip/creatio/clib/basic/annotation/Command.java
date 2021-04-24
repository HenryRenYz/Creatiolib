package vip.creatio.clib.basic.annotation;

/**
 * A method marked by @Command will be wrapped to a
 * CommandExecutor
 */
public @interface Command {

    String[] cmd();



}

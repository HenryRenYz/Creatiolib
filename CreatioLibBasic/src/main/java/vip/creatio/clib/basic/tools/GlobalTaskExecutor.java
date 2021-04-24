package vip.creatio.clib.basic.tools;

public interface GlobalTaskExecutor extends TaskExecutor {

    void addOnLoadTask(Runnable task);

    void addOnUnloadTask(Runnable task);

    default void removeOnLoadTask(Runnable task) {
        throw new UnsupportedOperationException("operation unsupported!");
    }

    default void removeOnUnloadTask(Runnable task) {
        throw new UnsupportedOperationException("operation unsupported!");
    }

}

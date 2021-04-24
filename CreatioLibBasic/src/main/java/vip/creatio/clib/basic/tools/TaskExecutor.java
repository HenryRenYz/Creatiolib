package vip.creatio.clib.basic.tools;

public interface TaskExecutor {

    void addSyncTask(Runnable task, int intervalMillis);

    default void addSyncTask(Runnable task) {
        addSyncTask(task, 50);
    }

    void addAsyncTask(Runnable task, int intervalMillis);

    default void addAsyncTask(Runnable task) {
        addAsyncTask(task, 50);
    }

    default void removeTask(Runnable task) {
        throw new UnsupportedOperationException("operation unsupported!");
    }

    default void removeAsyncTask(Runnable task) {
        throw new UnsupportedOperationException("operation unsupported!");
    }

    // Tick clock
    int counter();

    void start();

    void onLoad();

    void onUnload();
}

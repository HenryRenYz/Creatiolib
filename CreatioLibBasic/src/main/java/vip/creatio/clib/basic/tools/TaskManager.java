package vip.creatio.clib.basic.tools;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Iterator;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TaskManager implements GlobalTaskExecutor {

    protected final Queue<SyncTask> TASK = new ConcurrentLinkedQueue<>();
    protected final Queue<AsyncTask> ASYNC_TASK = new ConcurrentLinkedQueue<>();
    /** Called when plugin unload */
    protected final Queue<Runnable> UNLOAD_TASK = new ConcurrentLinkedQueue<>();
    /** Called when plugin load */
    protected final Queue<Runnable> LOAD_TASK = new ConcurrentLinkedQueue<>();

    protected final Timer TIMER = new Timer("TaskManagerAsyncExecutor");
    protected final JavaPlugin plugin;

    public TaskManager(JavaPlugin register) {
        this.plugin = register;
    }

    public void addSyncTask(Runnable r, int interval) {
        TASK.add(new SyncTask(r, interval / 50));
    }

    public void addAsyncTask(Runnable r, int interval) {
        AsyncTask task = new AsyncTask(r, interval);
        TIMER.schedule(task, 0, interval);
        ASYNC_TASK.add(task);
    }

    public void addOnUnloadTask(Runnable r) {
        UNLOAD_TASK.add(r);
    }

    public void addOnLoadTask(Runnable r) {
        LOAD_TASK.add(r);
    }

    public void removeTask(Runnable r) {
        Iterator<SyncTask> iter = TASK.iterator();
        while (iter.hasNext()) {
            Runnable runnable = iter.next().runnable;
            if (runnable == r) {
                iter.remove();
                return;
            }
        }
    }

    public void removeAsyncTask(Runnable r) {
        Iterator<AsyncTask> iter = ASYNC_TASK.iterator();
        while (iter.hasNext()) {
            AsyncTask task = iter.next();
            if (task.runnable == r) {
                iter.remove();
                task.cancel();
                return;
            }
        }
    }

    public void removeOnUnloadTask(Runnable r) {
        UNLOAD_TASK.remove(r);
    }

    public void removeOnLoadTask(Runnable r) {
        LOAD_TASK.remove(r);
    }

    public void onUnload() {
        UNLOAD_TASK.forEach(Runnable::run);
        TIMER.cancel();
    }

    public void onLoad() {
        LOAD_TASK.forEach(Runnable::run);
    }


    private static int syncTickCounter = 0;

    public int counter() {
        return syncTickCounter;
    }

    public void start() {
        BukkitScheduler scheduler = Bukkit.getScheduler();

        //1 ~ 20 ticks clock
        scheduler.scheduleSyncRepeatingTask(plugin, () -> {
            syncTickCounter++;

            // Task executor
            for (SyncTask task : TASK) {
                if (syncTickCounter % task.interval == 0)
                    task.run();
            }
        }, 10L, 1L);
    }

    private static class AsyncTask extends TimerTask {

        private final Runnable runnable;
        private final int interval; // in millisecond

        private AsyncTask(Runnable t, int interval) {
            this.runnable = t;
            this.interval = interval;
        }

        @Override
        public void run() {
            runnable.run();
        }
    }

    private static class SyncTask implements Runnable {

        private final Runnable runnable;
        private final int interval; // in tick

        private SyncTask(Runnable r, int interval) {
            this.runnable = r;
            this.interval = interval;
        }

        @Override
        public void run() {
            runnable.run();
        }
    }
}

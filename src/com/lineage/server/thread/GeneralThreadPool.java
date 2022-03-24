//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lineage.server.thread;

import com.lineage.config.Config;
import com.lineage.server.model.monitor.L1PcMonitor;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GeneralThreadPool {
    private static final Log _log = LogFactory.getLog(GeneralThreadPool.class);
    private static GeneralThreadPool _instance;
    private static final int SCHEDULED_CORE_POOL_SIZE = 100;
    private Executor _executor;
    private ScheduledExecutorService _scheduler;
    private ScheduledExecutorService _pcScheduler;
    private ScheduledExecutorService _aiScheduler;
    private final int _pcSchedulerPoolSize;

    public static GeneralThreadPool get() {
        if (_instance == null) {
            _instance = new GeneralThreadPool();
        }

        return _instance;
    }

    private GeneralThreadPool() {
        this._pcSchedulerPoolSize = 1 + Config.MAX_ONLINE_USERS / 10;
        this._executor = Executors.newCachedThreadPool();
        this._scheduler = Executors.newScheduledThreadPool(100, new GeneralThreadPool.PriorityThreadFactory("GSTPool", 5));
        this._pcScheduler = Executors.newScheduledThreadPool(this._pcSchedulerPoolSize, new GeneralThreadPool.PriorityThreadFactory("PSTPool", 5));
        this._aiScheduler = Executors.newScheduledThreadPool(this._pcSchedulerPoolSize, new GeneralThreadPool.PriorityThreadFactory("AITPool", 5));
    }

    public void execute(Runnable r) {
        try {
            if (this._executor == null) {
                Thread t = new Thread(r);
                t.start();
            } else {
                this._executor.execute(r);
            }
        } catch (Exception var3) {
            _log.error(var3.getLocalizedMessage(), var3);
        }

    }

    public void execute(Thread t) {
        try {
            t.start();
        } catch (Exception var3) {
            _log.error(var3.getLocalizedMessage(), var3);
        }

    }

    public ScheduledFuture<?> schedule(Runnable r, long delay) {
        try {
            if (delay <= 0L) {
                this._executor.execute(r);
                return null;
            } else {
                return this._scheduler.schedule(r, delay, TimeUnit.MILLISECONDS);
            }
        } catch (RejectedExecutionException var5) {
            _log.error(var5.getLocalizedMessage(), var5);
            return null;
        }
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable r, long initialDelay, long period) {
        try {
            return this._scheduler.scheduleAtFixedRate(r, initialDelay, period, TimeUnit.MILLISECONDS);
        } catch (Exception var7) {
            _log.error(var7.getLocalizedMessage(), var7);
            return null;
        }
    }

    public ScheduledFuture<?> pcSchedule(L1PcMonitor r, long delay) {
        try {
            if (delay <= 0L) {
                this._executor.execute(r);
                return null;
            } else {
                return this._pcScheduler.schedule(r, delay, TimeUnit.MILLISECONDS);
            }
        } catch (RejectedExecutionException var5) {
            _log.error(var5.getLocalizedMessage(), var5);
            return null;
        }
    }

    public ScheduledFuture<?> scheduleAtFixedRate(TimerTask command, long initialDelay, long period) {
        try {
            return this._aiScheduler.scheduleAtFixedRate(command, initialDelay, period, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException var7) {
            _log.error(var7.getLocalizedMessage(), var7);
            return null;
        }
    }

    public void cancel(ScheduledFuture<?> future, boolean mayInterruptIfRunning) {
        try {
            future.cancel(mayInterruptIfRunning);
        } catch (RejectedExecutionException var4) {
            _log.error(var4.getLocalizedMessage(), var4);
        }

    }

    public Timer aiScheduleAtFixedRate(TimerTask task, long delay, long period) {
        try {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(task, delay, period);
            return timer;
        } catch (RejectedExecutionException var7) {
            _log.error(var7.getLocalizedMessage(), var7);
            return null;
        }
    }

    public void cancel(TimerTask task) {
        try {
            task.cancel();
        } catch (Exception var3) {
            _log.error(var3.getLocalizedMessage(), var3);
        }

    }

    private class PriorityThreadFactory implements ThreadFactory {
        private final int _prio;
        private final String _name;
        private final AtomicInteger _threadNumber = new AtomicInteger(1);
        private final ThreadGroup _group;

        public PriorityThreadFactory(String name, int prio) {
            this._prio = prio;
            this._name = name;
            this._group = new ThreadGroup(this._name);
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(this._group, r);
            t.setName(this._name + "-" + this._threadNumber.getAndIncrement());
            t.setPriority(this._prio);
            return t;
        }

        public ThreadGroup getGroup() {
            return this._group;
        }
    }
}

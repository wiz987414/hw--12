package ru.sbt.manager.threadPool;

import ru.sbt.manager.context.ManagerContext;
import ru.sbt.manager.threadPool.poolThread.PoolThread;
import ru.sbt.manager.threadPool.poolThread.ScalablePoolThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ScalableThreadPool implements ThreadPool {
    private final ManagerContext executionContext;
    private final Queue<Runnable> tasks;
    private final List<ScalablePoolThread> workers;
    private final int minThreadCount;
    private final int maxThreadCount;

    public ScalableThreadPool(ManagerContext executionContext,
                              Queue<Runnable> tasks,
                              int minThreadCount,
                              int maxThreadCount) {
        this.executionContext = executionContext;
        this.tasks = tasks;
        this.workers = new ArrayList<>();
        this.minThreadCount = minThreadCount;
        this.maxThreadCount = maxThreadCount;
    }

    @Override
    public void start() {
        for (int i = 0; i < this.minThreadCount; i++) {
            this.workers.add(new ScalablePoolThread(this.tasks,
                    this.executionContext,
                    this.workers,
                    this.minThreadCount,
                    this.maxThreadCount));
            this.workers.get(i).start();
        }
    }

    @Override
    public void execute(Queue<Runnable> runnable) {
        synchronized (this.tasks) {
            this.tasks.notifyAll();
        }
    }


}

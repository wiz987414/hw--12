package ru.sbt.manager.threadPool;

import ru.sbt.manager.context.ManagerContext;
import ru.sbt.manager.threadPool.poolThread.PoolThread;

import java.util.List;
import java.util.Queue;

public class ScalableThreadPool implements ThreadPool {
    private final ManagerContext executionContext;
    private final Queue<Runnable> tasks;
    private final List<PoolThread> workers;
    private final int minThreadCount;
    private final int maxThreadCount;

    public ScalableThreadPool(ManagerContext executionContext,
                              Queue<Runnable> tasks,
                              List<PoolThread> workers,
                              int minThreadCount,
                              int maxThreadCount) {
        this.executionContext = executionContext;
        this.tasks = tasks;
        this.workers = workers;
        this.minThreadCount = minThreadCount;
        this.maxThreadCount = maxThreadCount;
    }

    @Override
    public void start() {
        for (int i = 0; i < this.minThreadCount; i++) {
            this.workers.add(new PoolThread(this.tasks, this.executionContext, this.workers));
            this.workers.get(i).start();
        }
    }

    @Override
    public void execute(Queue<Runnable> runnable) {

    }
}

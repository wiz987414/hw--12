package ru.sbt.manager.threadPool;

import ru.sbt.manager.context.ManagerContext;
import ru.sbt.manager.threadPool.poolThread.PoolThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class FixedThreadPool implements ThreadPool {
    private final ManagerContext executionContext;
    private final Queue<Runnable> tasks;
    private final List<PoolThread> workers;
    private final int threadCount;

    public FixedThreadPool(int threadsCount, Queue<Runnable> tasks, ManagerContext executionContext) {
        this.executionContext = executionContext;
        this.workers = new ArrayList<>();
        this.tasks = tasks;
        this.threadCount = threadsCount;
    }

    @Override
    public void start() {
        for (int i = 0; i < this.threadCount; i++) {
            this.workers.add(new PoolThread(this.tasks, this.executionContext, this.workers));
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

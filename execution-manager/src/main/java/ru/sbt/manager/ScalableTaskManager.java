package ru.sbt.manager;

import ru.sbt.manager.context.Context;
import ru.sbt.manager.context.ManagerContext;
import ru.sbt.manager.threadPool.FixedThreadPool;
import ru.sbt.manager.threadPool.ScalableThreadPool;

import java.util.ArrayDeque;
import java.util.Queue;

public class ScalableTaskManager implements ExecutionManager {
    private final ManagerContext executionContext;
    private volatile ScalableThreadPool workers;
    private volatile Queue<Runnable> tasks;
    private final int minThreadCount;
    private final int maxThreadCount;

    public ScalableTaskManager(int minThreadCount, int maxThreadCount) {
        this.tasks = new ArrayDeque<>();
        this.executionContext = new ManagerContext(this.getTasks());
        this.minThreadCount = minThreadCount;
        this.maxThreadCount = maxThreadCount;
    }

    public ManagerContext getExecutionContext() {
        return this.executionContext;
    }

    public Queue<Runnable> getTasks() {
        return this.tasks;
    }

    public ScalableThreadPool getWorkers() {
        return this.workers;
    }

    public int getMinThreadCount() {
        return minThreadCount;
    }

    public int getMaxThreadCount() {
        return maxThreadCount;
    }

    @Override
    public Context execute(Runnable callback, Queue<Runnable> tasks) {
        this.workers = new ScalableThreadPool(getExecutionContext(), getTasks(), getMinThreadCount(), getMaxThreadCount());
        this.getWorkers().start();
        tasks.add(callback);
        this.getWorkers().execute(tasks);
        return this.getExecutionContext();
    }
}

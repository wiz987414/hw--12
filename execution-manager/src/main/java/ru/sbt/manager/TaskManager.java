package ru.sbt.manager;

import ru.sbt.manager.context.Context;
import ru.sbt.manager.context.ManagerContext;
import ru.sbt.manager.threadPool.FixedThreadPool;
import ru.sbt.manager.threadPool.poolThread.PoolThread;
import ru.sbt.manager.workTasks.CallbackTask;
import ru.sbt.manager.workTasks.ExceptionTask;
import ru.sbt.manager.workTasks.WorkingTask;

import java.util.ArrayDeque;
import java.util.Queue;

public class TaskManager implements ExecutionManager {
    private final ManagerContext executionContext;
    private volatile FixedThreadPool workers;
    private volatile Queue<Runnable> tasks;
    private volatile int threadCount;

    public TaskManager(int threadCount) {
        this.tasks = new ArrayDeque<>();
        this.executionContext = new ManagerContext(this.getTasks());
        this.threadCount = threadCount;
    }

    public ManagerContext getExecutionContext() {
        return this.executionContext;
    }

    public Queue<Runnable> getTasks() {
        return this.tasks;
    }

    public FixedThreadPool getWorkers() {
        return this.workers;
    }

    @Override
    public Context execute(Runnable callback, Queue<Runnable> tasks) {
        this.workers = new FixedThreadPool(this.threadCount, getTasks(), getExecutionContext());
        this.getWorkers().start();
        tasks.add(callback);
        this.getWorkers().execute(tasks);
        return this.getExecutionContext();
    }
}

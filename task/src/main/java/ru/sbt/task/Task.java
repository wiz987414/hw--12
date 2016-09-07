package ru.sbt.task;

import ru.sbt.task.exceptions.TaskExecuteException;

import java.util.concurrent.*;

public class Task<T> {
    private final Callable callableTask;
    private volatile T taskResult;

    private Callable getCallableTask() {
        return callableTask;
    }

    private T getTaskResult() {
        return taskResult;
    }

    public <A> Task(Callable<? extends A> callable) {
        this.callableTask = callable;
        this.taskResult = null;
    }

    public T get() throws TaskExecuteException {
        if (this.getTaskResult() == null) {
            synchronized (this) {
                if (this.getTaskResult() == null) {
                    try {
                        this.taskResult = (T) this.getCallableTask().call();
                        return this.getTaskResult();
                    } catch (Exception e) {
                        throw new TaskExecuteException("Task executing exception", e, this);
                    }
                }
            }
        }
        return this.getTaskResult();
    }

    public synchronized void clear() {
        this.taskResult = null;
    }

}

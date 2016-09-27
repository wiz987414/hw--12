package ru.sbt.task;

import ru.sbt.task.exceptions.TaskExecuteException;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Task<T> {
    private final Callable callableTask;
    private final Lock lock;
    private volatile T taskResult;

    private Callable getCallableTask() {
        return callableTask;
    }

    private T getTaskResult() {
        return taskResult;
    }

    public <A> Task(Callable<? extends A> callable) {
        this.callableTask = callable;
        this.lock = new ReentrantLock();
        this.taskResult = null;
    }

    private Lock getLock() {
        return lock;
    }

    private void clearTaskResult() {
        this.taskResult = null;
    }

    public T get() throws TaskExecuteException {
        if (getTaskResult() == null) {
            try {
                if (getLock().tryLock(1,TimeUnit.SECONDS)) {
                    if (getTaskResult() == null) {
                        try {
                            this.taskResult = (T) getCallableTask().call();
                            return getTaskResult();
                        } catch (Exception e) {
                            throw new TaskExecuteException("Task executing exception", e, this);
                        }
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("Unable to lock", e);
            } finally {
                getLock().unlock();
            }
        }
        T result = getTaskResult();
        clearTaskResult();
        return result;
    }

    public synchronized void clear() {
        this.taskResult = null;
    }

}

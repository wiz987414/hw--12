package ru.sbt.manager.context;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class ManagerContext implements Context {
    private final Queue<Runnable> tasks;
    private volatile List<String> resultList;
    private volatile int completedTasks;
    private volatile int failedTasks;
    private volatile int interruptedTasks;
    private volatile boolean finished;

    public Queue<Runnable> getTasks() {
        return tasks;
    }

    public List<String> getResultList() {
        return resultList;
    }

    public ManagerContext(Queue<Runnable> tasks) {
        this.tasks = tasks;
        resultList = new CopyOnWriteArrayList<>();
    }

    public synchronized int updateFailedTass() {
        this.failedTasks = this.getFailedTaskCount() + 1;
        return this.getFailedTaskCount();
    }

    public synchronized int updateCompletedTass() {
        this.completedTasks = this.getCompletedTaskCount() + 1;
        return this.getCompletedTaskCount();
    }

    public synchronized int updateInterruptedTasks(int count) {
        this.interruptedTasks = this.getInterruptedTaskCount() + count;
        return this.getInterruptedTaskCount();
    }

    @Override
    public int getCompletedTaskCount() {
        return this.completedTasks;
    }

    @Override
    public int getFailedTaskCount() {
        return this.failedTasks;
    }

    @Override
    public int getInterruptedTaskCount() {
        return this.interruptedTasks;
    }

    @Override
    public synchronized void interrupt() {
        if (!tasks.isEmpty()) {
            this.updateInterruptedTasks(tasks.size());
            tasks.clear();
        }
    }

    @Override
    public boolean isFinished() {
        this.finished = tasks.isEmpty() && failedTasks == 0;
        return this.finished;
    }

    @Override
    public void showContext(String contextType) {
        StringBuilder contextString = new StringBuilder();
        contextString.append("------").append(contextType).append("------");
        contextString.append("\n-Completed   - ").append(this.getCompletedTaskCount()).append(" tasks");
        contextString.append("\n-Failed      - ").append(this.getFailedTaskCount()).append(" tasks");
        contextString.append("\n-Interrupted - ").append(this.getInterruptedTaskCount()).append(" tasks");
        contextString.append(isFinished() ? "\n- All tasks are finished" : "\n-Completed with exceptions");
        contextString.append("\n-------------------------");
        System.out.println(contextString.toString());
    }
}

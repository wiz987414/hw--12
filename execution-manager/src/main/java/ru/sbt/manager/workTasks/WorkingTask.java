package ru.sbt.manager.workTasks;

import ru.sbt.manager.context.ManagerContext;

import java.util.Calendar;

public class WorkingTask implements Runnable {
    private final ManagerContext workingContext;

    public WorkingTask(ManagerContext workingContext) {
        this.workingContext = workingContext;
    }

    @Override
    public synchronized void run() {
        this.workingContext.getResultList().add(Thread.currentThread().getName() + " - " + Calendar.getInstance().getTimeInMillis());
    }
}

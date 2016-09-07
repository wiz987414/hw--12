package ru.sbt.task.taskDomain;

import java.util.Calendar;
import java.util.concurrent.Callable;

public class WorkTask implements Callable<String> {
    @Override
    public String call() throws Exception {
        Thread.sleep(500);
        return Thread.currentThread().getName() + "-" + Calendar.getInstance().getTime();
    }
}

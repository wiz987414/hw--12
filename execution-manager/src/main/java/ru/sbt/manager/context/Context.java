package ru.sbt.manager.context;

import java.util.List;
import java.util.Queue;

public interface Context {
    Queue<Runnable> getTasks();

    List<String> getResultList();

    int getCompletedTaskCount();

    int getFailedTaskCount();

    int getInterruptedTaskCount();

    void interrupt();

    void showContext(String contextType);

    boolean isFinished();
}

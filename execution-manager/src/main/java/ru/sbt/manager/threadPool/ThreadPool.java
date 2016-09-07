package ru.sbt.manager.threadPool;

import java.util.Queue;

public interface ThreadPool {
    void start();

    void execute(Queue<Runnable> runnable);
}

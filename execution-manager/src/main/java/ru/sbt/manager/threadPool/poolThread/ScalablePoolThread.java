package ru.sbt.manager.threadPool.poolThread;

import ru.sbt.manager.context.ManagerContext;

import java.util.List;
import java.util.Queue;

public class ScalablePoolThread extends PoolThread {
    public ScalablePoolThread(Queue<Runnable> tasks, ManagerContext managerContext, List<PoolThread> finishedWorkers) {
        super(tasks, managerContext, finishedWorkers);
    }

    @Override
    public void run() {

    }
}

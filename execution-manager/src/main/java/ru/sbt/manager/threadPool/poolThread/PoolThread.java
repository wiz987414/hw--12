package ru.sbt.manager.threadPool.poolThread;

import ru.sbt.manager.context.ManagerContext;
import ru.sbt.manager.workTasks.CallbackTask;
import java.util.List;
import java.util.Queue;

public class PoolThread extends Thread {
    private final ManagerContext managerContext;
    private final ManagerContext threadContext;
    private final List<PoolThread> finishedWorkers;
    private volatile boolean workStatus;

    public PoolThread(Queue<Runnable> tasks, ManagerContext managerContext, List<PoolThread> finishedWorkers) {
        this.managerContext = managerContext;
        this.threadContext = new ManagerContext(tasks);
        this.finishedWorkers = finishedWorkers;
        workStatus = true;
    }

    private void interruptPoolThread() {
        System.out.println("\nFinishing thread - " + Thread.currentThread().getName());
        this.threadContext.updateInterruptedTasks(this.managerContext.getInterruptedTaskCount());
        this.threadContext.showContext("Thread context");
        if (this.managerContext.getTasks().isEmpty()) {
            workStatus = false;
        }
    }

    private void finalisePool() {
        boolean poolIsFree;
        do {
            poolIsFree = true;
            for (PoolThread worker : this.finishedWorkers) {
                if (!worker.equals(Thread.currentThread()) && worker.getState() == State.WAITING) {
                    poolIsFree = false;
                }
                if (worker.getState() == State.TERMINATED) poolIsFree = false;
            }
        } while (poolIsFree);
        this.finishedWorkers.stream().filter(interruptedWorker ->
                !interruptedWorker.equals(Thread.currentThread())).forEach(Thread::interrupt);
        Thread.currentThread().interrupt();
    }

    @Override
    public void run() {
        Runnable currentTask = null;
        while (workStatus) {
            synchronized (this.managerContext.getTasks()) {
                while (this.managerContext.getTasks().isEmpty()) {
                    try {
                        this.managerContext.getTasks().wait();
                    } catch (InterruptedException e) {
                        interruptPoolThread();
                        break;
                    }
                }
                if (!this.managerContext.getTasks().isEmpty())
                    currentTask = this.managerContext.getTasks().remove();
            }
            try {
                if (currentTask != null) {
                    currentTask.run();
                    if (currentTask.getClass() != CallbackTask.class) {
                        this.managerContext.updateCompletedTass();
                        this.threadContext.updateCompletedTass();
                    } else finalisePool();
                    currentTask = null;
                }
            } catch (RuntimeException e) {
                this.managerContext.updateFailedTass();
                this.threadContext.updateFailedTass();
                managerContext.getResultList().add("Exception in thread - " + Thread.currentThread().getName());
            }
        }
    }
}

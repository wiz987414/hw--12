package ru.sbt.manager.workTasks;

import ru.sbt.manager.context.ManagerContext;

public class CallbackTask implements Runnable {
    private final ManagerContext resultContext;

    public CallbackTask(ManagerContext resultContext) {
        this.resultContext = resultContext;
    }

    @Override
    public void run() {
        System.out.println("\n---------------------\n"
                + "Callback task started\n---------------------\n");
    }
}

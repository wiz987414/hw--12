package ru.sbt.manager.workTasks;

public class CallbackTask implements Runnable {

    @Override
    public void run() {
        System.out.println("\n---------------------\n"
                + "Callback task started\n---------------------\n");
    }
}

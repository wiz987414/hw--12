package ru.sbt.manager.workTasks;

public class ExceptionTask implements Runnable {
    @Override
    public void run() {
        throw new RuntimeException(new Exception("Processing exception"));
    }
}

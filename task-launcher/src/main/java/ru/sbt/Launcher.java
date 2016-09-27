package ru.sbt;

import ru.sbt.manager.ScalableTaskManager;
import ru.sbt.manager.TaskManager;
import ru.sbt.manager.context.Context;
import ru.sbt.task.Task;
import ru.sbt.task.taskDomain.WorkTask;
import ru.sbt.manager.workTasks.CallbackTask;
import ru.sbt.manager.workTasks.ExceptionTask;
import ru.sbt.manager.workTasks.WorkingTask;

public class Launcher {
    public static void main(String[] args) {
        System.out.println("\nTask №1\n--------------------------------------");
        Task task = new Task(new WorkTask());
        Runnable launchTask = () -> System.out.println(Thread.currentThread().getName() + " >> " + task.get());
        for (int i = 0; i < 20; i++) {
            new Thread(launchTask).start();
            task.clear();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Exception causes interrupted sleeping thread", e);
        }
        System.out.println("\nTask №2\n--------------------------------------");
        TaskManager taskManager = new TaskManager(4);
        for (int i = 0; i < 30; i++) {
            if (i % 5 == 0)
                taskManager.getTasks().add(new ExceptionTask());
            taskManager.getTasks().add(new WorkingTask(taskManager.getExecutionContext()));
        }
        Context managerContext = taskManager.execute(new CallbackTask(), taskManager.getTasks());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Exception causes interrupted sleeping thread", e);
        }
        managerContext.getResultList().forEach(System.out::println);
        managerContext.showContext("Tasks context");
        System.out.println("\nTask №3\n--------------------------------------");
        ScalableTaskManager scalableTaskManager = new ScalableTaskManager(2, 5);
        for (int i = 0; i < 30; i++) {
            if (i % 5 == 0)
                scalableTaskManager.getTasks().add(new ExceptionTask());
            scalableTaskManager.getTasks().add(new WorkingTask(scalableTaskManager.getExecutionContext()));
        }
        Context scalableContext = scalableTaskManager.execute(new CallbackTask(),
                scalableTaskManager.getTasks());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Exception causes interrupted sleeping thread", e);
        }
        scalableContext.getResultList().forEach(System.out::println);
        scalableContext.showContext("Tasks context");
    }
}

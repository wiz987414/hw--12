package ru.sbt.task.exceptions;

import ru.sbt.task.Task;

public class TaskExecuteException extends RuntimeException {
    private final Task taskCause;

    public TaskExecuteException(String message, Throwable cause, Task taskCause) {
        super(message, cause);
        this.taskCause = taskCause;
    }
}

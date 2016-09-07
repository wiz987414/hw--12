package ru.sbt.manager;

import ru.sbt.manager.context.Context;

import java.util.Queue;

public interface ExecutionManager {
    Context execute(Runnable callback, Queue<Runnable> tasks);
}

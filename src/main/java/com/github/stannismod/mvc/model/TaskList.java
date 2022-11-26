package com.github.stannismod.mvc.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TaskList {

    private final String name;
    private final Map<String, Task> tasks = new HashMap<>();

    public TaskList(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addTask(Task task) {
        tasks.put(task.getName(), task);
    }

    public void removeTask(String name) {
        tasks.remove(name);
    }

    public Task getTask(String name) {
        return tasks.get(name);
    }

    public Collection<Task> tasks() {
        return tasks.values();
    }
}

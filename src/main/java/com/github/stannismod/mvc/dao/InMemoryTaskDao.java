package com.github.stannismod.mvc.dao;

import com.github.stannismod.mvc.model.Task;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryTaskDao implements TaskDao {

    private final Map<String, Task> tasks = new HashMap<>();

    @Override
    public void addTask(final Task task) {
        tasks.put(task.getName(), task);
    }

    @Override
    public void removeTask(final String name) {
        tasks.remove(name);
    }

    @Override
    public Task getTaskByName(final String name) {
        return tasks.get(name);
    }

    @Override
    public void setTaskStatus(final String name, final Task.Status status) {
        getTaskByName(name).setStatus(status);
    }

    @Override
    public List<Task> getTasks() {
        return tasks.values().stream()
                .sorted(Comparator.comparing(Task::getStatus).thenComparing(Task::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getActualTasks() {
        long now = System.currentTimeMillis();
        return tasks.values().stream()
                .filter(t -> t.getStatus() == Task.Status.ASSIGNED)
                .filter(t -> t.getDue().getTime() > now)
                .sorted(Comparator.comparing(Task::getStatus).thenComparing(Task::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getOverdueTasks() {
        long now = System.currentTimeMillis();
        return tasks.values().stream()
                .filter(t -> t.getStatus() == Task.Status.ASSIGNED)
                .filter(t -> t.getDue().getTime() <= now)
                .sorted(Comparator.comparing(Task::getStatus).thenComparing(Task::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getCompletedTasks() {
        return tasks.values().stream()
                .filter(t -> t.getStatus() == Task.Status.COMPLETED)
                .collect(Collectors.toList());
    }
}

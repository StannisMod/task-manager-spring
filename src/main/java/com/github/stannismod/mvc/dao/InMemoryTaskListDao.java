package com.github.stannismod.mvc.dao;

import com.github.stannismod.mvc.model.Task;
import com.github.stannismod.mvc.model.TaskList;

import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryTaskListDao implements TaskListDao {

    private final Map<String, TaskList> tasks = new HashMap<>();

    @Override
    public Iterable<String> getTaskLists() {
        return tasks.keySet();
    }

    @Override
    public void addList(final String list) {
        tasks.put(list, new TaskList(list));
    }

    @Override
    public void removeList(final String list) {
        tasks.remove(list);
    }

    protected TaskList getTaskList(String list) {
        TaskList taskList = tasks.get(list);
        if (taskList == null) {
            taskList = new TaskList(list);
            tasks.put(list, taskList);
        }
        return taskList;
    }

    @Override
    public void addTask(final Task task) {
        getTaskList(task.getList()).addTask(task);
    }

    @Override
    public void removeTask(final String list, final String name) {
        getTaskList(list).removeTask(name);
    }

    @Override
    public Task getTaskByName(final String list, final String name) {
        return getTaskList(list).getTask(name);
    }

    @Override
    public void setTaskStatus(final String list, final String name, final Task.Status status) {
        getTaskByName(list, name).setStatus(status);
    }

    @Override
    public List<Task> getTasks(final String list) {
        return getTaskList(list).tasks().stream()
                .sorted(Comparator.comparing(Task::getStatus).thenComparing(Task::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getActualTasks(final String list) {
        long now = System.currentTimeMillis() / 1000;
        return getTaskList(list).tasks().stream()
                .filter(t -> t.getStatus() == Task.Status.ASSIGNED)
                .filter(t -> t.getDue().toEpochSecond(ZoneOffset.UTC) > now)
                .sorted(Comparator.comparing(Task::getStatus).thenComparing(Task::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getOverdueTasks(final String list) {
        long now = System.currentTimeMillis() / 1000;
        return getTaskList(list).tasks().stream()
                .filter(t -> t.getStatus() == Task.Status.ASSIGNED)
                .filter(t -> t.getDue().toEpochSecond(ZoneOffset.UTC) <= now)
                .sorted(Comparator.comparing(Task::getStatus).thenComparing(Task::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getCompletedTasks(final String list) {
        return getTaskList(list).tasks().stream()
                .filter(t -> t.getStatus() == Task.Status.COMPLETED)
                .collect(Collectors.toList());
    }
}

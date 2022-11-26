package com.github.stannismod.mvc.dao;

import com.github.stannismod.mvc.model.Task;

import java.util.List;

public interface TaskListDao {

    Iterable<String> getTaskLists();

    void addList(String list);

    void removeList(String list);

    void addTask(Task task);

    void removeTask(String list, String name);

    Task getTaskByName(String list, String name);

    void setTaskStatus(String list, String name, Task.Status status);

    List<Task> getTasks(String list);

    List<Task> getActualTasks(String list);

    List<Task> getOverdueTasks(String list);

    List<Task> getCompletedTasks(String list);
}

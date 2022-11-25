package com.github.stannismod.mvc.dao;

import com.github.stannismod.mvc.model.Task;

import java.util.List;

public interface TaskDao {

    void addTask(Task task);

    void removeTask(Task task);

    List<Task> getTasks();

    List<Task> getActualTasks();

    List<Task> getOverdueTasks();

    List<Task> getFinishedTasks();
}

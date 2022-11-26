package com.github.stannismod.mvc.logic;

import com.github.stannismod.mvc.dao.TaskListDao;
import com.github.stannismod.mvc.model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DataFilter {

    Map<String, DataFilter> filters = prepareFilters();
    static Map<String, DataFilter> prepareFilters() {
        Map<String, DataFilter> res = new HashMap<>();
        res.put("all", (list, tasks) -> tasks.getTasks(list));
        res.put("actual", (list, tasks) -> tasks.getActualTasks(list));
        res.put("overdue", (list, tasks) -> tasks.getOverdueTasks(list));
        res.put("completed", (list, tasks) -> tasks.getCompletedTasks(list));
        return res;
    }

    List<Task> filter(String list, TaskListDao tasks);

    static Optional<DataFilter> getFilterByName(String name) {
        return Optional.ofNullable(filters.get(name));
    }
}

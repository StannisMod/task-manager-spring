package com.github.stannismod.mvc.logic;

import com.github.stannismod.mvc.dao.TaskDao;
import com.github.stannismod.mvc.model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DataFilter {

    Map<String, DataFilter> filters = prepareFilters();
    static Map<String, DataFilter> prepareFilters() {
        Map<String, DataFilter> res = new HashMap<>();
        res.put("all", TaskDao::getTasks);
        res.put("actual", TaskDao::getActualTasks);
        res.put("overdue", TaskDao::getOverdueTasks);
        res.put("completed", TaskDao::getCompletedTasks);
        return res;
    }

    List<Task> filter(TaskDao tasks);

    static Optional<DataFilter> getFilterByName(String name) {
        return Optional.ofNullable(filters.get(name));
    }
}

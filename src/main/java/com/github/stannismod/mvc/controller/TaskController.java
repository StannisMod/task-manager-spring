package com.github.stannismod.mvc.controller;

import com.github.stannismod.mvc.dao.TaskDao;
import com.github.stannismod.mvc.model.Filter;
import com.github.stannismod.mvc.model.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class TaskController {

    private final TaskDao tasks;

    public TaskController(final TaskDao tasks) {
        this.tasks = tasks;
    }

    @RequestMapping("/add-task", method = RequestMethod.POST)
    public String addTask(@ModelAttribute("task") Task task) {
        tasks.addTask(task);
        return "redirect:/tasks";
    }

    @RequestMapping(value = "/get-tasks", method = RequestMethod.GET)
    public String getTasks(ModelMap map) {
        prepareModelMap(map, tasks.getTasks());
        return "index";
    }

    private void prepareModelMap(ModelMap map, List<Task> tasks) {
        map.addAttribute("tasks", tasks);
        map.addAttribute("task", new Task());
        map.addAttribute("filter", new Filter());
    }
}

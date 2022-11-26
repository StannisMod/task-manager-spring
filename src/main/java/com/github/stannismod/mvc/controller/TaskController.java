package com.github.stannismod.mvc.controller;

import com.github.stannismod.mvc.dao.TaskListDao;
import com.github.stannismod.mvc.logic.DataFilter;
import com.github.stannismod.mvc.model.Filter;
import com.github.stannismod.mvc.model.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class TaskController {

    private final TaskListDao tasks;

    public TaskController(final TaskListDao tasks) {
        this.tasks = tasks;
    }

    @RequestMapping(value = "/add-task", method = RequestMethod.POST)
    public String addTask(@ModelAttribute("task") Task task, @RequestParam String list) {
        task.setList(list);
        tasks.addTask(task);
        return "redirect:/tasks?list=" + task.getList();
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public String getTasks(@RequestParam(required = false, defaultValue = "") String list, ModelMap map) {
        prepareModelMap(map, list, tasks.getTaskLists(), tasks.getTasks(list));
        return "index";
    }

    @RequestMapping(value = "/filter-tasks", method = RequestMethod.GET)
    public String getTasks(@RequestParam String list, @RequestParam String filter, ModelMap map) {
        Optional<DataFilter> dataFilter = DataFilter.getFilterByName(filter);
        dataFilter.ifPresent(f -> prepareModelMap(map, list, tasks.getTaskLists(), f.filter(list, tasks)));
        return "index";
    }

    @RequestMapping(value = "/complete-task", method = RequestMethod.POST)
    public String completeTask(@RequestParam String list, @RequestParam String name) {
        tasks.setTaskStatus(list, name, Task.Status.COMPLETED);
        return "redirect:/tasks?list=" + list;
    }

    @RequestMapping(value = "/remove-task", method = RequestMethod.POST)
    public String removeTask(@RequestParam String list, @RequestParam String name) {
        tasks.removeTask(list, name);
        return "redirect:/tasks?list=" + list;
    }

    @RequestMapping(value = "/add-task-list", method = RequestMethod.POST)
    public String addList(@RequestParam String name) {
        tasks.addList(name);
        return "redirect:/tasks?list=" + name;
    }

    @RequestMapping(value = "/remove-task-list", method = RequestMethod.POST)
    public String removeList(@RequestParam String name) {
        tasks.removeList(name);
        return "redirect:/tasks";
    }

    private void prepareModelMap(ModelMap map, String list, Iterable<String> lists, List<Task> tasks) {
        map.addAttribute("tasks", tasks);
        map.addAttribute("list", list);
        map.addAttribute("lists", lists);
        map.addAttribute("task", new Task());
        map.addAttribute("filter", new Filter());
        map.addAttribute("now", new Date());
    }
}

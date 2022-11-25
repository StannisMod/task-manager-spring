package com.github.stannismod.mvc.controller;

import com.github.stannismod.mvc.dao.TaskDao;
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

    private final TaskDao tasks;

    public TaskController(final TaskDao tasks) {
        this.tasks = tasks;
    }

    @RequestMapping(value = "/add-task", method = RequestMethod.POST)
    public String addTask(@ModelAttribute("task") Task task) {
        tasks.addTask(task);
        return "redirect:/tasks";
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public String getTasks(ModelMap map) {
        prepareModelMap(map, tasks.getTasks());
        return "index";
    }

    @RequestMapping(value = "/filter-tasks", method = RequestMethod.GET)
    public String getTasks(@RequestParam String filter, ModelMap map) {
        Optional<DataFilter> dataFilter = DataFilter.getFilterByName(filter);
        dataFilter.ifPresent(f -> prepareModelMap(map, f.filter(tasks)));
        return "index";
    }

    @RequestMapping(value = "/complete-task", method = RequestMethod.POST)
    public String completeTask(@ModelAttribute("name") String name) {
        tasks.setTaskStatus(name, Task.Status.COMPLETED);
        return "redirect:/tasks";
    }

    @RequestMapping(value = "/remove-task", method = RequestMethod.POST)
    public String removeTask(@ModelAttribute("name") String name) {
        tasks.removeTask(name);
        return "redirect:/tasks";
    }

    private void prepareModelMap(ModelMap map, List<Task> tasks) {
        map.addAttribute("wrapper", new TasksWrapper(tasks));
        map.addAttribute("task", new Task());
        map.addAttribute("filter", new Filter());
        map.addAttribute("now", new Date());
    }

    public static class TasksWrapper {

        private List<Task> tasks;

        public TasksWrapper(final List<Task> tasks) {
            this.tasks = tasks;
        }

        public List<Task> getTasks() {
            return tasks;
        }

        public void setTasks(final List<Task> tasks) {
            this.tasks = tasks;
        }
    }
}

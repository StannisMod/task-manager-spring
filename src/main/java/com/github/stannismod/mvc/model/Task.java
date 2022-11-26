package com.github.stannismod.mvc.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private String list;
    private String name;
    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime due;
    private Status status = Status.ASSIGNED;

    public Task() {
        this("", "", LocalDateTime.now());
    }

    public Task(final String name, final String description, final LocalDateTime due) {
        this.name = name;
        this.description = description;
        this.due = due;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public LocalDateTime getDue() {
        return due;
    }

    public void setDue(final LocalDateTime due) {
        this.due = due;
    }

    public String getDueString() {
        return due.format(DATE_TIME_FORMAT);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public String getList() {
        return list;
    }

    public void setList(final String list) {
        this.list = list;
    }

    public enum Status {
        ASSIGNED,
        COMPLETED
    }
}

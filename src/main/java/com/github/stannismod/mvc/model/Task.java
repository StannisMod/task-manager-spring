package com.github.stannismod.mvc.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.util.Date;

public class Task {

    private static final DateFormat DATE_TIME_FORMAT = DateFormat.getDateTimeInstance();

    private String name;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date due;
    private Status status = Status.ASSIGNED;

    public Task() {
        this("", "", new Date());
    }

    public Task(final String name, final String description, final Date due) {
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

    public Date getDue() {
        return due;
    }

    public void setDue(final Date due) {
        this.due = due;
    }

    public String getDueString() {
        return DATE_TIME_FORMAT.format(due);
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

    public enum Status {
        ASSIGNED,
        COMPLETED
    }
}

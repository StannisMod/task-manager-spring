package com.github.stannismod.mvc.model;

public class Task {

    private final String name;
    private final String description;
    private final long due;
    private Status status;

    public Task() {
        this("empty", "no description", 0);
    }

    public Task(final String name, final String description, final long due) {
        this.name = name;
        this.description = description;
        this.due = due;
        this.status = due < System.currentTimeMillis() ? Status.ASSIGNED : Status.FINISHED;
    }

    public String getName() {
        return name;
    }

    public long getDue() {
        return due;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public enum Status {
        ASSIGNED,
        FINISHED
    }
}

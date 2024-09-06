package model;

import model.enums.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Comparable<Task> {

    protected int taskId;
    protected String title;
    protected String description;
    protected StatusOfTask status;
    public final TypeOfTask type = TypeOfTask.TASK;

    private Duration duration;
    private LocalDateTime startTime;
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(String title,
                String description,
                StatusOfTask status,
                LocalDateTime startTime,
                Duration duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String title,
                String description,
                StatusOfTask status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plus(duration);
        } else {
            return null;
        }
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    protected void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    protected void setDuration(Duration duration) {
        this.duration = duration;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public StatusOfTask getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


    @Override
    public int compareTo(Task task) {
        if (this.getStartTime() == null || task.getStartTime() == null) {
            return -1;
        }
        return this.getStartTime().compareTo(task.getStartTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        if (this.taskId == task.taskId) return true;
        return Objects.equals(title, task.title) && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description);
    }

    @Override
    public String toString() {
        String formatedStartTime = "startTime not set";
        String formatedDuration = "---";
        String formatedEndTime = "endTime not set";


        if (getStartTime() != null) {
            formatedStartTime = getStartTime().format(dateFormatter);
        }

        if (getEndTime() != null) {
            formatedEndTime = getEndTime().format(dateFormatter);
        }

        if (getDuration() != null) {
            formatedDuration = String.valueOf(getDuration().toMinutes());
        }

        return taskId + ","
                + type + ","
                + title + ","
                + status + ","
                + description + ","
                + "no epic" + ","
                + formatedStartTime + ","
                + formatedDuration + ","
                + formatedEndTime;
    }
}
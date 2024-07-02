package TaskManager;

import java.util.Objects;

public class Task {

    private int taskId;
    private String title;
    private String description;
    protected StatusOfTask status;


    public Task(String title, String description, StatusOfTask status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public int getTaskId() {
        return taskId;
    }

    protected void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public StatusOfTask getStatus() {
        return status;
    }

    public String getTitle() {
        return title + " - " + description;
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
}
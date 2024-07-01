package TaskManager;

public class Task {

    private static int taskCount = 1;
    private final int taskId;
    private String title;
    private String description;
    protected StatusOfTask status;


    public Task(String title, String description) {
        taskId = taskCount++;
        this.title = title;
        this.description = description;
        this.status = StatusOfTask.NEW;
    }

    public int getTaskId() {
        return taskId;
    }

    void updateStatus(StatusOfTask status) {
        this.status = status;
    }

    public String getTitle() {
        return title + " - " + description;
    }

    public StatusOfTask getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
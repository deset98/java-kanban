package taskmanager;

public class Subtask extends Task {
    private int epicId;
    private final TypeOfTask type = TypeOfTask.SUBTASK;

    public Subtask(int epicId, String title, String description, StatusOfTask status) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return taskId + "," + type + "," + title + "," + status + "," + description + "," + epicId;
    }
}
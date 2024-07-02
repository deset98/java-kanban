package TaskManager;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int epicId, String title, String description, StatusOfTask status) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}

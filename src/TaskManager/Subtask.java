package TaskManager;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int epicId, String title, String description) {
        super(title, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}

package taskmanager;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> epicSubtasks = new ArrayList<>();
    private final TypeOfTask type = TypeOfTask.EPIC;

    public Epic(String title, String description, StatusOfTask status) {
        super(title, description, status);
    }

    void setEpicSubtasks(ArrayList<Subtask> epicSubtasks) {
        this.epicSubtasks = epicSubtasks;
    }

    public ArrayList<Subtask> getEpicSubtasks() {
        return epicSubtasks;
    }

    void setEpicStatus(StatusOfTask status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return taskId + "," + type + "," + title + "," + status + "," + description;
    }
}
package TaskManager;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> epicSubtasks = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    void setEpicSubtasks(ArrayList<Subtask> epicSubtasks) {
        this.epicSubtasks = epicSubtasks;
    }

    public ArrayList<Subtask> getEpicSubtasks() {
        return epicSubtasks;
    }
}
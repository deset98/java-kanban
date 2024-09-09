package model;

import model.enums.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;
    private final TypeOfTask type = TypeOfTask.SUBTASK;

    public Subtask(int epicId,
                   String title,
                   String description,
                   StatusOfTask status,
                   LocalDateTime startTime,
                   Duration duration) {
        super(title, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(int epicId,
                   String title,
                   String description,
                   StatusOfTask status) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
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
                + epicId + ","
                + formatedStartTime + ","
                + formatedDuration + ","
                + formatedEndTime;
    }
}
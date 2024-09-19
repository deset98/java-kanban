package model;

import model.enums.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> epicSubtasks = new ArrayList<>();

    private LocalDateTime endTime;

    public Epic(String title,
                String description,
                StatusOfTask status) {
        super(title, description, status);
        this.type = TypeOfTask.EPIC;
    }


    public void setEpicSubtasks(List<Subtask> epicSubtasks) {
        this.epicSubtasks = epicSubtasks;
    }

    public List<Subtask> getEpicSubtasks() {
        return epicSubtasks;
    }

    public void setEpicStatus(StatusOfTask status) {
        this.status = status;
    }

    public void calculateTimeVariablesForEpic() {
        // находит startTime
        epicSubtasks.stream()
                .min(Task::compareTo)
                .ifPresent(subtask -> setStartTime(subtask.getStartTime()));

        // считает duration
        setDuration(epicSubtasks.stream()
                .map(task -> {
                    if (task.getDuration() == null) {
                        return Duration.ZERO;
                    } else {
                        return task.getDuration();
                    }
                })
                .reduce(Duration.ZERO, Duration::plus));

        // считает endTime
        epicSubtasks.stream()
                .filter(task -> task.getStartTime() != null)
                .max(Task::compareTo)
                .ifPresent(subtask -> endTime = subtask.getEndTime());
    }


    @Override
    public String toString() {
        String formatedStartTime = "startTime not set";
        String formatedDuration = "---";
        String formatedEndTime = "endTime not set";


        if (getStartTime() != null) {
            formatedStartTime = getStartTime().format(dateFormatter);
        }

        if (endTime != null) {
            formatedEndTime = endTime.format(dateFormatter);
        }

        if (getDuration() != null) {
            formatedDuration = String.valueOf(getDuration().toMinutes());
        }

        return taskId + ","
                + type + ","
                + title + ","
                + status + ","
                + description + ","
                + "is epic" + ","
                + formatedStartTime + ","
                + formatedDuration + ","
                + formatedEndTime;
    }
}
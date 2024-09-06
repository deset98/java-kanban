package service;

import exceptions.ManagerSaveException;
import model.*;
import model.enums.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class FileBackedTaskManager extends InMemoryTaskManager {

    File autosave;

    public FileBackedTaskManager(File autosave) {
        this.autosave = autosave;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter(autosave, StandardCharsets.UTF_8)) {
            fileWriter.write("id,type,name,status,description,epic,startTime,duration,endTime\n");
            for (Task task : getAllTasks()) {
                fileWriter.write(task.toString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла");
        }
    }

    private Task fromString(String string) {
        String[] split = string.split(",");

        TypeOfTask type = TypeOfTask.valueOf(split[1]);

        int id = Integer.parseInt(split[0]);
        String title = split[2];
        StatusOfTask status = StatusOfTask.valueOf(split[3]);
        String description = split[4];

        LocalDateTime startTime;
        try {
            startTime = LocalDateTime.parse(split[6], Task.dateFormatter);
        } catch (DateTimeParseException e) {
            startTime = null;
        }

        Duration duration;
        try {
            duration = Duration.ofMinutes(Integer.parseInt(split[7]));
        } catch (NumberFormatException e) {
            duration = null;
        }

        switch (type) {
            case TASK:
                Task task = new Task(title, description, status, startTime, duration);
                task.setTaskId(id);
                tasksMap.put(id, task);
                return task;

            case EPIC:
                Epic epic = new Epic(title, description, status);
                epic.setTaskId(id);
                epicsMap.put(id, epic);
                return epic;

            case SUBTASK:
                int epicId = Integer.parseInt(split[5]);
                Epic epicOfSubtask = epicsMap.get(epicId);

                Subtask subtask = new Subtask(epicId, title, description, status, startTime, duration);
                subtask.setTaskId(id);
                subtasksMap.put(id, subtask);
                epicOfSubtask.getEpicSubtasks().add(subtask);
                epicOfSubtask.calculateTimeVariablesForEpic();
                return subtask;

            default:
                return null;
        }
    }

    public static FileBackedTaskManager loadFromFile(File autosave) {
        String[] split = null;
        try {
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(autosave);
            split = Files.readString(autosave.toPath()).split("\n");

            for (int i = 1; i < split.length; i++) {
                fileBackedTaskManager.fromString(split[i]);
            }
            return fileBackedTaskManager;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }


}
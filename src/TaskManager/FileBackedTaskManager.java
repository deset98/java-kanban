package taskmanager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

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
            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task : getAllTasks()) {
                fileWriter.write(task.toString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла");
        }
    }

    private Task fromString(String string) {
        String[] split = string.split(",");
        Task task = null;

        if (split[1].equals(TypeOfTask.TASK.name())) {
            task = new Task(split[2], split[4], StatusOfTask.valueOf(split[3]));
            task.setTaskId(Integer.parseInt(split[0]));
            tasksMap.put(task.getTaskId(), task);
        } else if (split[1].equals("EPIC")) {
            task = new Epic(split[2], split[4], StatusOfTask.valueOf(split[3]));
            task.setTaskId(Integer.parseInt(split[0]));
            epicsMap.put(task.getTaskId(), (Epic) task);
        } else if (split[1].equals("SUBTASK")) {
            task = new Subtask(Integer.parseInt(split[5].replaceAll("[^*0-9]", "")), split[2],
                    split[4], StatusOfTask.valueOf(split[3]));
            task.setTaskId(Integer.parseInt(split[0]));
            subtasksMap.put(task.getTaskId(), (Subtask) task);
        }
        return task;
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
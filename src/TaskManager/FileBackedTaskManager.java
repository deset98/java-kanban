package taskmanager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {

    File autosave;

    public FileBackedTaskManager(File autosave) {
        this.autosave = autosave;
        this.loadFromFile(autosave);
    }

    @Override
    public Task getTaskById(int id) {
        history.add(tasksMap.get(id));
        save();
        return tasksMap.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        history.add(subtasksMap.get(id));
        save();
        return subtasksMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        history.add(epicsMap.get(id));
        save();
        return epicsMap.get(id);
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
            for (Task task : getHistory()) {
                fileWriter.write(task.toString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
    }

    private Task fromString(String string) {
        String[] split = string.split(",");
        Task task = null;


        if (split[1].equals("TASK")) {
            task = new Task(split[2], split[4], StatusOfTask.valueOf(split[3]));
            task.setTaskId(Integer.parseInt(split[0]));
        } else if (split[1].equals("EPIC")) {
            task = new Epic(split[2], split[4], StatusOfTask.valueOf(split[3]));
            task.setTaskId(Integer.parseInt(split[0]));
        } else if (split[1].equals("SUBTASK")) {
            task = new Subtask(Integer.parseInt(split[5]), split[2], split[4], StatusOfTask.valueOf(split[3]));
            task.setTaskId(Integer.parseInt(split[0]));
        }
        return task;
    }

    private void loadFromFile(File autosave) {
        String[] split = null;
        try {
            split = Files.readString(autosave.toPath()).split("\n");
            if (split.length == 1) {  // завершает метод, если передан пустой файл
                return;
            }

            for (String line : split) {
                history.add(fromString(line));
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла...");
        }
    }
}
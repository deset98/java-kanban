package TaskManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    private int nextTaskId = 1;

    private HashMap<Integer, Task> tasksMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtasksMap = new HashMap<>();
    private HashMap<Integer, Epic> epicsMap = new HashMap<>();


    public void addTask(Task task) {
        task.setTaskId(nextTaskId);
        tasksMap.put(nextTaskId, task);
        nextTaskId++;
    }

    public void addEpic(Epic epic) {
        epic.setTaskId(nextTaskId);
        epicsMap.put(nextTaskId, epic);
        nextTaskId++;
    }

    public void addSubtask(Subtask subtask) {
        subtask.setTaskId(nextTaskId);
        subtasksMap.put(nextTaskId, subtask);
        nextTaskId++;

        Epic epic = epicsMap.get(subtask.getEpicId());
        ArrayList<Subtask> subtasks = epic.getEpicSubtasks();
        subtasks.add(subtask);

        epic.setEpicStatus(calculateEpicStatus(subtask.getEpicId()));

    }


    public Task getTaskById(int id) {
        return tasksMap.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasksMap.get(id);
    }

    public Epic getEpicById(int id) {
        return epicsMap.get(id);
    }


    public void removeTaskById(int id) {
        tasksMap.remove(id);
    }

    public void removeSubtaskById(int id) {
        Subtask subtask = subtasksMap.get(id);
        Epic epic = epicsMap.get(subtask.getEpicId());
        subtasksMap.remove(id);
        ArrayList<Subtask> subtasks = epic.getEpicSubtasks();
        subtasks.remove(subtask);
        epic.setEpicStatus(calculateEpicStatus(subtask.getEpicId()));
    }

    public void removeEpicById(int id) {
        Epic epic = epicsMap.get(id);
        ArrayList<Subtask> subtasks = epic.getEpicSubtasks();
        for (Subtask subtask : subtasks) {
            subtasksMap.remove(subtask.getTaskId());
        }
        epicsMap.remove(id);
    }


    public void clearTasks() {
        tasksMap.clear();
    }

    public void clearSubtasks() {
        ArrayList<Epic> epics = new ArrayList<>();
        for (Subtask subtask : subtasksMap.values()) {
            Epic epic = epicsMap.get(subtask.getEpicId());
            if (!epics.contains(epic)) {
                epics.add(epic);
            }
        }

        for (Epic epic : epics) {
            ArrayList<Subtask> subtasks = epic.getEpicSubtasks();
            subtasks.clear();
            epic.setEpicStatus(calculateEpicStatus(epic.getTaskId()));
        }
        subtasksMap.clear();

    }

    public void clearEpics() {
        epicsMap.clear();
        subtasksMap.clear();
    }


    public ArrayList<Subtask> getEpicSubtasksList(int id) {
        Epic epic = epicsMap.get(id);
        return epic.getEpicSubtasks();
    }


    public void updateTask(Task task) {
        tasksMap.put(task.getTaskId(), task);
    }

    public void updateEpic(Epic epic) {

        Epic oldEpic = epicsMap.get(epic.getTaskId());
        epic.setEpicSubtasks(oldEpic.getEpicSubtasks());

        epicsMap.put(epic.getTaskId(), epic);
        epic.setEpicStatus(calculateEpicStatus(epic.getTaskId()));
    }

    public void updateSubtask(Subtask subtask) {

        subtasksMap.put(subtask.getTaskId(), subtask);

        Epic epic = epicsMap.get(subtask.getEpicId());
        epic.getEpicSubtasks().add(subtask);
        epic.setEpicStatus(calculateEpicStatus(subtask.getEpicId()));
    }


    private StatusOfTask calculateEpicStatus(int epicId) {

        Epic epic = epicsMap.get(epicId);
        ArrayList<Subtask> epicSubtasks = epic.getEpicSubtasks();

        if (epicSubtasks.isEmpty()) {
            return StatusOfTask.NEW;
        }

        boolean containsNew = false;
        boolean containsDone = false;
        for (Subtask epicSubtask : epicSubtasks) {
            StatusOfTask statusOfSubtask = epicSubtask.getStatus();

            if (statusOfSubtask == StatusOfTask.NEW) {
                containsNew = true;
            } else if (statusOfSubtask == StatusOfTask.DONE) {
                containsDone = true;
            } else if (statusOfSubtask == StatusOfTask.IN_PROGRESS) {
                return StatusOfTask.IN_PROGRESS;
            } else if (containsNew && containsDone) {
                return StatusOfTask.IN_PROGRESS;
            }
        }

        if (containsNew) {
            return StatusOfTask.NEW;
        }

        return StatusOfTask.DONE;
    }
}

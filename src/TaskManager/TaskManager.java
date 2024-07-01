package TaskManager;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Task> tasksMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtasksMap = new HashMap<>();
    private HashMap<Integer, Epic> epicsMap = new HashMap<>();


    public void addTask(Task task) {
        tasksMap.put(task.getTaskId(), task);
    }

    public void addTask(Epic epic) {
        epicsMap.put(epic.getTaskId(), epic);
    }

    public void addTask(Subtask subtask) {
        subtasksMap.put(subtask.getTaskId(), subtask);

        Epic epic = epicsMap.get(subtask.getEpicId());
        ArrayList<Subtask> subtasks = epic.getEpicSubtasks();
        subtasks.add(subtask);

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
        epic.updateStatus(epicCompleted(subtask.getEpicId()));
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
        subtasksMap.clear();
    }

    public void clearEpics() {
        epicsMap.clear();
    }


    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(tasksMap.values());
    }

    public ArrayList<Subtask> getSubtasksList() {
        return new ArrayList<>(subtasksMap.values());
    }

    public ArrayList<Epic> getEpicsList() {
        return new ArrayList<>(epicsMap.values());
    }


    public ArrayList<Subtask> getEpicSubtasksList(int id) {
        Epic epic = epicsMap.get(id);
        return epic.getEpicSubtasks();
    }


    public void updateTask(Task updTask, Task oldTask) {
        updTask.updateStatus(StatusOfTask.IN_PROGRESS);
        tasksMap.put(oldTask.getTaskId(), updTask);
    }

    public void updateEpic(Epic updEpic, Epic oldEpic) {
        ArrayList<Subtask> oldEpicSubtasks = oldEpic.getEpicSubtasks();
        updEpic.setEpicSubtasks(oldEpicSubtasks);

        updEpic.updateStatus(StatusOfTask.IN_PROGRESS);
        epicsMap.put(oldEpic.getTaskId(), updEpic);
    }

    public void updateSubtask(Subtask updSubtask, Subtask oldSubtask) {
        subtasksMap.put(oldSubtask.getTaskId(), updSubtask);

        Epic epic = epicsMap.get(updSubtask.getEpicId());
        ArrayList<Subtask> subtasks = epic.getEpicSubtasks();
        subtasks.remove(oldSubtask);
        subtasks.add(updSubtask);
        updSubtask.updateStatus(StatusOfTask.IN_PROGRESS);
        epic.updateStatus(epicCompleted(updSubtask.getEpicId()));
    }

    private StatusOfTask epicCompleted(int epicId) {

        Epic epic = epicsMap.get(epicId);
        ArrayList<Subtask> epicSubtasks = epic.getEpicSubtasks();

        for (Subtask epicSubtask : epicSubtasks) {
            StatusOfTask status = epicSubtask.getStatus();
            if (!(status.equals(StatusOfTask.DONE))) {
                return StatusOfTask.IN_PROGRESS;
            }
        }
        return StatusOfTask.DONE;
    }


    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epicsMap.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasksMap.values());
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasksMap.values());
    }
}
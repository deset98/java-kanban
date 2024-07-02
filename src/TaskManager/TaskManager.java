package TaskManager;

import java.util.ArrayList;
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


    public void updateTask(Task task) {                // Привет! Если по ТЗ нужно ID присваивать через TaskManager,
        int oldTaskId = 0;                             // то это делается в add-методе. Следовательно, если передать в
        for (Task oldTask : tasksMap.values()) {       // update() объект Task, который еще не был в add(), то у его ID
            if (oldTask.equals(task)) {                // будет установлено значение по-умолчанию taskId = 0;
                removeTaskById(oldTask.getTaskId());   // Решил что можно через equals() найти старую задачу в мапе,
                oldTaskId = oldTask.getTaskId();       // удалить ее и далее присвоить новой задаче ID от старой.
                break;                                 // Либо можно через TaskManager присвоить другой ID (отличный от старого).
            }                                          // И уже по этому присвоенному ID добавить в мапу.
        }                                              // Только работает все это пока поля title и description равны в
                                                       // и в новой и в старой задаче...
        task.setTaskId(oldTaskId);
        tasksMap.put(task.getTaskId(), task);
    }

    public void updateEpic(Epic epic) {

        int oldEpicId = 0;
        ArrayList<Subtask> subtasks = epic.getEpicSubtasks();
        for (Epic oldEpic : epicsMap.values()) {
            if (oldEpic.equals(epic)) {
                subtasks = oldEpic.getEpicSubtasks();
                epicsMap.remove(oldEpic.getTaskId());
                oldEpicId = oldEpic.getTaskId();
                break;
            }
        }

        epic.setTaskId(oldEpicId);
        epic.setEpicSubtasks(subtasks);
        epicsMap.put(epic.getTaskId(), epic);
        epic.setEpicStatus(calculateEpicStatus(epic.getTaskId()));
    }

    public void updateSubtask(Subtask subtask) {

        int oldSubtaskId = 0;
        for (Task oldSubtask : subtasksMap.values()) {
            if (oldSubtask.equals(subtask)) {
                removeSubtaskById(oldSubtask.getTaskId());
                oldSubtaskId = oldSubtask.getTaskId();
                break;
            }
        }

        subtask.setTaskId(oldSubtaskId);
        subtasksMap.put(subtask.getTaskId(), subtask);

        Epic epic = epicsMap.get(subtask.getEpicId());
        epic.setEpicStatus(calculateEpicStatus(subtask.getEpicId()));
    }


    private StatusOfTask calculateEpicStatus(int epicId) {

        Epic epic = epicsMap.get(epicId);
        ArrayList<Subtask> epicSubtasks = epic.getEpicSubtasks();

        if (epicSubtasks.isEmpty()) {
            return StatusOfTask.NEW;
        }

        boolean statusNew = false;
        for (Subtask epicSubtask : epicSubtasks) {
            StatusOfTask status = epicSubtask.getStatus();
            if (status == StatusOfTask.NEW) {
                statusNew = true;
            } else {
                statusNew = false;
                break;
            }
        }

        if (statusNew) {
            return StatusOfTask.NEW;
        }

        boolean statusDone = false;
        for (Subtask epicSubtask : epicSubtasks) {
            StatusOfTask status = epicSubtask.getStatus();
            if (status == StatusOfTask.DONE) {
                statusDone = true;
            } else {
                statusDone = false;
                break;
            }
        }

        if (statusDone) {
            return StatusOfTask.DONE;
        }

        return StatusOfTask.IN_PROGRESS;
    }
}
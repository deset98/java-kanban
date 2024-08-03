package taskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {
    private int nextTaskId = 1;

    private final HashMap<Integer, Task> tasksMap;
    private final HashMap<Integer, Subtask> subtasksMap;
    private final HashMap<Integer, Epic> epicsMap;

    private final HistoryManager history;


    public InMemoryTaskManager() {
        this.tasksMap = new HashMap<>();
        this.subtasksMap = new HashMap<>();
        this.epicsMap = new HashMap<>();

        this.history = Managers.getDefaultHistoryManager();
    }


    @Override
    public void addTask(Task task) {
        task.setTaskId(nextTaskId);
        tasksMap.put(nextTaskId, task);
        nextTaskId++;
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setTaskId(nextTaskId);
        epicsMap.put(nextTaskId, epic);
        nextTaskId++;
    }

    @Override
    public void addSubtask(Subtask subtask) {

        subtask.setTaskId(nextTaskId);

        subtasksMap.put(nextTaskId, subtask);
        nextTaskId++;

        Epic epic = epicsMap.get(subtask.getEpicId());
        ArrayList<Subtask> subtasks = epic.getEpicSubtasks();
        subtasks.add(subtask);

        epic.setEpicStatus(calculateEpicStatus(subtask.getEpicId()));

    }


    @Override
    public Task getTaskById(int id) {
        history.add(tasksMap.get(id));
        return tasksMap.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        history.add(subtasksMap.get(id));
        return subtasksMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        history.add(epicsMap.get(id));
        return epicsMap.get(id);
    }

    @Override
    public void removeTaskById(int id) {
        tasksMap.remove(id);
        history.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask subtask = subtasksMap.get(id);
        Epic epic = epicsMap.get(subtask.getEpicId());
        subtasksMap.remove(id);
        ArrayList<Subtask> subtasks = epic.getEpicSubtasks();
        subtasks.remove(subtask);
        epic.setEpicStatus(calculateEpicStatus(subtask.getEpicId()));
        history.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epicsMap.get(id);
        ArrayList<Subtask> subtasks = epic.getEpicSubtasks();
        for (Subtask subtask : subtasks) {
            subtasksMap.remove(subtask.getTaskId());
            history.remove(subtask.getTaskId());
        }
        epicsMap.remove(id);
        history.remove(id);
    }


    @Override
    public void clearTasks() {
        for (Task task : tasksMap.values()) {
            history.remove(task.getTaskId());
        }
        tasksMap.clear();

    }

    @Override
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

        for (Subtask subtask : subtasksMap.values()) {
            history.remove(subtask.getTaskId());
        }
        subtasksMap.clear();
    }

    @Override
    public void clearEpics() {
        for (Epic epic : epicsMap.values()) {
            history.remove(epic.getTaskId());
        }
        epicsMap.clear();

        for (Subtask subtask : subtasksMap.values()) {
            history.remove(subtask.getTaskId());
        }
        subtasksMap.clear();
    }


    @Override
    public ArrayList<Subtask> getEpicSubtasks(int id) {
        Epic epic = epicsMap.get(id);
        return epic.getEpicSubtasks();
    }


    @Override
    public void updateTask(Task task) {
        tasksMap.put(task.getTaskId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {

        Epic oldEpic = epicsMap.get(epic.getTaskId());
        epic.setEpicSubtasks(oldEpic.getEpicSubtasks());

        epicsMap.put(epic.getTaskId(), epic);
        epic.setEpicStatus(calculateEpicStatus(epic.getTaskId()));
    }

    @Override
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
            }

            if (containsNew && containsDone) {
                return StatusOfTask.IN_PROGRESS;
            }
        }

        if (containsNew) {
            return StatusOfTask.NEW;
        }
        return StatusOfTask.DONE;
    }

    public List<Task> getHistory() {
        return history.getHistory();
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<Task>(tasksMap.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<Subtask>(subtasksMap.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<Epic>(epicsMap.values());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return nextTaskId == that.nextTaskId && Objects.equals(tasksMap, that.tasksMap) && Objects.equals(subtasksMap, that.subtasksMap) && Objects.equals(epicsMap, that.epicsMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nextTaskId, tasksMap, subtasksMap, epicsMap);
    }
}
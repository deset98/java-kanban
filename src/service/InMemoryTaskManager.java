package service;

import model.*;
import model.enums.*;
import util.*;
import service.interfaces.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class InMemoryTaskManager implements TaskManager {
    private int nextTaskId = 1;

    protected final HashMap<Integer, Task> tasksMap;
    protected final HashMap<Integer, Subtask> subtasksMap;
    protected final HashMap<Integer, Epic> epicsMap;

    private final TreeSet<Task> prioritizedTasks;
    protected final HistoryManager history;


    public InMemoryTaskManager() {
        this.tasksMap = new HashMap<>();
        this.subtasksMap = new HashMap<>();
        this.epicsMap = new HashMap<>();
        this.prioritizedTasks = new TreeSet<>();

        this.history = Managers.getDefaultHistoryManager();
    }


    @Override
    public void addTask(Task task) {

        Consumer<Task> addTaskFunction = someTask -> {
            someTask.setTaskId(nextTaskId);
            tasksMap.put(nextTaskId, someTask);
            nextTaskId++;
        };

        if (task.getStartTime() == null) {
            addTaskFunction.accept(task);
        } else if (checkOfAbsenceTimeIntersection(task)) {
            addTaskFunction.accept(task);
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setTaskId(nextTaskId);
        epicsMap.put(nextTaskId, epic);
        nextTaskId++;
    }

    @Override
    public void addSubtask(Subtask subtask) {

        Consumer<Subtask> addSubtaskFunction = someSubtask -> {
            someSubtask.setTaskId(nextTaskId);
            subtasksMap.put(nextTaskId, someSubtask);
            nextTaskId++;

            Epic epic = epicsMap.get(someSubtask.getEpicId());
            List<Subtask> subtasks = epic.getEpicSubtasks();
            subtasks.add(someSubtask);

            epic.setEpicStatus(calculateEpicStatus(someSubtask.getEpicId()));
            epic.calculateTimeVariablesForEpic();
        };

        if (subtask.getStartTime() == null) {
            addSubtaskFunction.accept(subtask);
        } else if (checkOfAbsenceTimeIntersection(subtask)) {
            addSubtaskFunction.accept(subtask);
            prioritizedTasks.add(subtask);
        }
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
        prioritizedTasks.remove(tasksMap.get(id));
        tasksMap.remove(id);
        history.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask subtask = subtasksMap.get(id);
        Epic epic = epicsMap.get(subtask.getEpicId());
        subtasksMap.remove(id);
        List<Subtask> subtasks = epic.getEpicSubtasks();
        subtasks.remove(subtask);
        epic.setEpicStatus(calculateEpicStatus(subtask.getEpicId()));
        history.remove(id);
        prioritizedTasks.remove(subtask);
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epicsMap.get(id);

        epic.getEpicSubtasks().forEach(subtask -> {
            subtasksMap.remove(subtask.getTaskId());
            history.remove(subtask.getTaskId());
        });
        epicsMap.remove(id);
        history.remove(id);
    }


    @Override
    public void clearTasks() {
        List<Integer> listOfId = new ArrayList<>(tasksMap.keySet());
        listOfId.forEach(this::removeTaskById);
    }

    @Override
    public void clearSubtasks() {
        List<Integer> listOfId = new ArrayList<>(subtasksMap.keySet());
        listOfId.forEach(this::removeSubtaskById);
    }

    @Override
    public void clearEpics() {
        epicsMap.keySet().forEach(history::remove);
        epicsMap.clear();

        subtasksMap.keySet().forEach(history::remove);
        subtasksMap.clear();
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
        epic.calculateTimeVariablesForEpic();
    }

    @Override
    public void updateSubtask(Subtask subtask) {

        subtasksMap.put(subtask.getTaskId(), subtask);

        Epic epic = epicsMap.get(subtask.getEpicId());
        epic.getEpicSubtasks().add(subtask);
        epic.setEpicStatus(calculateEpicStatus(subtask.getEpicId()));
        epic.calculateTimeVariablesForEpic();
    }


    public List<Task> getHistory() {
        return history.getHistory();
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasksMap.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasksMap.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epicsMap.values());
    }

    @Override
    public List<Subtask> getEpicSubtasks(int id) {
        Epic epic = epicsMap.get(id);
        return epic.getEpicSubtasks();
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasksMap.values());
        allTasks.addAll(epicsMap.values());
        allTasks.addAll(subtasksMap.values());
        return allTasks;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    @Override
    public boolean checkOfAbsenceTimeIntersection(Task t1) {
        if (prioritizedTasks.isEmpty()) {
            return true;
        }

        Predicate<Task> startTimeFinder = t2 -> t2.getEndTime().isBefore(t1.getStartTime());
        Predicate<Task> endTimeFinder = t3 -> t3.getStartTime().isAfter(t1.getEndTime());
        Predicate<Task> isBeforeFirst = t2 -> t2.equals(prioritizedTasks.first()) && t2.getStartTime().isAfter(t1.getEndTime());
        Predicate<Task> isAfterLast = t2 -> t2.equals(prioritizedTasks.last()) && t2.getEndTime().isBefore(t1.getStartTime());

        return prioritizedTasks.stream()
                .anyMatch(t2 -> {
                    if (isBeforeFirst.test(t2) || isAfterLast.test(t2)) {
                        return true;
                    } else if (startTimeFinder.test(t2) && endTimeFinder.test(prioritizedTasks.higher(t2))) {
                        return true;
                    } else {
                        return false;
                    }
                });
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

    private StatusOfTask calculateEpicStatus(int epicId) {

        Epic epic = epicsMap.get(epicId);
        List<Subtask> epicSubtasks = epic.getEpicSubtasks();

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
}
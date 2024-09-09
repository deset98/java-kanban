package service.interfaces;

import model.*;

import java.util.List;

public interface TaskManager {


    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);


    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);


    void removeTaskById(int id);

    void removeSubtaskById(int id);

    void removeEpicById(int id);


    void clearTasks();

    void clearSubtasks();

    void clearEpics();


    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);


    List<Task> getTasks();

    List<Subtask> getSubtasks();

    List<Epic> getEpics();

    List<Subtask> getEpicSubtasks(int id);

    List<Task> getAllTasks();

    List<Task> getHistory();


    List<Task> getPrioritizedTasks();

    boolean checkOfAbsenceTimeIntersection(Task task);
}
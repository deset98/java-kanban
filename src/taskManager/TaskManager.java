package taskManager;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {


    public void addTask(Task task);

    public void addEpic(Epic epic);

    public void addSubtask(Subtask subtask);


    public Task getTaskById(int id);

    public Subtask getSubtaskById(int id);

    public Epic getEpicById(int id);


    public void removeTaskById(int id);

    public void removeSubtaskById(int id);

    public void removeEpicById(int id);


    public void clearTasks();

    public void clearSubtasks();

    public void clearEpics();

    public ArrayList<Subtask> getEpicSubtasks(int id);


    public void updateTask(Task task);

    public void updateEpic(Epic epic);

    public void updateSubtask(Subtask subtask);

    public ArrayList<Task> getTasks();

    public ArrayList<Subtask> getSubtasks();

    public ArrayList<Epic> getEpics();

    public List<Task> getHistory();
}
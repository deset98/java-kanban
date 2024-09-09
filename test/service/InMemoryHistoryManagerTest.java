package service;

import service.interfaces.*;
import model.*;
import model.enums.*;
import util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    TaskManager taskManager;
    HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefaultTaskManager();
        historyManager = Managers.getDefaultHistoryManager();
    }

    @Test
    public void historyManagerShouldReturnTaskAfterUpdate() {
        // проверка на сохранение версии задачи в истории
        Task firstTask = new Task("Задача", "Описание задачи", StatusOfTask.NEW);
        taskManager.addTask(firstTask);
        taskManager.getTaskById(firstTask.getTaskId());
        taskManager.updateTask(new Task("updЗадача", "updОписание",
                StatusOfTask.IN_PROGRESS));
        List<Task> history = taskManager.getHistory();
        Task updTask = history.get(0);
        assertEquals(firstTask, updTask, "Старая версия задачи не сохранена");
    }

    @Test
    public void historyManagerShouldReturnEpicAfterUpdate() {
        // проверка на сохранение версии эпика в истории
        Epic firstEpic = new Epic("Эпик", "Описание эпика", StatusOfTask.NEW);
        taskManager.addEpic(firstEpic);
        taskManager.getEpicById(firstEpic.getTaskId());
        Epic secondEpic = new Epic("Эпик", "Описание эпика", StatusOfTask.NEW);
        secondEpic.setTaskId(firstEpic.getTaskId());
        taskManager.updateEpic(secondEpic);
        List<Task> history = taskManager.getHistory();
        Task updEpic = history.get(0);
        assertEquals(firstEpic, updEpic, "Старая версия эпика не сохранена");
    }

    @Test
    public void historyManagerShouldReturnSubtaskAfterUpdate() {
        // проверка на сохранение версии подзадачи в истории
        Epic epic = new Epic("Эпик 1", "Помыть посуду", StatusOfTask.NEW);
        taskManager.addEpic(epic);
        Subtask firstSubtask = new Subtask(1, "Подзадача", "Описание подзадачи", StatusOfTask.NEW,
                LocalDateTime.parse("09.10.2020 10:00", Task.dateFormatter), Duration.ofMinutes(100));
        taskManager.addSubtask(firstSubtask);
        taskManager.getSubtaskById(firstSubtask.getTaskId());
        taskManager.updateSubtask(new Subtask(1, "updЭпик", "updОписание эпика",
                StatusOfTask.IN_PROGRESS, LocalDateTime.parse("09.10.2020 10:00", Task.dateFormatter),
                Duration.ofMinutes(100)));
        List<Task> history = taskManager.getHistory();
        Task updSubtask = history.get(0);
        assertEquals(firstSubtask, updSubtask, "Старая версия подзадачи не сохранена");
    }

    @Test
    public void historyManagerShouldNotDublicateTasks() {
        Task task1 = new Task("Задача", "Описание задачи", StatusOfTask.NEW);
        taskManager.addTask(task1);
        taskManager.getTaskById(task1.getTaskId());
        taskManager.getTaskById(task1.getTaskId());
        Task task2 = new Task("Задача", "Описание задачи", StatusOfTask.NEW);
        taskManager.addTask(task2);
        taskManager.getTaskById(task2.getTaskId());
        taskManager.getTaskById(task2.getTaskId());

        assertEquals(2, taskManager.getHistory().size(), "История дублирует задачи");
    }

    @Test
    public void historyManagerShouldRemoveAllTasks() {
        Task task1 = new Task("Задача", "Описание задачи", StatusOfTask.NEW);
        taskManager.addTask(task1);
        taskManager.getTaskById(task1.getTaskId());
        Task task2 = new Task("Задача", "Описание задачи", StatusOfTask.NEW);
        taskManager.addTask(task2);
        taskManager.getTaskById(task2.getTaskId());


        taskManager.removeTaskById(task1.getTaskId());
        taskManager.removeTaskById(task2.getTaskId());

        assertTrue(taskManager.getHistory().isEmpty(), "История не очищается");

    }

    @Test
    public void mustRemoveTaskFromHeadOfHistory() {
        // Удаление просмотра с начала
        Task task1 = new Task("Task #1", "Description #1", StatusOfTask.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Task #2", "Description #2", StatusOfTask.NEW);
        taskManager.addTask(task2);
        Task task3 = new Task("Task #3", "Description #3", StatusOfTask.NEW);
        taskManager.addTask(task3);

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);

        taskManager.removeTaskById(1);

        assertEquals(taskManager.getHistory().getFirst(), task2);

    }

    @Test
    public void mustRemoveTaskFromTailOfHistory() {
        // Удаление просмотра с конца
        Task task1 = new Task("Task #1", "Description #1", StatusOfTask.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Task #2", "Description #2", StatusOfTask.NEW);
        taskManager.addTask(task2);
        Task task3 = new Task("Task #3", "Description #3", StatusOfTask.NEW);
        taskManager.addTask(task3);

        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        taskManager.getTaskById(1);

        taskManager.removeTaskById(1);

        assertEquals(taskManager.getHistory().getLast(), task3);
    }

    @Test
    public void mustRemoveTaskFromMiddleOfHistory() {
        // Удаление просмотра с конца
        Task task1 = new Task("Task #1", "Description #1", StatusOfTask.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Task #2", "Description #2", StatusOfTask.NEW);
        taskManager.addTask(task2);
        Task task3 = new Task("Task #3", "Description #3", StatusOfTask.NEW);
        taskManager.addTask(task3);

        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        taskManager.getTaskById(1);

        taskManager.removeTaskById(3);

        assertNotEquals(taskManager.getHistory().get(1), task3);
        assertEquals(taskManager.getHistory().size(), 2);
    }

    @Test
    public void mustGetsEmptyHistory() {
        assertTrue(taskManager.getHistory().isEmpty() && taskManager.getHistory() != null);
    }
}
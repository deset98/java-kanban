package taskManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        taskManager.updateTask(new Task("updЗадача","updОписание",
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
        Subtask firstSubtask = new Subtask(1,"Подзадача", "Описание подзадачи", StatusOfTask.NEW);
        taskManager.addSubtask(firstSubtask);
        taskManager.getSubtaskById(firstSubtask.getTaskId());
        taskManager.updateSubtask(new Subtask(1,"updЭпик","updОписание эпика",
                StatusOfTask.IN_PROGRESS));
        List<Task> history = taskManager.getHistory();
        Task updSubtask = history.get(0);
        assertEquals(firstSubtask, updSubtask, "Старая версия подзадачи не сохранена");
    }


    @Test
    public void returnListOfHistoryWithOnly10Tasks() {

        for (int i = 0; i < 15; i++) {
            Task task = new Task("Задача", "Описание", StatusOfTask.NEW);
            taskManager.addTask(task);
            taskManager.getTaskById(task.getTaskId());
        }
        assertEquals(10, taskManager.getHistory().size(), "Кол-во задач в истории отличается от 10");
    }
}
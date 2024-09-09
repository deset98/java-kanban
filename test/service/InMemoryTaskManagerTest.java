package service;

import model.*;
import model.enums.*;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends AbstractTaskManagerTest<InMemoryTaskManager> {

    @Test
    void addNewTaskAndFindItByNumber() {
        //проверка создания и поиска задачи по id;
        Task task = new Task("Задача", "Описание задачи", StatusOfTask.NEW);
        taskManager.addTask(task);
        Task searchingTask = taskManager.getTaskById(task.getTaskId());
        assertNotNull(searchingTask);
        assertEquals(task, searchingTask, "Задачи не эквивалентны");
    }

    @Test
    void addNewEpicAndFindItByNumber() {
        //проверка создания и поиска эпика по id;
        Epic epic = new Epic("Эпик", "Описание эпика", StatusOfTask.NEW);
        taskManager.addEpic(epic);
        Epic searchingEpic = taskManager.getEpicById(epic.getTaskId());
        assertNotNull(searchingEpic);
        assertEquals(epic, searchingEpic, "Эпики не эквивалентны");
    }

    @Test
    void addNewSubtaskAndFindItByNumber() {
        //проверка создания и поиска эпика по id;
        Epic epic = new Epic("Эпик", "Описание эпика", StatusOfTask.NEW);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask(1, "Подзадача", "Описание подзадачи", StatusOfTask.NEW);
        taskManager.addSubtask(subtask);
        Subtask searchingSubtask = taskManager.getSubtaskById(subtask.getTaskId());
        assertNotNull(searchingSubtask);
        assertEquals(subtask, searchingSubtask, "Эпики не эквивалентны");
    }


    @Test
    void taskShouldBeKeepTheirValuesOfFieldsAfterAddInTaskManager() {
        // проверка неизменности полей при добавлении задачи Task
        Task task = new Task("Задача 1", "Помыть посуду", StatusOfTask.NEW);
        String titleBeforeAdd = task.getTitle();
        String descriptionBeforeAdd = task.getDescription();
        StatusOfTask statusBeforeAdd = task.getStatus();

        taskManager.addTask(task);
        Task taskAfterAdd = taskManager.getTaskById(1);

        assertEquals(taskAfterAdd.getTitle(), titleBeforeAdd, "Поля 'title' не равны");
        assertEquals(taskAfterAdd.getDescription(), descriptionBeforeAdd, "Поля 'description' не равны");
        assertEquals(taskAfterAdd.getStatus(), statusBeforeAdd, "Поля 'status' не равны");
    }

    @Test
    void subtaskShouldBeKeepTheirValuesOfFieldsAfterAddInTaskManager() {
        // проверка неизменности полей при добавлении задачи Subtask
        Epic epic = new Epic("Эпик 1", "Помыть посуду", StatusOfTask.NEW);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask(1, "Подзадача 1", "Помыть посуду", StatusOfTask.NEW);
        int epicIdBeforeAdd = subtask.getEpicId();
        String titleBeforeAdd = subtask.getTitle();
        String descriptionBeforeAdd = subtask.getDescription();
        StatusOfTask statusBeforeAdd = subtask.getStatus();

        taskManager.addSubtask(subtask);
        Subtask subtaskAfterAdd = taskManager.getSubtaskById(2);

        assertEquals(subtaskAfterAdd.getEpicId(), epicIdBeforeAdd, "Поля 'epicId' не равны");
        assertEquals(subtaskAfterAdd.getTitle(), titleBeforeAdd, "Поля 'title' не равны");
        assertEquals(subtaskAfterAdd.getDescription(), descriptionBeforeAdd, "Поля 'description' не равны");
        assertEquals(subtaskAfterAdd.getStatus(), statusBeforeAdd, "Поля 'status' не равны");
    }

    @Test
    void epicShouldBeKeepTheirValuesOfFieldsAfterAddInTaskManager() {
        // проверка неизменности полей при добавлении задачи Epic
        Epic epic = new Epic("Эпик 1", "Помыть посуду", StatusOfTask.NEW);
        String titleBeforeAdd = epic.getTitle();
        String descriptionBeforeAdd = epic.getDescription();
        StatusOfTask statusBeforeAdd = epic.getStatus();
        List<Subtask> epicSubtasksBeforeAdd = epic.getEpicSubtasks();

        taskManager.addEpic(epic);
        Epic epicAfterAdd = taskManager.getEpicById(1);

        assertEquals(epicAfterAdd.getTitle(), titleBeforeAdd, "Поля 'title' не равны");
        assertEquals(epicAfterAdd.getDescription(), descriptionBeforeAdd, "Поля 'description' не равны");
        assertEquals(epicAfterAdd.getStatus(), statusBeforeAdd, "Поля 'status' не равны");
        assertEquals(epicAfterAdd.getEpicSubtasks(), epicSubtasksBeforeAdd, "Поля 'epicSubtasks' не равны");
    }


    @Test
    void taskShouldBeEqualsWithAddIdAndTaskWithSetId() {
        //проверка задач с заданным id и сгенерированным id
        Task task1 = new Task("Задача 1", "Описание задачи 1", StatusOfTask.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", StatusOfTask.NEW);

        taskManager.addTask(task1);
        task2.setTaskId(1);

        assertEquals(task1, task2, "Задачи не равны");
    }

    @Test
    void mustCorrectlyCalculateTheStatusOfTheEpicIfAllSubtasksAreNEW() {
        //Все подзадачи со статусом NEW и расчет статуса Эпика
        taskManager.addEpic(new Epic("Epic #1", "Description of Epic", StatusOfTask.NEW));
        taskManager.addSubtask(new Subtask(1, "Subtask #1", "Description #1", StatusOfTask.NEW));
        taskManager.addSubtask(new Subtask(1, "Subtask #2", "Description #2", StatusOfTask.NEW));
        taskManager.addSubtask(new Subtask(1, "Subtask #3", "Description #3", StatusOfTask.NEW));

        Epic epic = taskManager.getEpicById(1);
        assertEquals(epic.getStatus(), StatusOfTask.NEW);
    }

    @Test
    void mustCorrectlyCalculateTheStatusOfTheEpicIfAllSubtasksAreDONE() {
        //Все подзадачи со статусом DONE и расчет статуса Эпика
        taskManager.addEpic(new Epic("Epic #1", "Description of Epic", StatusOfTask.NEW));
        taskManager.addSubtask(new Subtask(1, "Subtask #1", "Description #1", StatusOfTask.DONE));
        taskManager.addSubtask(new Subtask(1, "Subtask #2", "Description #2", StatusOfTask.DONE));
        taskManager.addSubtask(new Subtask(1, "Subtask #3", "Description #3", StatusOfTask.DONE));

        Epic epic = taskManager.getEpicById(1);
        assertEquals(epic.getStatus(), StatusOfTask.DONE);
    }

    @Test
    void mustCorrectlyCalculateTheStatusOfTheEpicIfSubtasksAreNEWorDONE() {
        //Подзадачи со статусом NEW и DONE и расчет статуса Эпика
        taskManager.addEpic(new Epic("Epic #1", "Description of Epic", StatusOfTask.NEW));
        taskManager.addSubtask(new Subtask(1, "Subtask #1", "Description #1", StatusOfTask.NEW));
        taskManager.addSubtask(new Subtask(1, "Subtask #2", "Description #2", StatusOfTask.DONE));
        taskManager.addSubtask(new Subtask(1, "Subtask #3", "Description #3", StatusOfTask.NEW));

        Epic epic = taskManager.getEpicById(1);
        assertEquals(epic.getStatus(), StatusOfTask.IN_PROGRESS);
    }

    @Test
    void mustCorrectlyCalculateTheStatusOfTheEpicIfAllSubtasksAreIN_PROGRESS() {
        //Подзадачи со статусом IN_PROGRESS и расчет статуса Эпика
        taskManager.addEpic(new Epic("Epic #1", "Description of Epic", StatusOfTask.NEW));
        taskManager.addSubtask(new Subtask(1, "Subtask #1", "Description #1", StatusOfTask.IN_PROGRESS));
        taskManager.addSubtask(new Subtask(1, "Subtask #2", "Description #2", StatusOfTask.IN_PROGRESS));
        taskManager.addSubtask(new Subtask(1, "Subtask #3", "Description #3", StatusOfTask.IN_PROGRESS));

        Epic epic = taskManager.getEpicById(1);
        assertEquals(epic.getStatus(), StatusOfTask.IN_PROGRESS);
    }

    @Test
    void mustChecksAssignmentOfTheTaskToTheEpic() {
        //Эпик содержит подзадачи, а подзадачи присвоены эпику
        taskManager.addEpic(new Epic("Epic #1", "Description of Epic", StatusOfTask.NEW));
        taskManager.addSubtask(new Subtask(1, "Subtask #1", "Description #1", StatusOfTask.IN_PROGRESS));
        taskManager.addSubtask(new Subtask(1, "Subtask #2", "Description #2", StatusOfTask.NEW));
        taskManager.addSubtask(new Subtask(1, "Subtask #3", "Description #3", StatusOfTask.DONE));

        Epic epic = taskManager.getEpicById(1);

        List<Subtask> subtasksInTheManager = taskManager.getSubtasks();
        List<Subtask> subtasksInTheEpic = epic.getEpicSubtasks();

        assertTrue(subtasksInTheManager.containsAll(subtasksInTheEpic));
        assertTrue(subtasksInTheEpic.stream()
                .allMatch(subtask -> subtask.getEpicId() == epic.getTaskId()));
    }

    @Test
    void mustChecksIntersectionOfIntervals() {
        // При пересечке задача не добавляется в менеджер, а при отсутсвии пересечения наоборот
        Task task1 = new Task("Task #1", "Description #1", StatusOfTask.NEW,
                LocalDateTime.parse("10.10.2010 10:00", Task.dateFormatter), Duration.ofMinutes(120));
        taskManager.addTask(task1);
        // Пересекается с task1
        Task task2 = new Task("Task #2", "Description #2", StatusOfTask.NEW,
                LocalDateTime.parse("10.10.2010 11:00", Task.dateFormatter), Duration.ofMinutes(120));
        taskManager.addTask(task2);
        // Пересекается с task1
        Task task3 = new Task("Task #3", "Description #3", StatusOfTask.NEW,
                LocalDateTime.parse("10.10.2010 09:00", Task.dateFormatter), Duration.ofMinutes(120));
        taskManager.addTask(task3);
        // НЕ пересекается с task1
        Task task4 = new Task("Task #4", "Description #4", StatusOfTask.NEW,
                LocalDateTime.parse("01.10.2010 10:00", Task.dateFormatter), Duration.ofMinutes(120));
        taskManager.addTask(task4);

        List<Task> allTasks = taskManager.getTasks();

        assertFalse(allTasks.contains(task2));
        assertFalse(allTasks.contains(task3));
        assertTrue(allTasks.contains(task4));
    }
}
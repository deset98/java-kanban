package taskManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    TaskManager taskManager;

   @BeforeEach
   void beforeEach() {
       taskManager = Managers.getDefaultTaskManager();
   }

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
        Subtask subtask = new Subtask(1,"Подзадача", "Описание подзадачи", StatusOfTask.NEW);
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
        Subtask subtask = new Subtask(1,"Подзадача 1", "Помыть посуду", StatusOfTask.NEW);
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
        ArrayList<Subtask> epicSubtasksBeforeAdd = epic.getEpicSubtasks();

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


}
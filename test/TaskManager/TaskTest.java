package TaskManager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void tasksShouldBeEqualIfTheirIDEqual() {
        Task task1 = new Task("Задача 1", "Помыть посуду", StatusOfTask.NEW);
        task1.setTaskId(1);
        Task task2 = new Task("Задача 2", "Выкинуть мусор", StatusOfTask.NEW);
        task2.setTaskId(1);

        assertEquals(task1, task2, "Задачи не равны");
    }




}
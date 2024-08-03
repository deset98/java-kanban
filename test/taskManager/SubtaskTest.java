package taskManager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void subtasksShouldBeEqualIfTheirIDEqual() {
        Subtask subtask1 = new Subtask(1, "Подзадача 1", "Помыть посуду", StatusOfTask.NEW);
        subtask1.setTaskId(1);
        Subtask subtask2 = new Subtask(1, "Подзадача 2", "Выкинуть мусор", StatusOfTask.NEW);
        subtask2.setTaskId(1);

        assertEquals(subtask1, subtask2, "Подзадачи не равны");
    }
}
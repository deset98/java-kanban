package model;

import model.enums.*;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskTest {

    @Test
    void subtasksShouldBeEqualIfTheirIDEqual() {
        Subtask subtask1 = new Subtask(1, "Подзадача 1", "Помыть посуду", StatusOfTask.NEW,
                LocalDateTime.parse("09.10.2020 10:00", Task.dateFormatter), Duration.ofMinutes(100));
        subtask1.setTaskId(1);
        Subtask subtask2 = new Subtask(1, "Подзадача 2", "Выкинуть мусор", StatusOfTask.NEW,
                LocalDateTime.parse("09.10.2020 10:00", Task.dateFormatter), Duration.ofMinutes(100));
        subtask2.setTaskId(1);

        assertEquals(subtask1, subtask2, "Подзадачи не равны");
    }
}
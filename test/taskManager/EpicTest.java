package taskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void epicShouldBeEqualIfTheirIDEqual() {
        Epic epic1 = new Epic("Эпик 1", "Помыть посуду", StatusOfTask.NEW);
        epic1.setTaskId(1);
        Epic epic2 = new Epic("Эпик 2", "Выкинуть мусор", StatusOfTask.NEW);
        epic2.setTaskId(1);

        assertEquals(epic1, epic2, "Эпики не равны");
    }
}
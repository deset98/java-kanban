package TaskManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void managerTestShouldBeReturnInitializedTaskManager() {
        TaskManager taskManager1 = new InMemoryTaskManager();
        TaskManager taskManager2 = Managers.getDefaultTaskManager();

        assertEquals(taskManager1, taskManager2, "Менеджеры задач не равны");
    }

    @Test
    void managerTestShouldBeReturnInitializedHistoryManager() {
        HistoryManager historyManager1 = new InMemoryHistoryManager();
        HistoryManager historyManager2 = Managers.getDefaultHistoryManager();

        Assertions.assertEquals(historyManager1, historyManager2, "Менеджеры истории не равны");
    }
}
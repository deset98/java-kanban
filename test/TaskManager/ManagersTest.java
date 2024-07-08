package TaskManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void shouldReturnDefaultTaskManager() {
        Assertions.assertNotNull(Managers.getDefaultTaskManager(), "Менеджер задач не инициализирован");
        assertInstanceOf(TaskManager.class, Managers.getDefaultTaskManager());
    }

    @Test
    void managerTestShouldBeReturnInitializedHistoryManager() {
        assertNotNull(Managers.getDefaultHistoryManager(), "Менеджер истории не инициализирован");
        assertInstanceOf(HistoryManager.class, Managers.getDefaultHistoryManager());
    }
}
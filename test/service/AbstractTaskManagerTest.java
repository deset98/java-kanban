package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.interfaces.HistoryManager;
import service.interfaces.TaskManager;
import util.Managers;

abstract class AbstractTaskManagerTest<T extends TaskManager> {
    protected TaskManager taskManager;
    protected HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefaultTaskManager();
    }

    @Test
    abstract void addNewTaskAndFindItByNumber();

    @Test
    abstract void addNewEpicAndFindItByNumber();

    @Test
    abstract void addNewSubtaskAndFindItByNumber();


    @Test
    abstract void taskShouldBeKeepTheirValuesOfFieldsAfterAddInTaskManager();

    @Test
    abstract void subtaskShouldBeKeepTheirValuesOfFieldsAfterAddInTaskManager();

    @Test
    abstract void epicShouldBeKeepTheirValuesOfFieldsAfterAddInTaskManager();


    @Test
    abstract void taskShouldBeEqualsWithAddIdAndTaskWithSetId();

    @Test
    abstract void mustCorrectlyCalculateTheStatusOfTheEpicIfAllSubtasksAreNEW();

    @Test
    abstract void mustCorrectlyCalculateTheStatusOfTheEpicIfAllSubtasksAreDONE();

    @Test
    abstract void mustCorrectlyCalculateTheStatusOfTheEpicIfSubtasksAreNEWorDONE();

    @Test
    abstract void mustCorrectlyCalculateTheStatusOfTheEpicIfAllSubtasksAreIN_PROGRESS();

    @Test
    abstract void mustChecksAssignmentOfTheTaskToTheEpic();

    @Test
    abstract void mustChecksIntersectionOfIntervals();
}

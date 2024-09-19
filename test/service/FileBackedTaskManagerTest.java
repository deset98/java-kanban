package service;

import exceptions.ManagerSaveException;
import model.Task;
import model.enums.StatusOfTask;
import org.junit.jupiter.api.Test;
import util.Managers;

import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends InMemoryTaskManagerTest {

    File tempfile1;
    File tempfile2;

    @Override
    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefaultTaskManager();
        historyManager = Managers.getDefaultHistoryManager();

        try {
            tempfile1 = File.createTempFile("file1", ".csv");
            tempfile2 = File.createTempFile("file2", ".jpeg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testLoadFromFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempfile1, StandardCharsets.UTF_8))) {

            writer.write("id,type,name,status,description,epic,startTime,duration,endTime\n");
            writer.write("1,TASK,Name of task #1,NEW,Description of Task #1,no epic,10.10.2020 10:00,1000,11.10.2020 02:40\n");
            writer.write("2,TASK,Name of task #2,NEW,Description of Task #2,no epic,09.10.2020 10:00,100,09.10.2020 11:40\n");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при запись в файл");
        }

        FileBackedTaskManager managerFromFile = FileBackedTaskManager.loadFromFile(tempfile1);

        Task task1 = new Task("Name of task #1", "Description of Task #1", StatusOfTask.NEW,
                LocalDateTime.parse("10.10.2020 10:00", Task.dateFormatter), Duration.ofMinutes(1000));
        taskManager.addTask(task1);

        Task task2 = new Task("Name of task #2", "Description of Task #2", StatusOfTask.NEW,
                LocalDateTime.parse("09.10.2020 10:00", Task.dateFormatter), Duration.ofMinutes(100));
        taskManager.addTask(task2);

        assertEquals(task1, managerFromFile.getTaskById(1));
        assertEquals(task2, managerFromFile.getTaskById(2));
    }
}
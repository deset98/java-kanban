import org.w3c.dom.ls.LSOutput;

import model.*;
import model.enums.*;
import service.*;
import service.interfaces.*;
import util.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Main {

    public static void main(String[] args) throws IOException {

        TaskManager manager1 = Managers.getDefaultFileBackedTaskManager(new File("C:\\Users\\Deset\\" +
                "java-dev-yandex-practicum\\java-kanban\\autosave1.csv"));
//        TaskManager manager1 = Managers.getDefaultTaskManager();

        // ЗАДАЧА 1
        manager1.addTask(new Task("Name of task #1", "Description of Task #1", StatusOfTask.NEW,
                LocalDateTime.parse("10.10.2020 10:00", Task.dateFormatter), Duration.ofMinutes(1000)));
        // ЗАДАЧА 2
        manager1.addTask(new Task("Name of task #2", "Description of Task #2", StatusOfTask.NEW,
                LocalDateTime.parse("09.10.2020 10:00", Task.dateFormatter), Duration.ofMinutes(100)));

        // ЭПИК 3
        manager1.addEpic(new Epic("Name of Epic #1", "Description of Epic #1", StatusOfTask.NEW));
        // ЭПИК 4
        manager1.addEpic(new Epic("Name of Epic #2", "Description of Epic #2", StatusOfTask.NEW));

        // ПОДЗАДАЧА 5 эпик 3
        manager1.addSubtask(new Subtask(3, "Name of Subtask #1",
                "Description of Subtask #1 for Epic #3", StatusOfTask.NEW,
                LocalDateTime.parse("08.10.2020 10:00", Task.dateFormatter), Duration.ofMinutes(100)));
        // ПОДЗАДАЧА 6 эпик 3
        manager1.addSubtask(new Subtask(3, "Name of Subtask #2",
                "Description of Subtask #2 for Epic # 3", StatusOfTask.NEW,
                LocalDateTime.parse("07.10.2020 10:00", Task.dateFormatter), Duration.ofMinutes(100)));
        // ПОДЗАДАЧА 7 эпик 4
        manager1.addSubtask(new Subtask(4, "Name of Subtask #3",
                "Description of Subtask #1 for Epic # 4", StatusOfTask.DONE,
                LocalDateTime.parse("06.10.2020 10:00", Task.dateFormatter), Duration.ofMinutes(100)));
        // ПОДЗАДАЧА б/н эпик 3 (пересекается по времени с другой и не создается)
        manager1.addSubtask(new Subtask(3, "Name of Subtask #4",
                "Description of Subtask #3 for Epic # 3", StatusOfTask.DONE,
                LocalDateTime.parse("06.10.2020 10:00", Task.dateFormatter), Duration.ofMinutes(2000)));
        // ПОДЗАДАЧА 8 эпик 3
        manager1.addSubtask(new Subtask(3, "Name of Subtask #5",
                "Description of Subtask #4 for Epic # 3", StatusOfTask.DONE,
                LocalDateTime.parse("16.10.2018 10:00", Task.dateFormatter), Duration.ofMinutes(990)));
        // ПОДЗАДАЧА 9 эпик 4 не задана дата и продолжительность
        manager1.addSubtask(new Subtask(4, "Name of Subtask #6",
                "Description of Subtask #5 for Epic # 4", StatusOfTask.DONE));


        manager1.getTaskById(2);
        manager1.getTaskById(1);
        manager1.getEpicById(3);
        manager1.getEpicById(4);
        manager1.getSubtaskById(7);
        manager1.getSubtaskById(6);
        manager1.getSubtaskById(5);
        manager1.getSubtaskById(6);
        manager1.getSubtaskById(6);
        manager1.getTaskById(2);
        manager1.getSubtaskById(8);
        manager1.getSubtaskById(8);
        manager1.getTaskById(1);


/*        manager1.clearSubtasks();
        manager1.clearTasks();
        manager1.clearEpics();*/


        System.out.println("\nПРИОРИТЕТНЫЙ СПИСОК:");
        manager1.getPrioritizedTasks().forEach(System.out::println);

        System.out.println("\nИСТОРИЯ:");
        manager1.getHistory().forEach(System.out::println);

        System.out.println("\nВСЕ ЗАДАЧИ:");
        manager1.getAllTasks().forEach(System.out::println);

        System.out.println("\nЭПИКИ:");
        manager1.getEpics().forEach(System.out::println);

        System.out.println("\nПОДЗАДАЧИ:");
        manager1.getSubtasks().forEach(System.out::println);

        System.out.println("\nПОДЗАДАЧИ ЭПИКОВ:");
        manager1.getEpics().forEach(epic -> System.out.println("-------- ЭПИК №" + epic.getTaskId() + ":\n"
                + epic + "\n" + epic.getEpicSubtasks() + "\n"));

        System.out.println("\n");


        TaskManager manager2 = FileBackedTaskManager.loadFromFile(new File("C:\\Users\\Deset\\" +
                "java-dev-yandex-practicum\\java-kanban\\autosave1.csv"));

        System.out.println("----=== LOAD FROM MANAGER 2 ===----");
        manager2.getAllTasks().forEach(System.out::println);
    }
}
import taskmanager.*;

import java.io.File;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        TaskManager manager1 = Managers.getDefaultFileBackedTaskManager(new File("C:\\Users\\Deset\\" +
                "java-dev-yandex-practicum\\java-kanban\\autosave1.csv"));


        manager1.addTask(new Task("Name of task #1", "Description of Task #1", StatusOfTask.NEW));
        manager1.addTask(new Task("Name of task #2", "Description of Task #2", StatusOfTask.NEW));

        manager1.addEpic(new Epic("Name of Epic #1", "Description of Epic #1", StatusOfTask.NEW));
        manager1.addEpic(new Epic("Name of Epic #2", "Description of Epic #2", StatusOfTask.NEW));

        manager1.addSubtask(new Subtask(3, "Name of Subtask #1",
                "Description of Subtask #1 for Epic #3", StatusOfTask.NEW));
        manager1.addSubtask(new Subtask(3, "Name of Subtask #2",
                "Description of Subtask #2 for Epic # 3", StatusOfTask.NEW));
        manager1.addSubtask(new Subtask(4, "Name of Subtask #1",
                "Description of Subtask #1 for Epic # 4", StatusOfTask.DONE));


        for (Task task : manager1.getAllTasks()) {
            System.out.println("M1 - " + task);
        }
        System.out.println("\n");

        TaskManager manager2 = FileBackedTaskManager.loadFromFile(new File("C:\\Users\\Deset\\" +
                "java-dev-yandex-practicum\\java-kanban\\autosave2.csv"));

        for (Task task : manager2.getAllTasks()) {
            System.out.println("M2 - " + task);
        }
        System.out.println("\n");
    }
}
import taskmanager.*;

import java.io.File;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        // TaskManager manager2 = Managers.loadFromFile(File.createTempFile("autosave", "csv"));
        TaskManager manager2 = Managers.loadFromFile(new File("C:\\Users\\Deset\\" +
                "java-dev-yandex-practicum\\java-kanban\\autosave.csv"));


        manager2.addTask(new Task("Задача #1", "Обычная задача #1", StatusOfTask.NEW));
        manager2.addTask(new Task("Задача #2", "Обычная задача #2", StatusOfTask.NEW));

        manager2.addEpic(new Epic("Эпик #1", "Сложная задача #1 из нескольких частей", StatusOfTask.NEW));
        manager2.addEpic(new Epic("Эпик #2", "Сложная задача #2 из нескольких частей", StatusOfTask.NEW));

        manager2.addSubtask(new Subtask(3, "Подзадача #1", "Задача #1 для эпика c id 3",
                StatusOfTask.NEW));
        manager2.addSubtask(new Subtask(3, "Подзадача #2", "Задача #2 для эпика c id 3",
                StatusOfTask.NEW));
        manager2.addSubtask(new Subtask(4, "Подзадача #3", "Задача #3 для эпика с id 3",
                StatusOfTask.DONE));

        manager2.getTaskById(1);
        manager2.getEpicById(3);
        manager2.getSubtaskById(6);
        manager2.getSubtaskById(5);
        manager2.getSubtaskById(7);
        manager2.getTaskById(2);
        manager2.getEpicById(4);

        for (Task task : manager2.getHistory()) {
            System.out.println("main " + task);
        }
        System.out.println("\n");
    }
}
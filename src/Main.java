import TaskManager.*;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefaultTaskManager();

        manager.addTask(new Task("Задача #1", "Обычная задача #1", StatusOfTask.NEW));
        manager.addTask(new Task("Задача #2", "Обычная задача #2", StatusOfTask.NEW));

        manager.addEpic(new Epic("Эпик #1", "Сложная задача #1 из нескольких частей", StatusOfTask.NEW));
        manager.addEpic(new Epic("Эпик #2", "Сложная задача #2 из нескольких частей", StatusOfTask.NEW));

        manager.addSubtask(new Subtask(3, "Подзадача #1", "Задача #1 для эпика c id 3",
                StatusOfTask.NEW));
        manager.addSubtask(new Subtask(3, "Подзадача #2", "Задача #2 для эпика c id 3",
                StatusOfTask.NEW));
        manager.addSubtask(new Subtask(4, "Подзадача #1", "Задача #1 для эпика с id 4",
                StatusOfTask.DONE));
        manager.addSubtask(new Subtask(4, "Подзадача #2", "Задача #2 для эпика с id 4",
                StatusOfTask.NEW));

        manager.getTaskById(1);
        manager.getTaskById(2);

        manager.getEpicById(3);
        manager.getEpicById(4);

        manager.getSubtaskById(5);
        manager.getSubtaskById(6);
        manager.getSubtaskById(7);
        manager.getSubtaskById(8);

        printAllTasks(manager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("\nЭпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks(epic.getTaskId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("\nПодзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("\nИстория:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
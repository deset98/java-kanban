package util;

import service.*;
import service.interfaces.*;

import java.io.File;


public class Managers {

    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getDefaultFileBackedTaskManager(File file) {
        return new FileBackedTaskManager(file);
    }
}
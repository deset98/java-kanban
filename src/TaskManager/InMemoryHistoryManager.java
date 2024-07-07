package TaskManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> history = new ArrayList<>();


    public  <T extends Task> void add(T task) {
        if (history.size() == 10) {
            history.removeFirst();
        }
        history.add(task);
    }

    public List<Task> getHistory() {
        return history;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryHistoryManager that = (InMemoryHistoryManager) o;
        return Objects.equals(history, that.history);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(history);
    }
}

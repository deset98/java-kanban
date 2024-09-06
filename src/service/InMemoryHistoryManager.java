package service;

import model.*;
import service.interfaces.HistoryManager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node> history = new HashMap<>();
    private Node head;
    private Node tail;

    public <T extends Task> void add(T task) {
        Node currentNode = null;

        if (history.containsKey(task.getTaskId())) {
            currentNode = history.get(task.getTaskId());

            if (currentNode.equals(tail)) {
                return; // если просматриваемая задача уже есть в конце истории, то linkLast и removeNode не выполняются
            }

            remove(task.getTaskId());
        }
        linkLast(task);
    }

    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        Node node = head;

        if (node != null) {
            for (int i = 0; i < history.size(); i++) {
                tasks.add(node.data);
                node = node.next;
            }
        }
        return tasks;
    }

    public void remove(int id) {
        if (history.size() > 1) {
            removeNode(history.get(id));
            history.remove(id);
            setLastNodeAsHeadAndTail();

        } else if (history.size() == 1) {
            history.clear();
            head = tail = null;
        }
    }

    public void removeNode(Node node) {
        Node prevNode = node.prev;
        Node nextNode = node.next;

        if (prevNode == null) {
            head = nextNode;
        } else {
            prevNode.next = nextNode;
            node.prev = null;
        }

        if (nextNode == null) {
            tail = prevNode;
        } else {
            nextNode.prev = prevNode;
            node.next = null;
        }

        node.data = null;
    }

    private void setLastNodeAsHeadAndTail() {
        if (history.size() == 1) {
            List<Node> theLastNode = new ArrayList<>(history.values());
            head = tail = theLastNode.getFirst();
        }
    }

    private <T extends Task> void linkLast(T task) {
        Node newNode = null;
        Node oldTail = tail;
        if (tail != null) {
            newNode = new Node(tail, task, null);
            oldTail.next = newNode;
            tail = newNode;
        } else if (head == null) {
            newNode = new Node(null, task, null);
            head = newNode;
            tail = newNode;
        }
        history.put(task.getTaskId(), newNode);
    }

    private static class Node {

        private Task data;
        private Node next;
        private Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}
package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.interfaces.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_TASKS:
                handleGetTasks(exchange);
                return;
            case GET_TASK_BY_ID:
                handleGetTaskById(exchange);
                return;
            case POST_TASK:
                handlePostTask(exchange);
                return;
            case DELETE_TASK_BY_ID:
                handleDeleteTaskById(exchange);
                return;
            case UNKNOWN:
                sendInternalError(exchange);
                return;
            default:
                sendText(exchange, "Unknown endpoint", 404);
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getTasks());
        sendText(exchange, response, 200);
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> id = parseIdFromPath(exchange);
        if (id.isPresent()) {
            if (!thisTaskExist(id.get())) {
                sendNotFound(exchange);
                return;
            }
            String response = gson.toJson(taskManager.getTaskById(id.get()));
            sendText(exchange, response, 200);
        }
        sendInternalError(exchange);
    }

    private void handlePostTask(HttpExchange exchange) throws IOException {

        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "text/plain");

        Optional<Integer> id = parseIdFromPath(exchange);
        if (id.isPresent()) {
            try (InputStream inputStream = exchange.getRequestBody()) {

                String requestBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Task task = gson.fromJson(requestBody, Task.class);

                if (!thisTaskExist(id.get())) {
                    sendNotFound(exchange);
                    return;
                }

                taskManager.updateTask(task);
                sendText(exchange, "Task update", 201);
            }
        }

        if (id.isEmpty()) {
            try (InputStream inputStream = exchange.getRequestBody()) {
                String requestBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Task task = gson.fromJson(requestBody, Task.class);

                if (thisTaskExist(task.getTaskId())) {
                    sendHasInteractions(exchange);
                    return;
                }
                taskManager.addTask(task);
                sendText(exchange, "Task added", 201);
            }
        }
        sendInternalError(exchange);
    }


    private void handleDeleteTaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> id = parseIdFromPath(exchange);
        if (id.isPresent()) {
            if (thisTaskExist(id.get())) {
                sendNotFound(exchange);
                return;
            }
            taskManager.removeTaskById(id.get());
            sendText(exchange, "Task deleted", 201);
        }
    }


    private boolean thisTaskExist(int id) {
        return taskManager.getAllTasks().isEmpty() ||
                taskManager.getAllTasks().stream()
                        .anyMatch(task -> task.getTaskId() == id);
    }

    private Optional<Integer> parseIdFromPath(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }


    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        switch (requestMethod) {
            case "GET":
                if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
                    return Endpoint.GET_TASKS;
                } else if (pathParts.length == 3 && pathParts[1].equals("tasks")) {
                    try {
                        Integer.parseInt(pathParts[2]);
                        return Endpoint.GET_TASK_BY_ID;
                    } catch (NumberFormatException e) {
                        return Endpoint.UNKNOWN;
                    }
                } else {
                    return Endpoint.UNKNOWN;
                }
            case "POST":
                return Endpoint.POST_TASK;
            case "DELETE":
                return Endpoint.DELETE_TASK_BY_ID;
            default:
                return Endpoint.UNKNOWN;
        }
    }
}
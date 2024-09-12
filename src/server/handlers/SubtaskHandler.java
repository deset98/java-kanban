package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import model.Subtask;
import service.interfaces.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_SUBTASKS:
                handleGetSubtasks(exchange);
                return;
            case GET_SUBTASK_BY_ID:
                handleGetSubtaskById(exchange);
                return;
            case POST_SUBTASK:
                handlePostSubtask(exchange);
                return;
            case DELETE_SUBTASK_BY_ID:
                handleDeleteSubtaskById(exchange);
                return;
            case UNKNOWN:
                sendInternalError(exchange);
                return;
            default:
                sendText(exchange, "Unknown endpoint", 404);
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getSubtasks());
        sendText(exchange, response, 200);
    }

    private void handleGetSubtaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> id = parseIdFromPath(exchange);
        if (id.isPresent()) {
            if (!thisSubtaskExist(id.get())) {
                sendNotFound(exchange);
                return;
            }
            String response = gson.toJson(taskManager.getSubtaskById(id.get()));
            sendText(exchange, response, 200);
        }
        sendInternalError(exchange);
    }

    private void handlePostSubtask(HttpExchange exchange) throws IOException {

        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "text/plain");

        Optional<Integer> id = parseIdFromPath(exchange);
        if (id.isPresent()) {
            try (InputStream inputStream = exchange.getRequestBody()) {

                String requestBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Subtask subtask = gson.fromJson(requestBody, Subtask.class);

                if (!thisSubtaskExist(id.get())) {
                    sendNotFound(exchange);
                    return;
                }

                taskManager.updateSubtask(subtask);
                sendText(exchange, "Subtask update", 201);
            }
        }

        if (id.isEmpty()) {
            try (InputStream inputStream = exchange.getRequestBody()) {
                String requestBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Subtask subtask = gson.fromJson(requestBody, Subtask.class);

                if (thisSubtaskExist(subtask.getTaskId())) {
                    sendHasInteractions(exchange);
                    return;
                }
                taskManager.addSubtask(subtask);
                sendText(exchange, "Subtask added", 201);
            }
        }
        sendInternalError(exchange);
    }


    private void handleDeleteSubtaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> id = parseIdFromPath(exchange);
        if (id.isPresent()) {
            if (!thisSubtaskExist(id.get())) {
                sendNotFound(exchange);
                return;
            }
            taskManager.removeSubtaskById(id.get());
            sendText(exchange, "Subtask deleted", 201);
        }
    }


    private boolean thisSubtaskExist(int id) {
        return taskManager.getAllTasks().isEmpty() ||
                taskManager.getAllTasks().stream()
                        .anyMatch(task -> task.getTaskId() == id);

    }

    private Optional<Integer> parseIdFromPath(HttpExchange exchange) {
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
                if (pathParts.length == 2 && pathParts[1].equals("subtasks")) {
                    return Endpoint.GET_SUBTASKS;
                } else if (pathParts.length == 3 && pathParts[1].equals("subtasks")) {
                    try {
                        Integer.parseInt(pathParts[2]);
                        return Endpoint.GET_SUBTASK_BY_ID;
                    } catch (NumberFormatException e) {
                        return Endpoint.UNKNOWN;
                    }
                } else {
                    return Endpoint.UNKNOWN;
                }
            case "POST":
                return Endpoint.POST_SUBTASK;
            case "DELETE":
                return Endpoint.DELETE_SUBTASK_BY_ID;
            default:
                return Endpoint.UNKNOWN;
        }
    }
}
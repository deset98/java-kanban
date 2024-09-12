package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import service.interfaces.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_EPICS:
                handleGetEpics(exchange);
                return;
            case GET_EPIC_BY_ID:
                handleGetEpicById(exchange);
                return;
            case GET_EPIC_SUBTASK_BY_EPIC_ID:
                handleGetEpicSubtasks(exchange);
                return;
            case POST_EPIC:
                handlePostEpic(exchange);
                return;
            case DELETE_EPIC_BY_ID:
                handleDeleteEpicById(exchange);
                return;
            case UNKNOWN:
                sendInternalError(exchange);
                return;
            default:
                sendText(exchange, "Unknown endpoint", 404);
        }
    }

    private void handleGetEpicSubtasks(HttpExchange exchange) throws IOException {
        Optional<Integer> id = parseIdFromPath(exchange);
        if (id.isPresent()) {
            if (!thisEpicExist(id.get())) {
                sendNotFound(exchange);
                return;
            }
            String response = gson.toJson(taskManager.getEpicById(id.get()).getEpicSubtasks());
            sendText(exchange, response, 200);
        }
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getEpics());
        sendText(exchange, response, 200);
    }

    private void handleGetEpicById(HttpExchange exchange) throws IOException {
        Optional<Integer> id = parseIdFromPath(exchange);
        if (id.isPresent()) {
            if (!thisEpicExist(id.get())) {
                sendNotFound(exchange);
                return;
            }
            String response = gson.toJson(taskManager.getEpicById(id.get()));
            sendText(exchange, response, 200);
        }
        sendInternalError(exchange);
    }

    private void handlePostEpic(HttpExchange exchange) throws IOException {

        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "text/plain");

        try (InputStream inputStream = exchange.getRequestBody()) {
            String requestBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(requestBody, Epic.class);

            if (thisEpicExist(epic.getTaskId())) {
                sendHasInteractions(exchange);
                return;
            }
            taskManager.addEpic(epic);
            sendText(exchange, "Epic added", 201);
        }

        sendInternalError(exchange);
    }


    private void handleDeleteEpicById(HttpExchange exchange) throws IOException {
        Optional<Integer> id = parseIdFromPath(exchange);
        if (id.isPresent()) {
            if (!thisEpicExist(id.get())) {
                sendNotFound(exchange);
                return;
            }
            taskManager.removeEpicById(id.get());
            sendText(exchange, "Epic deleted", 201);
        }
    }


    private boolean thisEpicExist(int id) {
        return taskManager.getAllTasks().isEmpty() ||
                taskManager.getEpics().stream()
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
                if (pathParts.length == 2 && pathParts[1].equals("epics")) {
                    return Endpoint.GET_EPICS;
                } else if (pathParts.length == 3 && pathParts[1].equals("epics")) {
                    try {
                        Integer.parseInt(pathParts[2]);
                        return Endpoint.GET_EPIC_BY_ID;
                    } catch (NumberFormatException e) {
                        return Endpoint.UNKNOWN;
                    }
                } else if (pathParts.length == 4 && pathParts[1].equals("epics") && pathParts[3].equals("subtasks")) {
                    return Endpoint.GET_EPIC_SUBTASK_BY_EPIC_ID;
                } else {
                    return Endpoint.UNKNOWN;
                }
            case "POST":
                return Endpoint.POST_EPIC;
            case "DELETE":
                return Endpoint.DELETE_EPIC_BY_ID;
            default:
                return Endpoint.UNKNOWN;
        }
    }
}
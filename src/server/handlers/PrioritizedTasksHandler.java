package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import service.interfaces.TaskManager;

import java.io.IOException;

public class PrioritizedTasksHandler extends BaseHttpHandler {

    public PrioritizedTasksHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_PRIORITIZED_TASKS:
                handleGetPrioritizedTasks(exchange);
                return;
            case UNKNOWN:
                sendInternalError(exchange);
                return;
            default:
                sendText(exchange, "Unknown endpoint", 404);
        }
    }


    private void handleGetPrioritizedTasks(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getPrioritizedTasks());
        sendText(exchange, response, 200);
    }


    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (requestMethod.equals("GET")) {
            if (pathParts.length == 2 && pathParts[1].equals("prioritized")) {
                return Endpoint.GET_PRIORITIZED_TASKS;
            } else {
                return Endpoint.UNKNOWN;
            }
        } else {
            return Endpoint.UNKNOWN;
        }
    }
}
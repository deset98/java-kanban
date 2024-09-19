package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.interfaces.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {

    protected final TaskManager taskManager;
    protected final Gson gson;

    public BaseHttpHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }


    protected void sendText(HttpExchange exchange, String text, int rCode) throws IOException {
        this.writeResponse(exchange, text, rCode);
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        this.writeResponse(exchange, "Not found", 404);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        this.writeResponse(exchange, "Not Acceptable", 406);
    }

    protected void sendInternalError(HttpExchange exchange) throws IOException {
        this.writeResponse(exchange, "Internal Server Error", 500);
    }

    private void writeResponse(HttpExchange exchange, String text, int rCode) throws IOException {

        try (exchange) {
            try (OutputStream os = exchange.getResponseBody()) {
                exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                exchange.sendResponseHeaders(rCode, 0);
                os.write(text.getBytes(StandardCharsets.UTF_8));
            }
        }

    }

    protected enum Endpoint {
        GET_TASKS,
        GET_TASK_BY_ID,
        POST_TASK,
        DELETE_TASK_BY_ID,

        GET_SUBTASKS,
        GET_SUBTASK_BY_ID,
        POST_SUBTASK,
        DELETE_SUBTASK_BY_ID,

        GET_EPICS,
        GET_EPIC_BY_ID,
        GET_EPIC_SUBTASK_BY_EPIC_ID,
        POST_EPIC,
        DELETE_EPIC_BY_ID,

        GET_HISTORY,

        GET_PRIORITIZED_TASKS,

        UNKNOWN
    }
}
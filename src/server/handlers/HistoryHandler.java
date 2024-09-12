package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import service.interfaces.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_HISTORY:
                handleGetHistory(exchange);
                return;
            case UNKNOWN:
                sendInternalError(exchange);
                return;
            default:
                sendText(exchange, "Unknown endpoint", 404);
        }
    }


    private void handleGetHistory(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getHistory());
        sendText(exchange, response, 200);
    }


    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");


        if (requestMethod.equals("GET")) {
            if (pathParts.length == 2 && pathParts[1].equals("history")) {
                return Endpoint.GET_HISTORY;
            } else {
                return Endpoint.UNKNOWN;
            }
        } else {
            return Endpoint.UNKNOWN;
        }
    }
}
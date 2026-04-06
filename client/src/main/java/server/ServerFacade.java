package server;

import com.google.gson.Gson;
import exception.ResponseException;
import jakarta.websocket.*;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.ListGamesResult;
import result.LoginResult;
import result.RegisterResult;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade extends Endpoint {

    public Session session;
    private final String serverURL;
    private final HttpClient http;
    private final Gson gson;
    private String authToken;
    private int gameID;

    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
        this.http = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    public void registerUser(RegisterRequest registerRequest) throws ResponseException {
        RegisterResult registerResult = this.makeRequest("POST", "/user", registerRequest, RegisterResult.class);
        authToken = registerResult.authToken();
    }

    public void loginUser(LoginRequest loginRequest) throws ResponseException {
        LoginResult loginResult = this.makeRequest("POST", "/session", loginRequest, LoginResult.class);
        authToken = loginResult.authToken();
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws ResponseException {
        return this.makeRequest("POST", "/game", createGameRequest, CreateGameResult.class);
    }

    public ListGamesResult listGames() throws ResponseException {
        return this.makeRequest("GET", "/game", null, ListGamesResult.class);
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws ResponseException {
        this.makeRequest("PUT", "/game", joinGameRequest, null);
    }

    public void logoutUser() throws ResponseException {
        this.makeRequest("DELETE", "/session", null, null);
        authToken = null;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            var builder = HttpRequest.newBuilder()
                    .uri(URI.create(serverURL + path))
                    .header("Content-Type", "application/json");

            if (authToken != null) {
                builder.header("Authorization", authToken);
            }
            HttpRequest.BodyPublisher publisher;
            if (request != null) {
                publisher = HttpRequest.BodyPublishers.ofString(gson.toJson(request));
            } else {
                publisher = HttpRequest.BodyPublishers.noBody();
            }
            builder.method(method, publisher);
            HttpResponse<String> response = http.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            throwIfNotSuccessful(response);
            return readBody(response, responseClass);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    private <T> T readBody(HttpResponse<String> response, Class<T> responseClass) {
        String body = response.body();
        if (body == null || body.isBlank() || responseClass == null) {
            return null;
        }
        return gson.fromJson(body, responseClass);
    }

    private void throwIfNotSuccessful(HttpResponse<?> response) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public void createWSConnection(int gameID) throws Exception {
        this.gameID = gameID;
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);

        session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
            }
        });

        UserGameCommand userGameCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}

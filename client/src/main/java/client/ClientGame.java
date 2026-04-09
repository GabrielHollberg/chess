package client;

import chess.*;
import com.google.gson.Gson;
import request.*;
import result.CreateGameResult;
import result.ListGamesResult;
import server.ServerFacade;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.BLACK_BISHOP;
import static ui.EscapeSequences.BLACK_KING;
import static ui.EscapeSequences.BLACK_KNIGHT;
import static ui.EscapeSequences.BLACK_PAWN;
import static ui.EscapeSequences.BLACK_QUEEN;
import static ui.EscapeSequences.BLACK_ROOK;
import static ui.EscapeSequences.SET_BG_COLOR_BLUE;
import static ui.EscapeSequences.SET_BG_COLOR_DARK_BLUE;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLACK;
import static ui.EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;

public class ClientGame {
    ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
    ChessGame chessGame = new ChessGame();
    String username;
    String playerColor = "white";
    Scanner scanner = new Scanner(System.in);
    String line;

    public void run() {
        System.out.println(SET_TEXT_COLOR_BLUE + SET_BG_COLOR_BLACK);
        System.out.println(EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_BLUE + "(You can always type " + SET_TEXT_COLOR_RED + "\"h\"" + SET_TEXT_COLOR_BLUE + " for a list of commands)\n" + SET_TEXT_COLOR_BLUE);
        line = scanner.nextLine();
        while (true) {
            while (true) {
                if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h")) {
                    System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "\"h\" " + SET_TEXT_COLOR_BLUE + "(help)");
                    System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"r\" " + SET_TEXT_COLOR_BLUE + "(register)");
                    System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"l\" " + SET_TEXT_COLOR_BLUE + "(login)");
                    System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"q\" " + SET_TEXT_COLOR_BLUE + "(quit)\n");
                    line = scanner.nextLine();
                } else if (line.equalsIgnoreCase("register") || line.equalsIgnoreCase("r")) {
                    System.out.println("\n" + EMPTY + "  username:\n");
                    username = scanner.nextLine();
                    System.out.println("\n" + EMPTY + "  password:\n");
                    String password = scanner.nextLine();
                    System.out.println("\n" + EMPTY + "  email\n");
                    String email = scanner.nextLine();
                    RegisterRequest registerRequest = new RegisterRequest(username, password, email);
                    try {
                        serverFacade.registerUser(registerRequest);
                        break;
                    } catch (Exception e) {
                        System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "Sorry, this username has already been taken!\n" + SET_TEXT_COLOR_BLUE);
                        line = scanner.nextLine();
                    }
                } else if (line.equalsIgnoreCase("login") || line.equalsIgnoreCase("l")) {
                    System.out.println("\n" + EMPTY + "  username:\n");
                    username = scanner.nextLine();
                    System.out.println("\n" + EMPTY + "  password:\n");
                    String password = scanner.nextLine();
                    LoginRequest loginRequest = new LoginRequest(username, password);
                    try {
                        serverFacade.loginUser(loginRequest);
                        break;
                    } catch (Exception e) {
                        System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "Incorrect login!\n" + SET_TEXT_COLOR_BLUE);
                        line = scanner.nextLine();
                    }
                } else if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("q")) {
                    return;
                } else {
                    System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "Invalid command!\n" + SET_TEXT_COLOR_BLUE);
                    line = scanner.nextLine();
                }
            }
            System.out.println(SET_TEXT_COLOR_BLUE + SET_BG_COLOR_BLACK);
            System.out.println(EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_BLUE + "Signed in as " + SET_TEXT_COLOR_RED + username + SET_TEXT_COLOR_BLUE + "! (type " + SET_TEXT_COLOR_RED + "\"h\"" + SET_TEXT_COLOR_BLUE + " for more commands)\n" + SET_TEXT_COLOR_BLUE);
            line = scanner.nextLine();
            while (true) {
                if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h")) {
                    System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "\"h\" " + SET_TEXT_COLOR_BLUE + " (help)");
                    System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"c\" " + SET_TEXT_COLOR_BLUE + " (create a new game)");
                    System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"v\" " + SET_TEXT_COLOR_BLUE + " (view games)");
                    System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"j\" " + SET_TEXT_COLOR_BLUE + " (join a game)");
                    System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"s\" " + SET_TEXT_COLOR_BLUE + " (spectate a game)");
                    System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"l\" " + SET_TEXT_COLOR_BLUE + " (logout)");
                    System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"q\" " + SET_TEXT_COLOR_BLUE + " (quit)\n" + SET_TEXT_COLOR_BLUE);
                    line = scanner.nextLine();
                } else if (line.equalsIgnoreCase("create") || line.equalsIgnoreCase("c")) {
                    System.out.println("\n" + EMPTY + "  game name:\n");
                    String gameName = scanner.nextLine();
                    CreateGameRequest createGameRequest = new CreateGameRequest(gameName);
                    try {
                        CreateGameResult createGameResult = serverFacade.createGame(createGameRequest);
                        System.out.println("\n" + EMPTY + "  ------------Chess------------      Game " + SET_TEXT_COLOR_RED + gameName + SET_TEXT_COLOR_BLUE + " created! (type " + SET_TEXT_COLOR_RED + "\"join\"" + SET_TEXT_COLOR_BLUE + " or " + SET_TEXT_COLOR_RED + "\"j\"" + SET_TEXT_COLOR_BLUE + " and use game number " + SET_TEXT_COLOR_RED + createGameResult.gameID() + SET_TEXT_COLOR_BLUE + " if you would like to join this game)\n");
                        line = scanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "Sorry, This game could not be created!\n" + SET_TEXT_COLOR_BLUE);
                        line = scanner.nextLine();
                    }
                } else if (line.equalsIgnoreCase("view") || line.equalsIgnoreCase("v")) {
                    try {
                        ListGamesResult listGamesResult = serverFacade.listGames();
                        System.out.print("\n" + EMPTY + "  ------------Chess------------      ");
                        if (listGamesResult.games().isEmpty()) {
                            System.out.println("No games have been created!" + SET_TEXT_COLOR_BLUE);
                        } else {
                            System.out.println("If you'd like to join a game, remember the " + SET_TEXT_COLOR_RED + "game #" + SET_TEXT_COLOR_BLUE + " and type " + SET_TEXT_COLOR_RED + "\"j\"" + SET_TEXT_COLOR_BLUE);
                            System.out.println(EMPTY + "                                     (You may not take a slot that has already been filled)\n");
                            for (int i = 0; i < listGamesResult.games().size(); i++) {
                                System.out.print(EMPTY + "                                     " + SET_TEXT_COLOR_RED + (i + 1) + ". " + SET_TEXT_COLOR_BLUE + listGamesResult.games().get(i).gameName());
                                if (listGamesResult.games().get(i).whiteUsername() != null) {
                                    System.out.print("\n" + EMPTY + "                                     " + "white: " + SET_TEXT_COLOR_BLUE + listGamesResult.games().get(i).whiteUsername());
                                } else {
                                    System.out.print("\n" + EMPTY + "                                     " + "white: ");
                                }
                                if (listGamesResult.games().get(i).blackUsername() != null) {
                                    System.out.print("\n" + EMPTY + "                                     " + "black: " + SET_TEXT_COLOR_BLUE + listGamesResult.games().get(i).blackUsername());
                                } else {
                                    System.out.print("\n" + EMPTY + "                                     " + "black: " + SET_TEXT_COLOR_BLUE);
                                }
                                System.out.println();
                                System.out.println();
                            }
                            System.out.println(EMPTY + "                                     " + "If you'd like to join a game, remember the " + SET_TEXT_COLOR_RED + "game #" + SET_TEXT_COLOR_BLUE + " and type " + SET_TEXT_COLOR_RED + "\"j\"" + SET_TEXT_COLOR_BLUE);
                            System.out.println(EMPTY + "                                     (You may not take a slot that has already been filled)");
                        }
                        System.out.println();
                        line = scanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "Sorry, the games could not be viewed!\n" + SET_TEXT_COLOR_BLUE);
                        line = scanner.nextLine();
                    }
                } else if (line.equalsIgnoreCase("join") || line.equalsIgnoreCase("j")) {
                    try {
                        int gameNumber;
                        System.out.println("\n" + EMPTY + "  (if you don't know your " + SET_TEXT_COLOR_RED + "game # " + SET_TEXT_COLOR_BLUE + "type " + SET_TEXT_COLOR_RED + "\"v\"" + SET_TEXT_COLOR_BLUE + ")");
                        System.out.println("\n" + EMPTY + "  game number:\n");
                        if (scanner.hasNextInt()) {
                            gameNumber = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println("\n" + EMPTY + "  player color:\n");
                            playerColor = scanner.nextLine();
                            line = "refresh";
                            if (playerColor.equalsIgnoreCase("white") || playerColor.equalsIgnoreCase("w")) {
                                JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", gameNumber);
                                serverFacade.joinGame(joinGameRequest);
                                serverFacade.createWSConnection(this, gameNumber);
                                while (true) {
                                    if (line.equalsIgnoreCase("refresh") || line.equalsIgnoreCase("r")) {
                                        System.out.println("\n" + EMPTY + "  ------------Chess------------      Playing game " + SET_TEXT_COLOR_RED + gameNumber + SET_TEXT_COLOR_BLUE + " as " + SET_TEXT_COLOR_RED + "white" + SET_TEXT_COLOR_BLUE + " (type " + SET_TEXT_COLOR_RED + "\"h\"" + SET_TEXT_COLOR_BLUE + " for more commands)" + SET_TEXT_COLOR_BLUE);
                                        printBoard(chessGame.getBoard());
                                        line = scanner.nextLine();
                                    } else if (line.equalsIgnoreCase("move") || line.equalsIgnoreCase("m")) {
                                        System.out.println("\n" + EMPTY + "  piece coordinates (letter then number - a1, b5, etc.):\n");
                                        String startSquare = scanner.nextLine();
                                        if (!(startSquare.length() != 2 || (startSquare.charAt(0) != 'a' && startSquare.charAt(0) != 'b' && startSquare.charAt(0) != 'c' && startSquare.charAt(0) != 'd' && startSquare.charAt(0) != 'e' && startSquare.charAt(0) != 'f' && startSquare.charAt(0) != 'g' && startSquare.charAt(0) != 'h' && startSquare.charAt(0) != 'A' && startSquare.charAt(0) != 'B' && startSquare.charAt(0) != 'C' && startSquare.charAt(0) != 'D' && startSquare.charAt(0) != 'E' && startSquare.charAt(0) != 'F' && startSquare.charAt(0) != 'G' && startSquare.charAt(0) != 'H') || (startSquare.charAt(1) != '1' && startSquare.charAt(1) != '2' && startSquare.charAt(1) != '3' && startSquare.charAt(1) != '4' && startSquare.charAt(1) != '5' && startSquare.charAt(1) != '6' && startSquare.charAt(1) != '7' && startSquare.charAt(1) != '8'))) {
                                            System.out.println("\n" + EMPTY + "  move coordinates:\n");
                                            String endSquare = scanner.nextLine();
                                            if (!(endSquare.length() != 2 || (endSquare.charAt(0) != 'a' && endSquare.charAt(0) != 'b' && endSquare.charAt(0) != 'c' && endSquare.charAt(0) != 'd' && endSquare.charAt(0) != 'e' && endSquare.charAt(0) != 'f' && endSquare.charAt(0) != 'g' && endSquare.charAt(0) != 'h' && endSquare.charAt(0) != 'A' && endSquare.charAt(0) != 'B' && endSquare.charAt(0) != 'C' && endSquare.charAt(0) != 'D' && endSquare.charAt(0) != 'E' && endSquare.charAt(0) != 'F' && endSquare.charAt(0) != 'G' && endSquare.charAt(0) != 'H') || (endSquare.charAt(1) != '1' && endSquare.charAt(1) != '2' && endSquare.charAt(1) != '3' && endSquare.charAt(1) != '4' && endSquare.charAt(1) != '5' && endSquare.charAt(1) != '6' && endSquare.charAt(1) != '7' && endSquare.charAt(1) != '8'))) {
                                                int i = 0;
                                                int j = 0;
                                                switch (startSquare.charAt(0)) {
                                                    case 'a', 'A':
                                                        j = 0;
                                                        break;
                                                    case 'b', 'B':
                                                        j = 1;
                                                        break;
                                                    case 'c', 'C':
                                                        j = 2;
                                                        break;
                                                    case 'd', 'D':
                                                        j = 3;
                                                        break;
                                                    case 'e', 'E':
                                                        j = 4;
                                                        break;
                                                    case 'f', 'F':
                                                        j = 5;
                                                        break;
                                                    case 'g', 'G':
                                                        j = 6;
                                                        break;
                                                    case 'h', 'H':
                                                        j = 7;
                                                        break;
                                                }
                                                switch (startSquare.charAt(1)) {
                                                    case '1':
                                                        i = 0;
                                                        break;
                                                    case '2':
                                                        i = 1;
                                                        break;
                                                    case '3':
                                                        i = 2;
                                                        break;
                                                    case '4':
                                                        i = 3;
                                                        break;
                                                    case '5':
                                                        i = 4;
                                                        break;
                                                    case '6':
                                                        i = 5;
                                                        break;
                                                    case '7':
                                                        i = 6;
                                                        break;
                                                    case '8':
                                                        i = 7;
                                                        break;
                                                }
                                                ChessPosition startPosition = new ChessPosition(i + 1, j + 1);
                                                switch (startSquare.charAt(0)) {
                                                    case 'a', 'A':
                                                        j = 0;
                                                        break;
                                                    case 'b', 'B':
                                                        j = 1;
                                                        break;
                                                    case 'c', 'C':
                                                        j = 2;
                                                        break;
                                                    case 'd', 'D':
                                                        j = 3;
                                                        break;
                                                    case 'e', 'E':
                                                        j = 4;
                                                        break;
                                                    case 'f', 'F':
                                                        j = 5;
                                                        break;
                                                    case 'g', 'G':
                                                        j = 6;
                                                        break;
                                                    case 'h', 'H':
                                                        j = 7;
                                                        break;
                                                }
                                                switch (endSquare.charAt(1)) {
                                                    case '1':
                                                        i = 0;
                                                        break;
                                                    case '2':
                                                        i = 1;
                                                        break;
                                                    case '3':
                                                        i = 2;
                                                        break;
                                                    case '4':
                                                        i = 3;
                                                        break;
                                                    case '5':
                                                        i = 4;
                                                        break;
                                                    case '6':
                                                        i = 5;
                                                        break;
                                                    case '7':
                                                        i = 6;
                                                        break;
                                                    case '8':
                                                        i = 7;
                                                        break;
                                                }
                                                ChessPosition endPosition = new ChessPosition(i + 1, j + 1);
                                                ChessMove chessMove = new ChessMove(startPosition, endPosition, null);
                                                chessGame.makeMove(chessMove);
                                                chessGame.setTeamTurn(ChessGame.TeamColor.BLACK);
                                                UpdateGameRequest updateGameRequest = new UpdateGameRequest(gameNumber, chessGame);
                                                serverFacade.updateGame(updateGameRequest);
                                                ChessGame.TeamColor teamColor = ChessGame.TeamColor.WHITE;
                                                serverFacade.sendMove(gameNumber, chessMove, teamColor);
                                                line = "refresh";
                                            }
                                        }
                                    } else if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h")) {
                                        System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "\"h\" " + SET_TEXT_COLOR_BLUE + " (help)");
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"v\" " + SET_TEXT_COLOR_BLUE + " (view moves)" + SET_TEXT_COLOR_BLUE);
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"m\" " + SET_TEXT_COLOR_BLUE + " (move piece)" + SET_TEXT_COLOR_BLUE);
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"r\" " + SET_TEXT_COLOR_BLUE + " (refresh board)" + SET_TEXT_COLOR_BLUE);
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"f\" " + SET_TEXT_COLOR_BLUE + " (forfeit)" + SET_TEXT_COLOR_BLUE);
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"l\" " + SET_TEXT_COLOR_BLUE + " (leave game)\n" + SET_TEXT_COLOR_BLUE);
                                        line = scanner.nextLine();
                                    } else if (line.equalsIgnoreCase("view") || line.equalsIgnoreCase("v")) {
                                        System.out.println("\n" + EMPTY + "  piece coordinates (letter then number - a1, b5, etc.):\n");
                                        String startSquare = scanner.nextLine();
                                        if (!(startSquare.length() != 2 || (startSquare.charAt(0) != 'a' && startSquare.charAt(0) != 'b' && startSquare.charAt(0) != 'c' && startSquare.charAt(0) != 'd' && startSquare.charAt(0) != 'e' && startSquare.charAt(0) != 'f' && startSquare.charAt(0) != 'g' && startSquare.charAt(0) != 'h' && startSquare.charAt(0) != 'A' && startSquare.charAt(0) != 'B' && startSquare.charAt(0) != 'C' && startSquare.charAt(0) != 'D' && startSquare.charAt(0) != 'E' && startSquare.charAt(0) != 'F' && startSquare.charAt(0) != 'G' && startSquare.charAt(0) != 'H') || (startSquare.charAt(1) != '1' && startSquare.charAt(1) != '2' && startSquare.charAt(1) != '3' && startSquare.charAt(1) != '4' && startSquare.charAt(1) != '5' && startSquare.charAt(1) != '6' && startSquare.charAt(1) != '7' && startSquare.charAt(1) != '8'))) {
                                            int i = 0;
                                            int j = 0;
                                            switch (startSquare.charAt(0)) {
                                                case 'a', 'A':
                                                    j = 0;
                                                    break;
                                                case 'b', 'B':
                                                    j = 1;
                                                    break;
                                                case 'c', 'C':
                                                    j = 2;
                                                    break;
                                                case 'd', 'D':
                                                    j = 3;
                                                    break;
                                                case 'e', 'E':
                                                    j = 4;
                                                    break;
                                                case 'f', 'F':
                                                    j = 5;
                                                    break;
                                                case 'g', 'G':
                                                    j = 6;
                                                    break;
                                                case 'h', 'H':
                                                    j = 7;
                                                    break;
                                            }
                                            switch (startSquare.charAt(1)) {
                                                case '1':
                                                    i = 0;
                                                    break;
                                                case '2':
                                                    i = 1;
                                                    break;
                                                case '3':
                                                    i = 2;
                                                    break;
                                                case '4':
                                                    i = 3;
                                                    break;
                                                case '5':
                                                    i = 4;
                                                    break;
                                                case '6':
                                                    i = 5;
                                                    break;
                                                case '7':
                                                    i = 6;
                                                    break;
                                                case '8':
                                                    i = 7;
                                                    break;
                                            }
                                            System.out.println("\n" + EMPTY + "  ------------Chess------------      " + "Available Moves\n" + SET_TEXT_COLOR_BLUE);
                                        }
                                    } else if (line.equalsIgnoreCase("forfeit") || line.equalsIgnoreCase("f")) {
                                        System.out.println("\n + EMPTY + Type \"f\" again to confirm forfeit:\n");
                                        line = scanner.nextLine();
                                        if (line.equalsIgnoreCase("forfeit") || line.equalsIgnoreCase("f")) {
                                            System.out.println("\n" + EMPTY + "  ------------Chess------------      You forfeited the game!\n");
                                        }
                                    } else if (line.equalsIgnoreCase("leave") || line.equalsIgnoreCase("l")) {
                                        LeaveGameRequest leaveGameRequest = new LeaveGameRequest("WHITE", gameNumber);
                                        serverFacade.leaveGame(leaveGameRequest);
                                        System.out.println("\n" + EMPTY + "  ------------Chess------------      You left the game!\n");
                                        line = scanner.nextLine();
                                        break;
                                    } else {
                                        System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "Invalid command!\n" + SET_TEXT_COLOR_BLUE);
                                        line = scanner.nextLine();
                                    }
                                }
                            } else if (playerColor.equalsIgnoreCase("black") || playerColor.equalsIgnoreCase("b")) {
                                JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", gameNumber);
                                serverFacade.joinGame(joinGameRequest);
                                serverFacade.createWSConnection(this, gameNumber);
                                while (true) {
                                    if (line.equalsIgnoreCase("refresh") || line.equalsIgnoreCase("r")) {
                                        System.out.println("\n" + EMPTY + "  ------------Chess------------      Playing game " + SET_TEXT_COLOR_RED + gameNumber + SET_TEXT_COLOR_BLUE + " as " + SET_TEXT_COLOR_RED + "black" + SET_TEXT_COLOR_BLUE + " (type " + SET_TEXT_COLOR_RED + "\"h\"" + SET_TEXT_COLOR_BLUE + " for more commands)" + SET_TEXT_COLOR_BLUE);
                                        printBoardFlipped(chessGame.getBoard());
                                        line = scanner.nextLine();
                                    } else if (line.equalsIgnoreCase("move") || line.equalsIgnoreCase("m")) {
                                        System.out.println("\n" + EMPTY + "  piece coordinates (letter then number - a1, b5, etc.):\n");
                                        String startSquare = scanner.nextLine();
                                        if (!(startSquare.length() != 2 || (startSquare.charAt(0) != 'a' && startSquare.charAt(0) != 'b' && startSquare.charAt(0) != 'c' && startSquare.charAt(0) != 'd' && startSquare.charAt(0) != 'e' && startSquare.charAt(0) != 'f' && startSquare.charAt(0) != 'g' && startSquare.charAt(0) != 'h' && startSquare.charAt(0) != 'A' && startSquare.charAt(0) != 'B' && startSquare.charAt(0) != 'C' && startSquare.charAt(0) != 'D' && startSquare.charAt(0) != 'E' && startSquare.charAt(0) != 'F' && startSquare.charAt(0) != 'G' && startSquare.charAt(0) != 'H') || (startSquare.charAt(1) != '1' && startSquare.charAt(1) != '2' && startSquare.charAt(1) != '3' && startSquare.charAt(1) != '4' && startSquare.charAt(1) != '5' && startSquare.charAt(1) != '6' && startSquare.charAt(1) != '7' && startSquare.charAt(1) != '8'))) {
                                            System.out.println("\n" + EMPTY + "  move coordinates:\n");
                                            String endSquare = scanner.nextLine();
                                            if (!(endSquare.length() != 2 || (endSquare.charAt(0) != 'a' && endSquare.charAt(0) != 'b' && endSquare.charAt(0) != 'c' && endSquare.charAt(0) != 'd' && endSquare.charAt(0) != 'e' && endSquare.charAt(0) != 'f' && endSquare.charAt(0) != 'g' && endSquare.charAt(0) != 'h' && endSquare.charAt(0) != 'A' && endSquare.charAt(0) != 'B' && endSquare.charAt(0) != 'C' && endSquare.charAt(0) != 'D' && endSquare.charAt(0) != 'E' && endSquare.charAt(0) != 'F' && endSquare.charAt(0) != 'G' && endSquare.charAt(0) != 'H') || (endSquare.charAt(1) != '1' && endSquare.charAt(1) != '2' && endSquare.charAt(1) != '3' && endSquare.charAt(1) != '4' && endSquare.charAt(1) != '5' && endSquare.charAt(1) != '6' && endSquare.charAt(1) != '7' && endSquare.charAt(1) != '8'))) {
                                                int i = 0;
                                                int j = 0;
                                                switch (startSquare.charAt(0)) {
                                                    case 'a', 'A':
                                                        j = 0;
                                                        break;
                                                    case 'b', 'B':
                                                        j = 1;
                                                        break;
                                                    case 'c', 'C':
                                                        j = 2;
                                                        break;
                                                    case 'd', 'D':
                                                        j = 3;
                                                        break;
                                                    case 'e', 'E':
                                                        j = 4;
                                                        break;
                                                    case 'f', 'F':
                                                        j = 5;
                                                        break;
                                                    case 'g', 'G':
                                                        j = 6;
                                                        break;
                                                    case 'h', 'H':
                                                        j = 7;
                                                        break;
                                                }
                                                switch (startSquare.charAt(1)) {
                                                    case '1':
                                                        i = 0;
                                                        break;
                                                    case '2':
                                                        i = 1;
                                                        break;
                                                    case '3':
                                                        i = 2;
                                                        break;
                                                    case '4':
                                                        i = 3;
                                                        break;
                                                    case '5':
                                                        i = 4;
                                                        break;
                                                    case '6':
                                                        i = 5;
                                                        break;
                                                    case '7':
                                                        i = 6;
                                                        break;
                                                    case '8':
                                                        i = 7;
                                                        break;
                                                }
                                                ChessPosition startPosition = new ChessPosition(i + 1, j + 1);
                                                switch (startSquare.charAt(0)) {
                                                    case 'a', 'A':
                                                        j = 0;
                                                        break;
                                                    case 'b', 'B':
                                                        j = 1;
                                                        break;
                                                    case 'c', 'C':
                                                        j = 2;
                                                        break;
                                                    case 'd', 'D':
                                                        j = 3;
                                                        break;
                                                    case 'e', 'E':
                                                        j = 4;
                                                        break;
                                                    case 'f', 'F':
                                                        j = 5;
                                                        break;
                                                    case 'g', 'G':
                                                        j = 6;
                                                        break;
                                                    case 'h', 'H':
                                                        j = 7;
                                                        break;
                                                }
                                                switch (endSquare.charAt(1)) {
                                                    case '1':
                                                        i = 0;
                                                        break;
                                                    case '2':
                                                        i = 1;
                                                        break;
                                                    case '3':
                                                        i = 2;
                                                        break;
                                                    case '4':
                                                        i = 3;
                                                        break;
                                                    case '5':
                                                        i = 4;
                                                        break;
                                                    case '6':
                                                        i = 5;
                                                        break;
                                                    case '7':
                                                        i = 6;
                                                        break;
                                                    case '8':
                                                        i = 7;
                                                        break;
                                                }
                                                ChessPosition endPosition = new ChessPosition(i + 1, j + 1);
                                                ChessMove chessMove = new ChessMove(startPosition, endPosition, null);
                                                chessGame.makeMove(chessMove);
                                                chessGame.setTeamTurn(ChessGame.TeamColor.WHITE);
                                                UpdateGameRequest updateGameRequest = new UpdateGameRequest(gameNumber, chessGame);
                                                serverFacade.updateGame(updateGameRequest);
                                                ChessGame.TeamColor teamColor = ChessGame.TeamColor.BLACK;
                                                serverFacade.sendMove(gameNumber, chessMove, teamColor);
                                                line = "refresh";
                                            }
                                        }
                                    } else if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h")) {
                                        System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "\"h\" " + SET_TEXT_COLOR_BLUE + " (help)");
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"v\" " + SET_TEXT_COLOR_BLUE + " (view moves)" + SET_TEXT_COLOR_BLUE);
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"m\" " + SET_TEXT_COLOR_BLUE + " (move piece)" + SET_TEXT_COLOR_BLUE);
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"r\" " + SET_TEXT_COLOR_BLUE + " (refresh board)" + SET_TEXT_COLOR_BLUE);
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"f\" " + SET_TEXT_COLOR_BLUE + " (forfeit)" + SET_TEXT_COLOR_BLUE);
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"l\" " + SET_TEXT_COLOR_BLUE + " (leave game)\n" + SET_TEXT_COLOR_BLUE);
                                        line = scanner.nextLine();
                                    } else if (line.equalsIgnoreCase("view") || line.equalsIgnoreCase("v")) {
                                        System.out.println("\n" + EMPTY + "  piece coordinates (letter then number - a1, b5, etc.):\n");
                                        String startSquare = scanner.nextLine();
                                        if (!(startSquare.length() != 2 || (startSquare.charAt(0) != 'a' && startSquare.charAt(0) != 'b' && startSquare.charAt(0) != 'c' && startSquare.charAt(0) != 'd' && startSquare.charAt(0) != 'e' && startSquare.charAt(0) != 'f' && startSquare.charAt(0) != 'g' && startSquare.charAt(0) != 'h' && startSquare.charAt(0) != 'A' && startSquare.charAt(0) != 'B' && startSquare.charAt(0) != 'C' && startSquare.charAt(0) != 'D' && startSquare.charAt(0) != 'E' && startSquare.charAt(0) != 'F' && startSquare.charAt(0) != 'G' && startSquare.charAt(0) != 'H') || (startSquare.charAt(1) != '1' && startSquare.charAt(1) != '2' && startSquare.charAt(1) != '3' && startSquare.charAt(1) != '4' && startSquare.charAt(1) != '5' && startSquare.charAt(1) != '6' && startSquare.charAt(1) != '7' && startSquare.charAt(1) != '8'))) {
                                            int i = 0;
                                            int j = 0;
                                            switch (startSquare.charAt(0)) {
                                                case 'a', 'A':
                                                    j = 0;
                                                    break;
                                                case 'b', 'B':
                                                    j = 1;
                                                    break;
                                                case 'c', 'C':
                                                    j = 2;
                                                    break;
                                                case 'd', 'D':
                                                    j = 3;
                                                    break;
                                                case 'e', 'E':
                                                    j = 4;
                                                    break;
                                                case 'f', 'F':
                                                    j = 5;
                                                    break;
                                                case 'g', 'G':
                                                    j = 6;
                                                    break;
                                                case 'h', 'H':
                                                    j = 7;
                                                    break;
                                            }
                                            switch (startSquare.charAt(1)) {
                                                case '1':
                                                    i = 0;
                                                    break;
                                                case '2':
                                                    i = 1;
                                                    break;
                                                case '3':
                                                    i = 2;
                                                    break;
                                                case '4':
                                                    i = 3;
                                                    break;
                                                case '5':
                                                    i = 4;
                                                    break;
                                                case '6':
                                                    i = 5;
                                                    break;
                                                case '7':
                                                    i = 6;
                                                    break;
                                                case '8':
                                                    i = 7;
                                                    break;
                                            }
                                            System.out.println("\n" + EMPTY + "  ------------Chess------------      " + "Available Moves\n" + SET_TEXT_COLOR_BLUE);
                                        }
                                    } else if (line.equalsIgnoreCase("forfeit") || line.equalsIgnoreCase("f")) {
                                        System.out.println("\n + EMPTY + Type \"f\" again to confirm forfeit:\n");
                                        line = scanner.nextLine();
                                        if (line.equalsIgnoreCase("forfeit") || line.equalsIgnoreCase("f")) {
                                            System.out.println("\n" + EMPTY + "  ------------Chess------------      You forfeited the game!\n");
                                        }
                                    } else if (line.equalsIgnoreCase("leave") || line.equalsIgnoreCase("l")) {
                                        LeaveGameRequest leaveGameRequest = new LeaveGameRequest("BLACK", gameNumber);
                                        serverFacade.leaveGame(leaveGameRequest);
                                        System.out.println("\n" + EMPTY + "  ------------Chess------------      You left the game!\n");
                                        line = scanner.nextLine();
                                        break;
                                    } else {
                                        System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "Invalid command!\n" + SET_TEXT_COLOR_BLUE);
                                        line = scanner.nextLine();
                                    }
                                }
                            } else {
                                throw new Exception();
                            }
                        } else {
                            line = scanner.nextLine();
                        }
                    } catch (Exception e) {
                        System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "Sorry, this game could not be joined!\n" + SET_TEXT_COLOR_BLUE);
                        line = scanner.nextLine();
                    }
                } else if (line.equalsIgnoreCase("spectate") || line.equalsIgnoreCase("s")) {
                    try {
                        int gameNumber;
                        System.out.println("\n" + EMPTY + "  game number:\n");
                        if (scanner.hasNextInt()) {
                            gameNumber = scanner.nextInt();
                            scanner.nextLine();
                        } else {
                            throw new Exception();
                        }
                        line = "refresh";
                        //serverFacade.throwIfGameNotExists(gameNumber);
                        serverFacade.createWSConnection(this, gameNumber);
                        while (true) {
                            if (line.equalsIgnoreCase("refresh") || line.equalsIgnoreCase("r")) {
                                System.out.println("\n" + EMPTY + "  ------------Chess------------      Spectating game " + SET_TEXT_COLOR_RED + gameNumber + SET_TEXT_COLOR_BLUE + " (type " + SET_TEXT_COLOR_RED + "\"h\"" + SET_TEXT_COLOR_BLUE + " for more commands)" + SET_TEXT_COLOR_BLUE);
                                printBoard(chessGame.getBoard());
                                line = scanner.nextLine();
                            } else if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h")) {
                                System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "\"h\" " + SET_TEXT_COLOR_BLUE + " (help)");
                                System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"r\" " + SET_TEXT_COLOR_BLUE + " (refresh board)" + SET_TEXT_COLOR_BLUE);
                                System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"l\" " + SET_TEXT_COLOR_BLUE + " (leave game)\n" + SET_TEXT_COLOR_BLUE);
                                line = scanner.nextLine();
                            } else if (line.equalsIgnoreCase("leave") || line.equalsIgnoreCase("l")) {
                                LeaveGameRequest leaveGameRequest = new LeaveGameRequest("", gameNumber);
                                serverFacade.leaveGame(leaveGameRequest);
                                System.out.println("\n" + EMPTY + "  ------------Chess------------      You left the game!\n");
                                line = scanner.nextLine();
                                break;
                            } else {
                                System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "Invalid command!\n" + SET_TEXT_COLOR_BLUE);
                                line = scanner.nextLine();
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "Sorry, this game could not be spectated!\n" + SET_TEXT_COLOR_BLUE);
                        line = scanner.nextLine();
                    }
                } else if (line.equalsIgnoreCase("logout") || line.equalsIgnoreCase("l")) {
                    try {
                        serverFacade.logoutUser();
                        System.out.println("\n" + EMPTY + "  ------------Chess------------      You are now signed out!\n");
                        line = scanner.nextLine();
                        break;
                    } catch (Exception e) {
                        System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "Sorry, you couldn't be logged out!\n" + SET_TEXT_COLOR_BLUE);
                        line = scanner.nextLine();
                    }
                } else if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("q")) {
                    try {
                        serverFacade.logoutUser();
                        return;
                    } catch (Exception e) {
                        return;
                    }
                } else {
                    System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "Invalid command, please try again!\n" + SET_TEXT_COLOR_BLUE);
                    line = scanner.nextLine();
                }
            }
        }
    }

    public static void printBoard(ChessBoard board) {
        System.out.println();
        for (int i = 7; i >= 0 ; i--) {
            System.out.print(EMPTY + SET_TEXT_COLOR_BLUE);
            System.out.print(i + 1);
            System.out.print(" ");
            for (int j = 0; j < 8; j++) {
                String pieceColor = "";
                String pieceType = "";
                ChessPosition position = new ChessPosition(i + 1, j + 1);
                if (board.getPiece(position) != null) {
                    switch (board.getPiece(position).getPieceType()) {
                        case ChessPiece.PieceType.ROOK:
                            pieceType = switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE -> {
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    yield BLACK_ROOK;
                                }
                                case ChessGame.TeamColor.BLACK -> {
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    yield BLACK_ROOK;
                                }
                            };
                            break;
                        case ChessPiece.PieceType.KNIGHT:
                            pieceType = switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE -> {
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    yield BLACK_KNIGHT;
                                }
                                case ChessGame.TeamColor.BLACK -> {
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    yield BLACK_KNIGHT;
                                }
                            };
                            break;
                        case ChessPiece.PieceType.BISHOP:
                            pieceType = switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE -> {
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    yield BLACK_BISHOP;
                                }
                                case ChessGame.TeamColor.BLACK -> {
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    yield BLACK_BISHOP;
                                }
                            };
                            break;
                        case ChessPiece.PieceType.KING:
                            pieceType = switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE -> {
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    yield BLACK_KING;
                                }
                                case ChessGame.TeamColor.BLACK -> {
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    yield BLACK_KING;
                                }
                            };
                            break;
                        case ChessPiece.PieceType.QUEEN:
                            pieceType = switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE -> {
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    yield BLACK_QUEEN;
                                }
                                case ChessGame.TeamColor.BLACK -> {
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    yield BLACK_QUEEN;
                                }
                            };
                            break;
                        case ChessPiece.PieceType.PAWN:
                            pieceType = switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE -> {
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    yield BLACK_PAWN;
                                }
                                case ChessGame.TeamColor.BLACK -> {
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    yield BLACK_PAWN;
                                }
                            };
                            break;
                    }
                } else {
                    pieceType = " " + EMPTY + " ";
                }
                if (i % 2 == 1) {
                    if (j % 2 == 0) {
                        System.out.print(SET_BG_COLOR_DARK_BLUE + pieceColor + pieceType);
                    } else {
                        System.out.print(SET_BG_COLOR_BLUE + pieceColor + pieceType);
                    }
                } else {
                    if (j % 2 == 1) {
                        System.out.print(SET_BG_COLOR_DARK_BLUE + pieceColor + pieceType);
                    } else {
                        System.out.print(SET_BG_COLOR_BLUE + pieceColor + pieceType);
                    }
                }
                if (j == 7) {
                    System.out.println(SET_BG_COLOR_BLACK);
                }
            }
            if (i == 0) {
                System.out.println(SET_TEXT_COLOR_BLUE + "     A" + EMPTY + " B" + EMPTY + " C" + EMPTY + " D" + EMPTY + " E" + EMPTY + " F" + EMPTY + " G" + EMPTY + " H");
            }
        }
        System.out.println();
    }

    public static void printBoardFlipped(ChessBoard board) {
        System.out.println();
        for (int i = 0; i < 8 ; i++) {
            System.out.print(EMPTY + SET_TEXT_COLOR_BLUE);
            System.out.print(i + 1);
            System.out.print(" ");
            for (int j = 7; j >= 0; j--) {
                String pieceColor = "";
                String pieceType = "";
                ChessPosition position = new ChessPosition(i + 1, j + 1);
                if (board.getPiece(position) != null) {
                    switch (board.getPiece(position).getPieceType()) {
                        case ChessPiece.PieceType.ROOK:
                            pieceType = switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE -> {
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    yield BLACK_ROOK;
                                }
                                case ChessGame.TeamColor.BLACK -> {
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    yield BLACK_ROOK;
                                }
                            };
                            break;
                        case ChessPiece.PieceType.KNIGHT:
                            pieceType = switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE -> {
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    yield BLACK_KNIGHT;
                                }
                                case ChessGame.TeamColor.BLACK -> {
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    yield BLACK_KNIGHT;
                                }
                            };
                            break;
                        case ChessPiece.PieceType.BISHOP:
                            pieceType = switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE -> {
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    yield BLACK_BISHOP;
                                }
                                case ChessGame.TeamColor.BLACK -> {
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    yield BLACK_BISHOP;
                                }
                            };
                            break;
                        case ChessPiece.PieceType.KING:
                            pieceType = switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE -> {
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    yield BLACK_KING;
                                }
                                case ChessGame.TeamColor.BLACK -> {
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    yield BLACK_KING;
                                }
                            };
                            break;
                        case ChessPiece.PieceType.QUEEN:
                            pieceType = switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE -> {
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    yield BLACK_QUEEN;
                                }
                                case ChessGame.TeamColor.BLACK -> {
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    yield BLACK_QUEEN;
                                }
                            };
                            break;
                        case ChessPiece.PieceType.PAWN:
                            pieceType = switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE -> {
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    yield BLACK_PAWN;
                                }
                                case ChessGame.TeamColor.BLACK -> {
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    yield BLACK_PAWN;
                                }
                            };
                            break;
                    }
                } else {
                    pieceType = " " + EMPTY + " ";
                }
                if (i % 2 == 1) {
                    if (j % 2 == 0) {
                        System.out.print(SET_BG_COLOR_DARK_BLUE + pieceColor + pieceType);
                    } else {
                        System.out.print(SET_BG_COLOR_BLUE + pieceColor + pieceType);
                    }
                } else {
                    if (j % 2 == 1) {
                        System.out.print(SET_BG_COLOR_DARK_BLUE + pieceColor + pieceType);
                    } else {
                        System.out.print(SET_BG_COLOR_BLUE + pieceColor + pieceType);
                    }
                }
                if (j == 0) {
                    System.out.println(SET_BG_COLOR_BLACK);
                }
            }
            if (i == 7) {
                System.out.println(SET_TEXT_COLOR_BLUE + "     H" + EMPTY + " G" + EMPTY + " F" + EMPTY + " E" + EMPTY + " D" + EMPTY + " C" + EMPTY + " B" + EMPTY + " A");
            }
        }
        System.out.println();
    }

    public void onMessage(String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            chessGame = new Gson().fromJson(serverMessage.game, ChessGame.class);
        } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            System.out.println(EMPTY + "  " + serverMessage.message);
            if (playerColor.equals("black") || playerColor.equals("b")) {
                printBoardFlipped(chessGame.getBoard());
            } else {
                printBoard(chessGame.getBoard());
            }
        }
    }
}

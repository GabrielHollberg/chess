package client;

import chess.*;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.ListGamesResult;
import server.ServerFacade;

import java.awt.*;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ClientMain {
    public static void main(String[] args) {
        ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
        String username;
        Scanner scanner = new Scanner(System.in);
        String line;
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
                        String playerColor;
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
                                serverFacade.createWSConnection(joinGameRequest.gameID());
                                while (true) {
                                    if (line.equalsIgnoreCase("refresh") || line.equalsIgnoreCase("r")) {
                                        ChessBoard board = new ChessBoard();
                                        board.resetBoard();
                                        System.out.println("\n" + EMPTY + "  ------------Chess------------      Playing game " + SET_TEXT_COLOR_RED + gameNumber + SET_TEXT_COLOR_BLUE + " as " + SET_TEXT_COLOR_RED + "white" + SET_TEXT_COLOR_BLUE + " (type " + SET_TEXT_COLOR_RED + "\"h\"" + SET_TEXT_COLOR_BLUE + " for more commands)\n" + SET_TEXT_COLOR_BLUE);
                                        printBoard(board);
                                        line = scanner.nextLine();
                                    } else if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h")) {
                                        System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "\"h\" " + SET_TEXT_COLOR_BLUE + " (help)");
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"v\" " + SET_TEXT_COLOR_BLUE + " (view moves)" + SET_TEXT_COLOR_BLUE);
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"m\" " + SET_TEXT_COLOR_BLUE + " (move piece)" + SET_TEXT_COLOR_BLUE);
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"r\" " + SET_TEXT_COLOR_BLUE + " (refresh board)" + SET_TEXT_COLOR_BLUE);
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"f\" " + SET_TEXT_COLOR_BLUE + " (forfeit)" + SET_TEXT_COLOR_BLUE);
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"l\" " + SET_TEXT_COLOR_BLUE + " (leave game)\n" + SET_TEXT_COLOR_BLUE);
                                        line = scanner.nextLine();
                                    } else if (line.equalsIgnoreCase("leave") || line.equalsIgnoreCase("l")) {
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
                                serverFacade.createWSConnection(joinGameRequest.gameID());
                                while (true) {
                                    if (line.equalsIgnoreCase("refresh") || line.equalsIgnoreCase("r")) {
                                        ChessBoard board = new ChessBoard();
                                        board.resetBoard();
                                        System.out.println("\n" + EMPTY + "  ------------Chess------------      Playing game " + SET_TEXT_COLOR_RED + gameNumber + SET_TEXT_COLOR_BLUE + " as " + SET_TEXT_COLOR_RED + "black" + SET_TEXT_COLOR_BLUE + " (type " + SET_TEXT_COLOR_RED + "\"h\"" + SET_TEXT_COLOR_BLUE + " for more commands)\n" + SET_TEXT_COLOR_BLUE);
                                        printBoardFlipped(board);
                                        line = scanner.nextLine();
                                    } else if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h")) {
                                        System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "\"h\" " + SET_TEXT_COLOR_BLUE + " (help)");
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"v\" " + SET_TEXT_COLOR_BLUE + " (view moves)" + SET_TEXT_COLOR_BLUE);
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"m\" " + SET_TEXT_COLOR_BLUE + " (move piece)" + SET_TEXT_COLOR_BLUE);
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"r\" " + SET_TEXT_COLOR_BLUE + " (refresh board)" + SET_TEXT_COLOR_BLUE);
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"f\" " + SET_TEXT_COLOR_BLUE + " (forfeit)" + SET_TEXT_COLOR_BLUE);
                                        System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"l\" " + SET_TEXT_COLOR_BLUE + " (leave game)\n" + SET_TEXT_COLOR_BLUE);
                                        line = scanner.nextLine();
                                    } else if (line.equalsIgnoreCase("leave") || line.equalsIgnoreCase("l")) {
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
                        while (true) {
                            if (line.equalsIgnoreCase("refresh") || line.equalsIgnoreCase("r")) {
                                ChessBoard board = new ChessBoard();
                                board.resetBoard();
                                System.out.println("\n" + EMPTY + "  ------------Chess------------      Spectating game " + SET_TEXT_COLOR_RED + gameNumber + SET_TEXT_COLOR_BLUE + " (type " + SET_TEXT_COLOR_RED + "\"h\"" + SET_TEXT_COLOR_BLUE + " for more commands)\n" + SET_TEXT_COLOR_BLUE);
                                printBoard(board);
                                line = scanner.nextLine();
                            } else if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h")) {
                                System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "\"h\" " + SET_TEXT_COLOR_BLUE + " (help)");
                                System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"r\" " + SET_TEXT_COLOR_BLUE + " (refresh board)" + SET_TEXT_COLOR_BLUE);
                                System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"l\" " + SET_TEXT_COLOR_BLUE + " (leave game)\n" + SET_TEXT_COLOR_BLUE);
                                line = scanner.nextLine();
                            } else if (line.equalsIgnoreCase("leave") || line.equalsIgnoreCase("l")) {
                                System.out.println("\n" + EMPTY + "  ------------Chess------------      You left the game!\n");
                                line = scanner.nextLine();
                                break;
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
}

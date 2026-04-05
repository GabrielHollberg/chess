package client;

import chess.*;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.ListGamesResult;
import result.LoginResult;
import result.RegisterResult;
import server.ServerFacade;

import java.awt.*;
import java.util.List;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ClientMain {
    public static void main(String[] args) {
        ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
        String username = "";
        Scanner scanner = new Scanner(System.in);
        String line = "";
        System.out.println(SET_TEXT_COLOR_BLUE + SET_BG_COLOR_BLACK);
        System.out.println(EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_BLUE + "(type " + SET_TEXT_COLOR_RED + "\"help\"" + SET_TEXT_COLOR_BLUE + " or " + SET_TEXT_COLOR_RED + "\"h\"" + SET_TEXT_COLOR_BLUE + " for a list of commands)\n" + SET_TEXT_COLOR_BLUE);
        line = scanner.nextLine();
        while (true) {
            while (true) {
                if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h")) {
                    System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "\"help\" " + SET_TEXT_COLOR_BLUE + "or " + SET_TEXT_COLOR_RED + "\"h\"" + SET_TEXT_COLOR_BLUE);
                    System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"register\" " + SET_TEXT_COLOR_BLUE + "or " + SET_TEXT_COLOR_RED + "\"r\"" + SET_TEXT_COLOR_BLUE);
                    System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"login\" " + SET_TEXT_COLOR_BLUE + "or " + SET_TEXT_COLOR_RED + "\"l\"" + SET_TEXT_COLOR_BLUE);
                    System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"quit\" " + SET_TEXT_COLOR_BLUE + "or " + SET_TEXT_COLOR_RED + "\"q\"\n" + SET_TEXT_COLOR_BLUE);
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
                        RegisterResult registerResult = serverFacade.registerUser(registerRequest);
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
                        LoginResult loginResult = serverFacade.loginUser(loginRequest);
                        break;
                    } catch (Exception e) {
                        System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "Incorrect login, please try again!\n" + SET_TEXT_COLOR_BLUE);
                        line = scanner.nextLine();
                    }
                } else if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("q")) {
                    return;
                } else {
                    System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "Invalid command, please try again!\n" + SET_TEXT_COLOR_BLUE);
                    line = scanner.nextLine();
                }
            }
            System.out.println(SET_TEXT_COLOR_BLUE + SET_BG_COLOR_BLACK);
            System.out.println(EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_BLUE + "Signed in as " + SET_TEXT_COLOR_RED + username + SET_TEXT_COLOR_BLUE + "! (type " + SET_TEXT_COLOR_RED + "\"help\"" + SET_TEXT_COLOR_BLUE + " or " + SET_TEXT_COLOR_RED + "\"h\"" + SET_TEXT_COLOR_BLUE + " for more commands)\n" + SET_TEXT_COLOR_BLUE);
            line = scanner.nextLine();
            while (true) {
                if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h")) {
                    System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "\"help\" " + SET_TEXT_COLOR_BLUE + "or " + SET_TEXT_COLOR_RED + "\"h\"");
                    System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"create\" " + SET_TEXT_COLOR_BLUE + "or " + SET_TEXT_COLOR_RED + "\"c\" " + SET_TEXT_COLOR_BLUE + "(create a new game)");
                    System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"list\" " + SET_TEXT_COLOR_BLUE + "or " + SET_TEXT_COLOR_RED + "\"l\" " + SET_TEXT_COLOR_BLUE + "(list games)");
                    System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"join\" " + SET_TEXT_COLOR_BLUE + "or " + SET_TEXT_COLOR_RED + "\"j\" " + SET_TEXT_COLOR_BLUE + "(join a game)");
                    System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"spectate\" " + SET_TEXT_COLOR_BLUE + "or " + SET_TEXT_COLOR_RED + "\"s\" " + SET_TEXT_COLOR_BLUE + "(spectate a game)");
                    System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"logout\" " + SET_TEXT_COLOR_BLUE + "or " + SET_TEXT_COLOR_RED + "\"out\"");
                    System.out.println(EMPTY + "                                     " + SET_TEXT_COLOR_RED + "\"quit\" " + SET_TEXT_COLOR_BLUE + "or " + SET_TEXT_COLOR_RED + "\"q\"\n" + SET_TEXT_COLOR_BLUE);
                    line = scanner.nextLine();
                } else if (line.equalsIgnoreCase("create") || line.equalsIgnoreCase("c")) {
                    System.out.println("\n" + EMPTY + "  game name:\n");
                    String gameName = scanner.nextLine();
                    CreateGameRequest createGameRequest = new CreateGameRequest(gameName);
                    try {
                        CreateGameResult createGameResult = serverFacade.createGame(createGameRequest);
                        System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + gameName + SET_TEXT_COLOR_BLUE + " created!\n" + SET_TEXT_COLOR_BLUE);
                        line = scanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "Sorry, This game could not be created!\n" + SET_TEXT_COLOR_BLUE);
                        line = scanner.nextLine();
                    }
                } else if (line.equalsIgnoreCase("list") || line.equalsIgnoreCase("l")) {
                    try {
                        ListGamesResult listGamesResult = serverFacade.listGames();
                        System.out.print("\n" + EMPTY + "  ------------Chess------------      ");
                        if (listGamesResult.games().isEmpty()) {
                            System.out.println("No games have been created!" + SET_TEXT_COLOR_BLUE);
                        } else {
                            System.out.println(SET_TEXT_COLOR_RED + "use game number when joining a game!\n" + SET_TEXT_COLOR_BLUE);
                            if (!listGamesResult.games().isEmpty()) {
                                for (int i = 0; i < listGamesResult.games().size(); i++) {
                                    System.out.print(EMPTY + "                                     " + SET_TEXT_COLOR_RED + (i + 1) + ". " + SET_TEXT_COLOR_BLUE + listGamesResult.games().get(i).gameName());
                                    if (listGamesResult.games().get(i).whiteUsername() != null) {
                                        System.out.print("\n" + EMPTY + "                                        " + "white: " + listGamesResult.games().get(i).whiteUsername() + " ");
                                    }
                                    if (listGamesResult.games().get(i).blackUsername() != null) {
                                        System.out.print("\n" + EMPTY + "                                        " + "black: " + listGamesResult.games().get(i).blackUsername() + " ");
                                    }
                                    System.out.println();
                                    System.out.println();
                                }
                            }
                        }
                        System.out.println();
                        line = scanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "Sorry, the games could not be listed!\n" + SET_TEXT_COLOR_BLUE);
                        line = scanner.nextLine();
                    }
                } else if (line.equalsIgnoreCase("join") || line.equalsIgnoreCase("j")) {
                    try {
                        String playerColor;
                        int gameIndex;
                        System.out.println("\n" + EMPTY + "  game number:\n");
                        if (scanner.hasNextInt()) {
                            gameIndex = scanner.nextInt();
                            scanner.nextLine();
                        } else {
                            throw new Exception();
                        }
                        System.out.println("\n" + EMPTY + "  player color:\n");
                        playerColor = scanner.nextLine();
                        if (playerColor.equalsIgnoreCase("white") || playerColor.equalsIgnoreCase("w")) {
                            JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", gameIndex);
                            serverFacade.joinGame(joinGameRequest);
                            while (true) {
                                ChessBoard board = new ChessBoard();
                                board.resetBoard();
                                System.out.println("\n" + EMPTY + "  ------------Chess------------      ");
                                printBoard(board);
                                line = scanner.nextLine();
                                if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("q")) {
                                    System.out.println("\n" + EMPTY + "  ------------Chess------------      You left the game!\n");
                                    line = scanner.nextLine();
                                    break;
                                }
                            }
                        } else if (playerColor.equalsIgnoreCase("black") || playerColor.equalsIgnoreCase("b")) {
                            JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", gameIndex);
                            serverFacade.joinGame(joinGameRequest);
                            while (true) {
                                ChessBoard board = new ChessBoard();
                                board.resetBoard();
                                System.out.println("\n" + EMPTY + "  ------------Chess------------      ");
                                printBoardFlipped(board);
                                line = scanner.nextLine();
                                if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("q")) {
                                    System.out.println("\n" + EMPTY + "  ------------Chess------------      You left the game!\n");
                                    line = scanner.nextLine();
                                    break;
                                }
                            }
                        } else {
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "Sorry, this game could not be joined!\n" + SET_TEXT_COLOR_BLUE);
                        line = scanner.nextLine();
                    }
                } else if (line.equalsIgnoreCase("spectate") || line.equalsIgnoreCase("s")) {
                    try {
                        int gameIndex;
                        System.out.println("\n" + EMPTY + "  game number:\n");
                        if (scanner.hasNextInt()) {
                            gameIndex = scanner.nextInt();
                            scanner.nextLine();
                        } else {
                            throw new Exception();
                        }
                        while (true) {
                            ChessBoard board = new ChessBoard();
                            board.resetBoard();
                            System.out.println("\n" + EMPTY + "  ------------Chess------------      ");
                            printBoard(board);
                            line = scanner.nextLine();
                            if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("q")) {
                                System.out.println("\n" + EMPTY + "  ------------Chess------------      You left the game!\n");
                                line = scanner.nextLine();
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("\n" + EMPTY + "  ------------Chess------------      " + SET_TEXT_COLOR_RED + "Sorry, this game could not be spectated!\n" + SET_TEXT_COLOR_BLUE);
                        line = scanner.nextLine();
                    }
                } else if (line.equalsIgnoreCase("logout") || line.equalsIgnoreCase("out")) {
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
                            switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE:
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    pieceType = BLACK_ROOK;
                                    break;
                                case ChessGame.TeamColor.BLACK:
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    pieceType = BLACK_ROOK;
                                    break;
                            }
                            break;
                        case ChessPiece.PieceType.KNIGHT:
                            switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE:
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    pieceType = BLACK_KNIGHT;
                                    break;
                                case ChessGame.TeamColor.BLACK:
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    pieceType = BLACK_KNIGHT;
                                    break;
                            }
                            break;
                        case ChessPiece.PieceType.BISHOP:
                            switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE:
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    pieceType = BLACK_BISHOP;
                                    break;
                                case ChessGame.TeamColor.BLACK:
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    pieceType = BLACK_BISHOP;
                                    break;
                            }
                            break;
                        case ChessPiece.PieceType.KING:
                            switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE:
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    pieceType = BLACK_KING;
                                    break;
                                case ChessGame.TeamColor.BLACK:
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    pieceType = BLACK_KING;
                                    break;
                            }
                            break;
                        case ChessPiece.PieceType.QUEEN:
                            switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE:
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    pieceType = BLACK_QUEEN;
                                    break;
                                case ChessGame.TeamColor.BLACK:
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    pieceType = BLACK_QUEEN;
                                    break;
                            }
                            break;
                        case ChessPiece.PieceType.PAWN:
                            switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE:
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    pieceType = BLACK_PAWN;
                                    break;
                                case ChessGame.TeamColor.BLACK:
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    pieceType = BLACK_PAWN;
                                    break;
                            }
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
                    if (j == 7) {
                        System.out.println(SET_BG_COLOR_BLACK);
                    }
                } else {
                    if (j % 2 == 1) {
                        System.out.print(SET_BG_COLOR_DARK_BLUE + pieceColor + pieceType);
                    } else {
                        System.out.print(SET_BG_COLOR_BLUE + pieceColor + pieceType);
                    }
                    if (j == 7) {
                        System.out.println(SET_BG_COLOR_BLACK);
                    }
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
                            switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE:
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    pieceType = BLACK_ROOK;
                                    break;
                                case ChessGame.TeamColor.BLACK:
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    pieceType = BLACK_ROOK;
                                    break;
                            }
                            break;
                        case ChessPiece.PieceType.KNIGHT:
                            switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE:
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    pieceType = BLACK_KNIGHT;
                                    break;
                                case ChessGame.TeamColor.BLACK:
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    pieceType = BLACK_KNIGHT;
                                    break;
                            }
                            break;
                        case ChessPiece.PieceType.BISHOP:
                            switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE:
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    pieceType = BLACK_BISHOP;
                                    break;
                                case ChessGame.TeamColor.BLACK:
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    pieceType = BLACK_BISHOP;
                                    break;
                            }
                            break;
                        case ChessPiece.PieceType.KING:
                            switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE:
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    pieceType = BLACK_KING;
                                    break;
                                case ChessGame.TeamColor.BLACK:
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    pieceType = BLACK_KING;
                                    break;
                            }
                            break;
                        case ChessPiece.PieceType.QUEEN:
                            switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE:
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    pieceType = BLACK_QUEEN;
                                    break;
                                case ChessGame.TeamColor.BLACK:
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    pieceType = BLACK_QUEEN;
                                    break;
                            }
                            break;
                        case ChessPiece.PieceType.PAWN:
                            switch (board.getPiece(position).getTeamColor()) {
                                case ChessGame.TeamColor.WHITE:
                                    pieceColor = SET_TEXT_COLOR_LIGHT_GREY;
                                    pieceType = BLACK_PAWN;
                                    break;
                                case ChessGame.TeamColor.BLACK:
                                    pieceColor = SET_TEXT_COLOR_BLACK;
                                    pieceType = BLACK_PAWN;
                                    break;
                            }
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
                    if (j == 0) {
                        System.out.println(SET_BG_COLOR_BLACK);
                    }
                } else {
                    if (j % 2 == 1) {
                        System.out.print(SET_BG_COLOR_DARK_BLUE + pieceColor + pieceType);
                    } else {
                        System.out.print(SET_BG_COLOR_BLUE + pieceColor + pieceType);
                    }
                    if (j == 0) {
                        System.out.println(SET_BG_COLOR_BLACK);
                    }
                }
            }
            if (i == 7) {
                System.out.println(SET_TEXT_COLOR_BLUE + "     H" + EMPTY + " G" + EMPTY + " F" + EMPTY + " E" + EMPTY + " D" + EMPTY + " C" + EMPTY + " B" + EMPTY + " A");
            }
        }
        System.out.println();
    }
}

package client;

import chess.*;
import request.CreateGameRequest;
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
        String authToken = "";
        String username = "";
        Scanner scanner = new Scanner(System.in);
        String line = "";
        System.out.println(SET_TEXT_COLOR_BLUE + SET_BG_COLOR_BLACK);
        System.out.println(EMPTY + "  --------Chess--------      " + SET_TEXT_COLOR_RED + "(type \"help\" for a list of commands)\n" + SET_TEXT_COLOR_BLUE);
        line = scanner.nextLine();
        while (true) {
            if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h")) {
                System.out.println("\n" + EMPTY + "  --------Chess--------      " + SET_TEXT_COLOR_RED + "\"help\" or \"h\"");
                System.out.println(EMPTY + "                             " + "\"register\" or \"r\"");
                System.out.println(EMPTY + "                             " + "\"login\" or \"l\"");
                System.out.println(EMPTY + "                             " + "\"quit\" or \"q\"\n" + SET_TEXT_COLOR_BLUE);
                line = scanner.nextLine();
            } else if (line.equalsIgnoreCase("register") || line.equalsIgnoreCase("r")) {
                System.out.println("\n" + EMPTY + "username:\n");
                username = scanner.nextLine();
                System.out.println("\n" + EMPTY + "password:\n");
                String password = scanner.nextLine();
                System.out.println("\n" + EMPTY + "email\n");
                String email = scanner.nextLine();
                RegisterRequest registerRequest = new RegisterRequest(username, password, email);
                try {
                    RegisterResult registerResult = serverFacade.registerUser(registerRequest);
                    authToken = registerResult.authToken();
                    break;
                } catch (Exception e) {
                    System.out.println("\n" + EMPTY + "  --------Chess--------      " + SET_TEXT_COLOR_RED + "Sorry, this username has already been taken!\n" + SET_TEXT_COLOR_BLUE);
                    line = scanner.nextLine();
                }
            } else if (line.equalsIgnoreCase("login") || line.equalsIgnoreCase("l")) {
                System.out.println("\n" + EMPTY + "username:\n");
                username = scanner.nextLine();
                System.out.println("\n" + EMPTY + "password:\n");
                String password = scanner.nextLine();
                LoginRequest loginRequest = new LoginRequest(username, password);
                try {
                    LoginResult loginResult = serverFacade.loginUser(loginRequest);
                    authToken = loginResult.authToken();
                    break;
                } catch (Exception e) {
                    System.out.println("\n" + EMPTY + "  --------Chess--------      " + SET_TEXT_COLOR_RED + "Incorrect login, please try again!\n" + SET_TEXT_COLOR_BLUE);
                    line = scanner.nextLine();
                }
            } else if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("q")) {
                return;
            } else {
                System.out.println("\n" + EMPTY + "  --------Chess--------      " + SET_TEXT_COLOR_RED + "Invalid command, please try again!\n" + SET_TEXT_COLOR_BLUE);
                line = scanner.nextLine();
            }
        }
        System.out.println(SET_TEXT_COLOR_BLUE + SET_BG_COLOR_BLACK);
        System.out.println(EMPTY + "Signed in as " + username + "      " + SET_TEXT_COLOR_RED + "(type \"help\" for more commands)\n" + SET_TEXT_COLOR_BLUE);
        line = scanner.nextLine();
        while (true) {
            if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h")) {
                System.out.println("\n" + EMPTY + "  --------Chess--------      " + SET_TEXT_COLOR_RED + "\"help\" or \"h\"");
                System.out.println(EMPTY + "                             " + "\"create\" or \"c\" (create a new game)");
                System.out.println(EMPTY + "                             " + "\"list\" or \"l\" (list games)");
                System.out.println(EMPTY + "                             " + "\"play\" or \"p\" (join a game)");
                System.out.println(EMPTY + "                             " + "\"watch\" or \"w\" (spectate a game)");
                System.out.println(EMPTY + "                             " + "\"logout\" or \"out\"");
                System.out.println(EMPTY + "                             " + "\"quit\" or \"q\"\n" + SET_TEXT_COLOR_BLUE);
                line = scanner.nextLine();
            } else if (line.equalsIgnoreCase("create") || line.equalsIgnoreCase("c")) {
                System.out.println("\n" + EMPTY + "game name:\n");
                String gameName = scanner.nextLine();
                CreateGameRequest createGameRequest = new CreateGameRequest(gameName);
                try {
                    CreateGameResult createGameResult = serverFacade.createGame(createGameRequest);
                    System.out.println("\n" + EMPTY + "  --------Chess--------      " + SET_TEXT_COLOR_RED + gameName + " created!\n" + SET_TEXT_COLOR_BLUE);
                    line = scanner.nextLine();
                } catch (Exception e) {
                    System.out.println("\n" + EMPTY + "  --------Chess--------      " + SET_TEXT_COLOR_RED + "Sorry, This game could not be created!\n" + SET_TEXT_COLOR_BLUE);
                    line = scanner.nextLine();
                }
            } else if (line.equalsIgnoreCase("list") || line.equalsIgnoreCase("l")) {
                try {
                    ListGamesResult listGamesResult = serverFacade.listGames();
                    System.out.print("\n" + EMPTY + "  --------Chess--------      ");
                    if (listGamesResult.games().isEmpty()) {
                        System.out.println(SET_TEXT_COLOR_RED + "No games have been created!" + SET_TEXT_COLOR_BLUE);
                    } else {
                        System.out.println(SET_TEXT_COLOR_RED + "use number when joining a game!\n" + SET_TEXT_COLOR_BLUE);
                        if (!listGamesResult.games().isEmpty()) {
                            for (int i = 0; i < listGamesResult.games().size(); i++) {
                                System.out.println(EMPTY + "                             " + SET_TEXT_COLOR_RED + (i + 1) + ". " + listGamesResult.games().get(i).gameName() + SET_TEXT_COLOR_BLUE);
                            }
                        }
                    }
                    System.out.println();
                    line = scanner.nextLine();
                } catch (Exception e) {
                    System.out.println("\n" + EMPTY + "  --------Chess--------      " + SET_TEXT_COLOR_RED + "Sorry, This game could not be created!\n" + SET_TEXT_COLOR_BLUE);
                    line = scanner.nextLine();
                }
            } else if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("q")) {
                return;
            } else {
                System.out.println("\n" + EMPTY + "  --------Chess--------      " + SET_TEXT_COLOR_RED + "Invalid command, please try again!\n" + SET_TEXT_COLOR_BLUE);
                line = scanner.nextLine();
            }
        }
    }

    public static void printBoard(ChessBoard board) {
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
                    pieceType = EMPTY + " ";
                }
                if (i % 2 == 1) {
                    if (j % 2 == 0) {
                        System.out.print(SET_BG_COLOR_BLUE + pieceColor + pieceType);
                    } else {
                        System.out.print(SET_BG_COLOR_DARK_BLUE + pieceColor + pieceType);
                    }
                    if (j == 7) {
                        System.out.println(SET_BG_COLOR_BLACK);
                    }
                } else {
                    if (j % 2 == 1) {
                        System.out.print(SET_BG_COLOR_BLUE + pieceColor + pieceType);
                    } else {
                        System.out.print(SET_BG_COLOR_DARK_BLUE + pieceColor + pieceType);
                    }
                    if (j == 7) {
                        System.out.println(SET_BG_COLOR_BLACK);
                    }
                }
            }
            if (i == 0) {
                System.out.println(SET_TEXT_COLOR_BLUE + EMPTY + EMPTY + " A" + EMPTY + "B" + EMPTY + "C" + EMPTY + "D" + EMPTY + "E" + EMPTY + "F" + EMPTY + "G" + EMPTY + "H");
            }
        }
        System.out.println();
    }
}

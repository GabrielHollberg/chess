package client;

import chess.*;

import java.util.Scanner;

import ui.EscapeSequences.*;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.BLACK_BISHOP;
import static ui.EscapeSequences.WHITE_KING;
import static ui.EscapeSequences.WHITE_PAWN;

public class ClientMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = "";
        System.out.println(SET_TEXT_COLOR_BLUE + SET_BG_COLOR_BLACK);
        System.out.println(EMPTY + "  --------Chess--------      (type \"help\" for a list of commands)\n");
        line = scanner.nextLine();
        while (true) {
            if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h")) {
                System.out.println("\n" + EMPTY + "\"help\" - \"h\"");
                System.out.println(EMPTY + "\"register\" - \"r\"");
                System.out.println(EMPTY + "\"login\" - \"l\"");
                System.out.println(EMPTY + "\"quit\" - \"q\"\n");
                line = scanner.nextLine();
            } else if (line.equalsIgnoreCase("register") || line.equalsIgnoreCase("r")) {
                System.out.println("\n" + EMPTY + "username:\n");
                String username = scanner.nextLine();
                System.out.println("\n" + EMPTY + "password:\n");
                String password = scanner.nextLine();
                System.out.println("\n" + EMPTY + "email\n");
                String email = scanner.nextLine();
                break;
            } else if (line.equalsIgnoreCase("login") || line.equalsIgnoreCase("l")) {
                System.out.println("\n" + EMPTY + "username:\n");
                String username = scanner.nextLine();
                System.out.println("\n" + EMPTY + "password:\n");
                String password = scanner.nextLine();
                break;
            } else if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("q")) {
                return;
            } else {
                System.out.println("\n" + EMPTY + "invalid command, please try again\n");
                System.out.println(EMPTY + "\"help\" - \"h\"");
                System.out.println(EMPTY + "\"register\" - \"r\"");
                System.out.println(EMPTY + "\"login\" - \"l\"");
                System.out.println(EMPTY + "\"quit\" - \"q\"\n");
                line = scanner.nextLine();
            }
        }
        while (true) {
            System.out.println(SET_TEXT_COLOR_BLUE + SET_BG_COLOR_BLACK);
            System.out.println(EMPTY + "  --------Chess--------\n");
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            printBoard(board);
            line = scanner.nextLine();
            if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h")) {
                System.out.println("Options\n");
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

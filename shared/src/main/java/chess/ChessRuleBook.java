package chess;

import java.util.ArrayList;

public interface ChessRuleBook {
    ArrayList<ChessMove> validMoves(ChessBoard board, ChessPosition position);
    ArrayList<ChessMove> validMoves(ChessBoard board, ChessGame.TeamColor color);
    boolean isBoardValid(ChessBoard board);
    boolean isInCheck(ChessBoard board, ChessGame.TeamColor color);
    boolean isInCheckmate(ChessBoard board, ChessGame.TeamColor color);
    boolean isInStalemate(ChessBoard board, ChessGame.TeamColor color);
}
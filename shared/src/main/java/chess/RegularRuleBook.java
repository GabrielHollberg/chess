package chess;

import java.util.ArrayList;

public class RegularRuleBook implements ChessRuleBook {
    public ArrayList<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        if (board.getPiece(position).getPieceType() == ChessPiece.PieceType.KING) {
            rule = new KingMovementRule();
        } else if (board.getPiece(position).getPieceType() == ChessPiece.PieceType.QUEEN) {
            rule = new QueenMovementRule();
        } else if (board.getPiece(position).getPieceType() == ChessPiece.PieceType.BISHOP) {
            rule = new BishopMovementRule();
        } else if (board.getPiece(position).getPieceType() == ChessPiece.PieceType.KNIGHT) {
            rule = new KnightMovementRule();
        } else if (board.getPiece(position).getPieceType() == ChessPiece.PieceType.ROOK) {
            rule = new RookMovementRule();
        } else if (board.getPiece(position).getPieceType() == ChessPiece.PieceType.PAWN) {
            rule = new PawnMovementRule();
        }
        return rule.validMoves(board, position);
    }
    public ArrayList<ChessMove> validMoves(ChessBoard board, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ChessPosition position = new ChessPosition(i + 1, j + 1);
                    if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        validMoves.addAll(validMoves(board, position));
                    }
                }
            }
        } else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ChessPosition position = new ChessPosition(i + 1, j + 1);
                    if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        validMoves.addAll(validMoves(board, position));
                    }
                }
            }
        }
        return validMoves;
    }
    public boolean isBoardValid(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }
    public boolean isInCheck(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not implemented");
    }
    public boolean isInCheckmate(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not implemented");
    }
    public boolean isInStalemate(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not implemented");
    }

    private MovementRule rule;
    private final ArrayList<ChessMove> validMoves = new ArrayList<>();
}

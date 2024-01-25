package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RegularRuleBook implements ChessRuleBook {
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
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
}

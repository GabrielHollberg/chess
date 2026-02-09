package chess;

import java.util.Collection;
import java.util.Iterator;

public class CheckCalculator {
    // Return true if king of given color is in check on given board
    public boolean isInCheck(ChessBoard board, ChessGame.TeamColor color) {
        ChessPosition kingPosition = null;
        // Loop through all pieces
        outer:
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition position = new ChessPosition(i + 1, j + 1);
                ChessPiece piece = board.getPiece(position);
                // Find non-opponent king's position
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color) {
                    kingPosition = position;
                    break outer;
                }
            }
        }
        // Loop through all pieces
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition position = new ChessPosition(i + 1, j + 1);
                ChessPiece piece = board.getPiece(position);
                // If opponent piece
                if (piece != null && piece.getTeamColor() != color) {
                    Collection<ChessMove> pieceMoves = piece.pieceMoves(board, position);
                    // Loop through piece's moves
                    for (ChessMove move : pieceMoves) {
                        // If move's end position is same as non-opponent king's position return true
                        if (move.getEndPosition().getRow() == kingPosition.getRow() && move.getEndPosition().getColumn() == kingPosition.getColumn()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheckMate(ChessBoard board, ChessGame.TeamColor color) {
        // Return true if king of given color is in checkmate on given board
        // If king is in check continue, otherwise return false
        if (isInCheck(board, color)) {
            // Loop through
            throw new RuntimeException("Not Implemented");
        } else {
            return false;
        }
    }

    public boolean isInStaleMate(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not Implemented");
    }
}

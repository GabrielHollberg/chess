package chess;

import java.util.Collection;
import java.util.Iterator;

public class CheckCalculator {

    public boolean isInCheck(ChessBoard board, ChessGame.TeamColor color) {
        ChessPosition kingPosition = null;
        outer:
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {                                                                   // Loop through all pieces
                ChessPosition position = new ChessPosition(i + 1, j + 1);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color) {   // Find non-opponent king's position
                    kingPosition = position;
                    break outer;
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {                                                                   // Loop through all pieces
                ChessPosition position = new ChessPosition(i + 1, j + 1);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() != color) {                                                        // If opponent piece
                    Collection<ChessMove> pieceMoves = piece.pieceMoves(board, position);
                    for (ChessMove move : pieceMoves) {                                                                  // Loop through piece's moves
                        if (move.getEndPosition().getRow() == kingPosition.getRow() && move.getEndPosition().getColumn() == kingPosition.getColumn()) { // If move's end position threatens non-opponent king
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheckMate(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not Implemented");
    }

    public boolean isInStaleMate(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not Implemented");
    }
}

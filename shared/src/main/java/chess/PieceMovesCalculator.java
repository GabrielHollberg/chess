package chess;

import java.util.ArrayList;
import java.util.Collection;

// Provides general methods to Calculator subclasses to return an ArrayList of ChessMove objects given a ChessBoard and a ChessPosition.

abstract public class PieceMovesCalculator {

    ChessPosition newPosition = null;
    ChessPosition possibleCheckPosition = null;
    int dRow = 0;
    int dColumn = 0;
    final Collection<ChessMove> moves = new ArrayList<>();

    abstract public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    public void moveNewPosition(ChessPosition newPosition) {
        this.newPosition = new ChessPosition(newPosition.getRow() + 1 + dRow, newPosition.getColumn() + 1 + dColumn);
    }

    public boolean checkNewPosition(ChessBoard board, ChessPosition myPosition) {
        if (onBoard(newPosition)) {
            if (board.getPiece(newPosition) != null) {
                if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
                return false;
            } else {
                moves.add(new ChessMove(myPosition, newPosition, null));
                return true;
            }
        } else {
            return false;
        }
    }

    public void checkNewPawnPosition(ChessBoard board, ChessPosition myPosition) {
        if (onBoard(newPosition)) {
            if (myPosition.getColumn() != newPosition.getColumn() && board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                if (newPosition.getRow() != 7 && newPosition.getRow() != 0) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                } else {
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
                }
            } else if (myPosition.getColumn() == newPosition.getColumn() && (dRow == 1 || dRow == -1) && board.getPiece(newPosition) == null) {
                if (newPosition.getRow() != 7 && newPosition.getRow() != 0) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                } else {
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
                }
                moveNewPosition(newPosition);
                if (myPosition.getRow() == 1 && board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE && board.getPiece(newPosition) == null) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                } else if (myPosition.getRow() == 6 && board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK && board.getPiece(newPosition) == null) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
    }

    public boolean onBoard(ChessPosition position) {
        return position.getRow() >= 0 && position.getColumn() >= 0 && position.getRow() <= 7 && position.getColumn() <= 7;
    }
}

package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> result = new ArrayList<>();

        ChessPiece myPiece = board.getPiece(myPosition);
        ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
        int i = 1;
        int j = 1;
        boolean stop = false;

        if (myPiece.type == ChessPiece.PieceType.BISHOP) {
            for (int k = 0; k < 4; k++) {
                while (!stop) {
                    if (newPosition.getRow() + i < 8 && newPosition.getRow() + i >= 0 && newPosition.getColumn() + j < 8 && newPosition.getColumn() + j >= 0) {
                        newPosition.setRow(newPosition.getRow() + i);
                        newPosition.setColumn(newPosition.getColumn() + j);

                        if (board.getPiece(newPosition) == null) {
                            result.add(new ChessMove(myPosition, new ChessPosition(newPosition.getRow() + 1, newPosition.getColumn() + 1), null));
                        } else if (board.getPiece(newPosition).pieceColor != myPiece.pieceColor) {
                            result.add(new ChessMove(myPosition, new ChessPosition(newPosition.getRow() + 1, newPosition.getColumn() + 1), null));
                            stop = true;
                            newPosition.setRow(myPosition.getRow());
                            newPosition.setColumn(myPosition.getColumn());
                        } else {
                            stop = true;
                            newPosition.setRow(myPosition.getRow());
                            newPosition.setColumn(myPosition.getColumn());
                        }
                    } else {
                        stop = true;
                        newPosition.setRow(myPosition.getRow());
                        newPosition.setColumn(myPosition.getColumn());
                    }
                }
                stop = false;
                if (k % 2 == 0) {
                    j *= -1;
                } else {
                    i *= -1;
                }
            }
        }

        return result;
    }

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;
}

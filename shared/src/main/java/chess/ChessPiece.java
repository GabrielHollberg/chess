package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public ChessPiece(ChessGame.TeamColor color, ChessPiece.PieceType type) {
        this.color = color;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "color=" + color +
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
        return color;
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
        if(board.getPiece(myPosition) == null) {
            return null;
        } else if(board.getPiece(myPosition).type == PieceType.ROOK) {
            return rookMoves(board, myPosition);
        } else if(board.getPiece(myPosition).type == PieceType.KNIGHT) {
            return knightMoves(board, myPosition);
        } else if(board.getPiece(myPosition).type == PieceType.BISHOP) {
            return bishopMoves(board, myPosition);
        } else if(board.getPiece(myPosition).type == PieceType.QUEEN) {
            return queenMoves(board, myPosition);
        } else if(board.getPiece(myPosition).type == PieceType.KING) {
            return kingMoves(board, myPosition);
        } else if(board.getPiece(myPosition).type == PieceType.PAWN) {
            return pawnMoves(board, myPosition);
        } else {
            return null;
        }
    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int xIt = 1;
        int yIt = 0;
        int xPrevious = myPosition.getRow();
        int yPrevious = myPosition.getColumn();

        // Iterate through four movement directions
        for(int i = 0; i < 4; i++) {
            do {
                ChessPosition newPosition = new ChessPosition(xPrevious + xIt + 1, yPrevious + yIt + 1);
                if(newPosition.getRow() <= 7 && newPosition.getRow() >= 0 && newPosition.getColumn() <= 7 && newPosition.getColumn() >= 0 && board.getPiece(newPosition) == null) {
                    moves.add(new ChessMove(myPosition, newPosition));
                } else {
                    break;
                }
                xPrevious += xIt;
                yPrevious += yIt;
            } while (xPrevious + xIt >= 0 && xPrevious + xIt <= 7 && yPrevious + yIt >= 0 && yPrevious + yIt <= 7);

            // Update iterators and reset row and column trackers
            switch(i) {
                case 0:
                    xIt = 0;
                    yIt = 1;
                    break;
                case 1:
                    xIt = -1;
                    yIt = 0;
                    break;
                case 2:
                    xIt = 0;
                    yIt = -1;
                    break;
            }
            xPrevious = myPosition.getRow();
            yPrevious = myPosition.getColumn();
        }

        return moves;
    }

    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        return null;
    }

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        return null;
    }

    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        return null;
    }

    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        return null;
    }

    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        return null;
    }

    private final ChessGame.TeamColor color;
    private final PieceType type;
}

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

        for(int i = 0; i < 4; i++) {
            while (myPosition.getColumn() >= 0 && myPosition.getColumn() <= 7 && myPosition.getRow() >= 0 && myPosition.getRow() <= 7) {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);

                if(board.getPiece(newPosition) != null) {
                    break;
                } else {
                    moves.add(new ChessMove(myPosition, newPosition));
                }
            }
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

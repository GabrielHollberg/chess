package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;
    private ChessPosition newPosition;
    private int dRow;
    private int dColumn;
    private Collection<ChessMove> moves;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
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
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if(board.getPiece(myPosition).getPieceType() == PieceType.KING) {
            return kingMoves(board, myPosition);
        } else if(board.getPiece(myPosition).getPieceType() == PieceType.QUEEN) {
            return queenMoves(board, myPosition);
        } else if(board.getPiece(myPosition).getPieceType() == PieceType.BISHOP) {
            return bishopMoves(board, myPosition);
        } else if(board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT) {
            return knightMoves(board, myPosition);
        } else if(board.getPiece(myPosition).getPieceType() == PieceType.ROOK) {
            return rookMoves(board, myPosition);
        } else if(board.getPiece(myPosition).getPieceType() == PieceType.PAWN) {
            return pawnMoves(board, myPosition);
        } else {
            return null;
        }
    }

    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {

    }

    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {

    }

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {

    }

    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {

    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        dColumn = 1;
        dRow = 0;
        newPosition = myPosition;

        incrementPosition(newPosition);
        if (checkNewPosition(board, myPosition)) {

        }

    }

    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {

    }

    public void incrementPosition(ChessPosition newPosition) {
        newPosition.setRow(newPosition.getRow() + dRow);
        newPosition.setColumn(newPosition.getRow() + dColumn);
    }

    public boolean checkNewPosition(ChessBoard board, ChessPosition myPosition) {
        if (board.getPiece(newPosition) != null) {
            if (board.getPiece(newPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                return false;
            }
        }
    }
}

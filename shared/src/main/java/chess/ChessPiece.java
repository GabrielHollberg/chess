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
        } else {
            return null;
        }
    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        xIt = 1;
        yIt = 0;
        xTracker = myPosition.getRow();
        yTracker = myPosition.getColumn();
        this.myPosition = myPosition;

        // Iterate through four movement directions
        for(int i = 0; i < 4; i++) {
            do {
                ChessPosition newPosition = new ChessPosition(xTracker + xIt + 1, yTracker + yIt + 1);
                if(newPosition.getRow() <= 7 && newPosition.getRow() >= 0 && newPosition.getColumn() <= 7 && newPosition.getColumn() >= 0 && (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
                    moves.add(new ChessMove(myPosition, newPosition));
                    if(board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        break;
                    }
                } else {
                    break;
                }
                xTracker += xIt;
                yTracker += yIt;
            } while (xTracker + xIt >= 0 && xTracker + xIt <= 7 && yTracker + yIt >= 0 && yTracker + yIt <= 7);

            // Update iterators and reset row and column trackers
            updateIt(i);
            resetTrackers();
        }
        return moves;
    }

    public void updateIt(int i) {
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
    }

    public void resetTrackers() {
        xTracker = myPosition.getRow();
        yTracker = myPosition.getColumn();
    }

    private int xIt;
    private int yIt;
    private int xTracker;
    private int yTracker;
    private ChessPosition myPosition;
    private final ChessGame.TeamColor color;
    private final PieceType type;
}

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
        } /*else if(board.getPiece(myPosition).type == PieceType.PAWN) {
            return pawnMoves(board, myPosition);
        }*/ else {
            return null;
        }
    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        // Iterate through four movement directions
        for(int i = 0; i < 4; i++) {
            // Reset row and column trackers
            resetTrackers();
            // Update Iterators
            updateIt(i);
            // Iterate through squares
            checkSquares(moves);
        }
        return moves;
    }

    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        // Iterate through four movement directions
        for(int i = 0; i < 8; i++) {
            // Reset row and column trackers
            resetTrackers();
            // Update Iterators
            updateIt(i);
            // Iterate through squares
            checkSquares(moves);
        }
        return moves;
    }

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        // Iterate through four movement directions
        for(int i = 0; i < 4; i++) {
            // Reset row and column trackers
            resetTrackers();
            // Update Iterators
            updateIt(i);
            // Iterate through squares
            checkSquares(moves);
        }
        return moves;
    }

    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        // Iterate through eight movement directions
        for(int i = 0; i < 8; i++) {
            // Reset row and column trackers
            resetTrackers();
            // Update Iterators
            updateIt(i);
            // Iterate through squares
            checkSquares(moves);
        }
        return moves;
    }

    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        // Iterate through eight movement directions
        for(int i = 0; i < 8; i++) {
            // Reset row and column trackers
            resetTrackers();
            // Update Iterators
            updateIt(i);
            // Iterate through squares
            checkSquares(moves);
        }
        return moves;
    }

    /*public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {

            if(myPosition.getRow() == 1) {
                resetTrackers();
                xIt = 0;
                yIt = 1;
                do {
                    xTracker += xIt;
                    yTracker += yIt;
                    if()
                }
            } else {

            }

        }

        // Iterate through eight movement directions
        for(int i = 0; i < 8; i++) {
            // Reset row and column trackers
            resetTrackers();
            // Update Iterators
            updateIt(i);
            // Iterate through squares
            checkSquares(moves);
        }
        return moves;
    }*/

    public void checkSquares(ArrayList<ChessMove> moves) {
        switch(board.getPiece(myPosition).getPieceType()) {
            case ROOK, QUEEN, BISHOP:
                do {
                    ChessPosition newPosition = new ChessPosition(xTracker + xIt + 1, yTracker + yIt + 1);
                    if (isPositionInBounds(newPosition)) {
                        if (board.getPiece(newPosition) == null) {
                            moves.add(new ChessMove(myPosition, newPosition));
                        } else if (isPositionEnemy(newPosition)) {
                            moves.add(new ChessMove(myPosition, newPosition));
                            break;
                        } else {
                            break;
                        }
                    }
                    xTracker += xIt;
                    yTracker += yIt;
                } while (xTracker + xIt >= 0 && xTracker + xIt <= 7 && yTracker + yIt >= 0 && yTracker + yIt <= 7);
                break;
            case KNIGHT, KING:
                ChessPosition newPosition = new ChessPosition(xTracker + xIt + 1, yTracker + yIt + 1);
                if (isPositionInBounds(newPosition)) {
                    if (board.getPiece(newPosition) == null) {
                        moves.add(new ChessMove(myPosition, newPosition));
                    } else if (isPositionEnemy(newPosition)) {
                        moves.add(new ChessMove(myPosition, newPosition));
                        break;
                    } else {
                        break;
                    }
                }
                break;
        }
    }

    public boolean isPositionInBounds(ChessPosition newPosition) {
        return (newPosition.getRow() <= 7 && newPosition.getRow() >= 0 && newPosition.getColumn() <= 7 && newPosition.getColumn() >= 0);
    }

    public boolean isPositionEnemy(ChessPosition newPosition) {
        return (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor());
    }

    public void updateIt(int i) {
        switch(board.getPiece(myPosition).getPieceType()) {
            case ROOK:
                switch (i) {
                    case 0:
                        xIt = 1;
                        yIt = 0;
                        break;
                    case 1:
                        xIt = 0;
                        yIt = 1;
                        break;
                    case 2:
                        xIt = -1;
                        yIt = 0;
                        break;
                    case 3:
                        xIt = 0;
                        yIt = -1;
                        break;
                }
                break;
            case KNIGHT:
                switch (i) {
                    case 0:
                        xIt = 2;
                        yIt = 1;
                        break;
                    case 1:
                        xIt = 2;
                        yIt = -1;
                        break;
                    case 2:
                        xIt = -2;
                        yIt = 1;
                        break;
                    case 3:
                        xIt = -2;
                        yIt = -1;
                        break;
                    case 4:
                        xIt = 1;
                        yIt = 2;
                        break;
                    case 5:
                        xIt = 1;
                        yIt = -2;
                        break;
                    case 6:
                        xIt = -1;
                        yIt = 2;
                        break;
                    case 7:
                        xIt = -1;
                        yIt = -2;
                        break;
                }
                break;
            case BISHOP, QUEEN, KING:
                switch (i) {
                    case 0:
                        xIt = 1;
                        yIt = 1;
                        break;
                    case 1:
                        xIt = -1;
                        yIt = 1;
                        break;
                    case 2:
                        xIt = 1;
                        yIt = -1;
                        break;
                    case 3:
                        xIt = -1;
                        yIt = -1;
                        break;
                    case 4:
                        xIt = 1;
                        yIt = 0;
                        break;
                    case 5:
                        xIt = 0;
                        yIt = 1;
                        break;
                    case 6:
                        xIt = -1;
                        yIt = 0;
                        break;
                    case 7:
                        xIt = 0;
                        yIt = -1;
                        break;
                }
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
    private ChessBoard board;
    private ChessPosition myPosition;
    private final ChessGame.TeamColor color;
    private final PieceType type;
}

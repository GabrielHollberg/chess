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
        }

        this.board = board;
        this.myPosition = myPosition;

        if(board.getPiece(myPosition).type == PieceType.ROOK) {
            return rookMoves();
        } else if(board.getPiece(myPosition).type == PieceType.KNIGHT) {
            return knightMoves();
        } else if(board.getPiece(myPosition).type == PieceType.BISHOP) {
            return bishopMoves();
        } else if(board.getPiece(myPosition).type == PieceType.QUEEN) {
            return queenMoves();
        } else if(board.getPiece(myPosition).type == PieceType.KING) {
            return kingMoves();
        } else {
            return pawnMoves();
        }
    }

    public Collection<ChessMove> rookMoves() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        // Iterate through four movement directions
        for(int i = 4; i < 8; i++) {
            // Reset row and column trackers
            resetTrackers();
            // Update Iterators
            updateIt(i);
            // Iterate through squares
            checkSquares(moves);
        }
        return moves;
    }

    public Collection<ChessMove> knightMoves() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        // Iterate through eight moves
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

    public Collection<ChessMove> bishopMoves() {
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

    public Collection<ChessMove> queenMoves() {
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

    public Collection<ChessMove> kingMoves() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        // Iterate through eight moves
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

    public Collection<ChessMove> pawnMoves() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            // Iterate through three movement directions
            for (int i = 0; i < 3; i++) {
                // Reset row and column trackers
                resetTrackers();
                // Update Iterators
                updateIt(i);
                // Iterate through squares
                checkSquares(moves);
            }
        } else {
            // Iterate through three movement directions
            for (int i = 3; i < 6; i++) {
                // Reset row and column trackers
                resetTrackers();
                // Update Iterators
                updateIt(i);
                // Iterate through squares
                checkSquares(moves);
            }
        }
        return moves;
    }

    public void checkSquares(ArrayList<ChessMove> moves) {
        switch(board.getPiece(myPosition).getPieceType()) {
            case ROOK, QUEEN, BISHOP:
                do {
                    ChessPosition newPosition = new ChessPosition(rowTracker + rowIt + 1, columnTracker + columnIt + 1);
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
                    columnTracker += columnIt;
                    rowTracker += rowIt;
                } while (columnTracker + columnIt >= 0 && columnTracker + columnIt <= 7 && rowTracker + rowIt >= 0 && rowTracker + rowIt <= 7);
                break;
            case KNIGHT, KING: {
                ChessPosition newPosition = new ChessPosition(rowTracker + rowIt + 1, columnTracker + columnIt + 1);
                if (isPositionInBounds(newPosition)) {
                    if (board.getPiece(newPosition) == null) {
                        moves.add(new ChessMove(myPosition, newPosition));
                    } else if (isPositionEnemy(newPosition)) {
                        moves.add(new ChessMove(myPosition, newPosition));
                        break;
                    }
                }}
                break;
            case PAWN: {
                ChessPosition newPosition = new ChessPosition(rowTracker + rowIt + 1, columnTracker + columnIt + 1);
                if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    if (columnIt == 0) {
                        if (myPosition.getRow() == 1) {
                            if (isPositionInBounds(newPosition) && board.getPiece(newPosition) == null) {
                                moves.add(new ChessMove(myPosition, newPosition));
                                ChessPosition newPositionTwo = new ChessPosition(newPosition.getRow() + 2, newPosition.getColumn() + 1);
                                if (isPositionInBounds(newPositionTwo) && board.getPiece(newPositionTwo) == null) {
                                    moves.add(new ChessMove(myPosition, newPositionTwo));
                                }
                            }
                        } else if (isPositionInBounds(newPosition) && board.getPiece(newPosition) == null) {
                            moves.add(new ChessMove(myPosition, newPosition));
                        }
                    } else if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        moves.add(new ChessMove(myPosition, newPosition));
                    }
                } else {
                    if (columnIt == 0) {
                        if (myPosition.getRow() == 6) {
                            if (isPositionInBounds(newPosition) && board.getPiece(newPosition) == null) {
                                moves.add(new ChessMove(myPosition, newPosition));
                                ChessPosition newPositionTwo = new ChessPosition(newPosition.getRow(), newPosition.getColumn() + 1);
                                if (isPositionInBounds(newPositionTwo) && board.getPiece(newPositionTwo) == null) {
                                    moves.add(new ChessMove(myPosition, newPositionTwo));
                                }
                            }
                        } else if (isPositionInBounds(newPosition) && board.getPiece(newPosition) == null) {
                            moves.add(new ChessMove(myPosition, newPosition));
                        }
                    } else if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        moves.add(new ChessMove(myPosition, newPosition));
                    }
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
            case BISHOP, QUEEN, KING, ROOK:
                switch (i) {
                    case 0:
                        columnIt = -1;
                        rowIt = -1;
                        break;
                    case 1:
                        columnIt = -1;
                        rowIt = 1;
                        break;
                    case 2:
                        columnIt = 1;
                        rowIt = -1;
                        break;
                    case 3:
                        columnIt = 1;
                        rowIt = 1;
                        break;
                    case 4:
                        columnIt = 1;
                        rowIt = 0;
                        break;
                    case 5:
                        columnIt = 0;
                        rowIt = 1;
                        break;
                    case 6:
                        columnIt = 0;
                        rowIt = -1;
                        break;
                    case 7:
                        columnIt = -1;
                        rowIt = 0;
                        break;
                }
                break;
            case KNIGHT:
                switch (i) {
                    case 0:
                        columnIt = 2;
                        rowIt = 1;
                        break;
                    case 1:
                        columnIt = 2;
                        rowIt = -1;
                        break;
                    case 2:
                        columnIt = -2;
                        rowIt = 1;
                        break;
                    case 3:
                        columnIt = -2;
                        rowIt = -1;
                        break;
                    case 4:
                        columnIt = 1;
                        rowIt = 2;
                        break;
                    case 5:
                        columnIt = 1;
                        rowIt = -2;
                        break;
                    case 6:
                        columnIt = -1;
                        rowIt = 2;
                        break;
                    case 7:
                        columnIt = -1;
                        rowIt = -2;
                        break;
                }
                break;
            case PAWN:
                switch(i) {
                    case 0:
                        columnIt = -1;
                        rowIt = 1;
                        break;
                    case 1:
                        columnIt = 1;
                        rowIt = 1;
                        break;
                    case 2:
                        columnIt = 0;
                        rowIt = 1;
                        break;
                    case 3:
                        columnIt = -1;
                        rowIt = -1;
                        break;
                    case 4:
                        columnIt = 1;
                        rowIt = -1;
                        break;
                    case 5:
                        columnIt = 0;
                        rowIt = -1;
                        break;
                }
        }
    }

    public void resetTrackers() {
        rowTracker = myPosition.getRow();
        columnTracker = myPosition.getColumn();
    }

    private int columnIt;
    private int rowIt;
    private int columnTracker;
    private int rowTracker;
    private ChessBoard board;
    private ChessPosition myPosition;
    private final ChessGame.TeamColor color;
    private final PieceType type;
}

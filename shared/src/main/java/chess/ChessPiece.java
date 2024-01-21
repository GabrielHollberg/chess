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

    public boolean isInBounds() {
        return newPosition.getRow() < 8 && newPosition.getRow() >= 0 && newPosition.getColumn() < 8 && newPosition.getColumn() >= 0;
    }

    public boolean validMove() {
        return board.getPiece(newPosition) == null || board.getPiece(newPosition).pieceColor != board.getPiece(myPosition).pieceColor;
    }

    public void addMove() {
        validMoves.add(new ChessMove(myPosition, new ChessPosition(newPosition.getRow() + 1, newPosition.getColumn() + 1), null));
    }

    public void addMove(PieceType promotion) {
        validMoves.add(new ChessMove(myPosition, new ChessPosition(newPosition.getRow() + 1, newPosition.getColumn() + 1), promotion));
    }

    public void checkPositions(int incRow, int incCol) {
        newPosition.update(incRow, incCol);
        while (isInBounds()) {
            if (validMove()) {
                addMove();
            }
            if (board.getPiece(newPosition) != null){
                break;
            }
            newPosition.update(incRow, incCol);
        }
        newPosition.resetTo(myPosition);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        this.board = board;
        this.myPosition = myPosition;
        this.newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);

        //Rook
        if (board.getPiece(myPosition).type == PieceType.ROOK)
        {
            int incRow = 1;
            int incCol = 0;
            for (int i = 0; i < 4; i++) {
                checkPositions(incRow, incCol);
                switch (i) {
                    case 0:
                        incRow = 0;
                        incCol = 1;
                        break;
                    case 1:
                        incRow = -1;
                        incCol = 0;
                        break;
                    case 2:
                        incRow = 0;
                        incCol = -1;
                        break;
                }
            }
        }

        //Knight
        else if (board.getPiece(myPosition).type == PieceType.KNIGHT) {
            int incRow = 2;
            int incCol = 1;
            for (int i = 0; i < 8; i++) {
                newPosition.update(incRow, incCol);
                if (isInBounds() && validMove()) {
                    addMove();
                }
                newPosition.resetTo(myPosition);
                int temp = incRow;
                incRow = incCol;
                incCol = temp;
                if(i % 4 == 0) {
                    incRow *= -1;
                }
                else if(i % 4 == 2) {
                    incCol *= -1;
                }
            }
        }

        //Bishop
        else if (board.getPiece(myPosition).type == PieceType.BISHOP) {
            int incRow = 1;
            int incCol = 1;
            for (int k = 0; k < 4; k++) {
                checkPositions(incRow, incCol);
                if (k % 2 == 0) {
                    incRow *= -1;
                } else {
                    incCol *= -1;
                }
            }
        }

        //Queen
        else if (board.getPiece(myPosition).type == PieceType.QUEEN) {
            int incRow = 1;
            int incCol = 0;
            for (int k = 0; k < 8; k++) {
                checkPositions(incRow, incCol);
                switch (k) {
                    case 0:
                        incRow = 0;
                        incCol = 1;
                        break;
                    case 1:
                        incRow = -1;
                        incCol = 0;
                        break;
                    case 2:
                        incRow = 0;
                        incCol = -1;
                        break;
                    case 3:
                        incRow = 1;
                        incCol = 1;
                    default:
                        if (k % 2 == 0) {
                            incRow *= -1;
                        } else {
                            incCol *= -1;
                        }
                }
            }
        }

        //King
        else if (board.getPiece(myPosition).type == PieceType.KING) {
            int incRow = 1;
            int incCol = 0;
            for (int k = 0; k < 8; k++) {
                newPosition.update(incRow, incCol);
                if (isInBounds()) {
                    if(validMove()) {
                        addMove();
                    }
                }
                newPosition.resetTo(myPosition);
                switch (k) {
                    case 0:
                        incRow = 0;
                        incCol = 1;
                        break;
                    case 1:
                        incRow = -1;
                        incCol = 0;
                        break;
                    case 2:
                        incRow = 0;
                        incCol = -1;
                        break;
                    case 3:
                        incRow = 1;
                        incCol = 1;
                    default:
                        if (k % 2 == 0) {
                            incRow *= -1;
                        } else {
                            incCol *= -1;
                        }
                }
            }
        }

        //Pawn
        else if (board.getPiece(myPosition).type == PieceType.PAWN) {
            if(board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE) {
                int incRow = 1;
                int incCol = -1;
                for(int i = 0; i < 3; i++) {
                    newPosition.update(incRow, incCol);
                    if (i % 2 == 0 && board.getPiece(newPosition) != null && isInBounds() && board.getPiece(newPosition).pieceColor != board.getPiece(myPosition).pieceColor) {
                        if (myPosition.getRow() == 6) {
                            addMove(PieceType.ROOK);
                            addMove(PieceType.KNIGHT);
                            addMove(PieceType.BISHOP);
                            addMove(PieceType.QUEEN);
                        } else {
                            addMove();
                        }
                    } else if(i % 2 == 1 && isInBounds() && board.getPiece(newPosition) == null) {
                        if (myPosition.getRow() == 6) {
                            addMove(PieceType.ROOK);
                            addMove(PieceType.KNIGHT);
                            addMove(PieceType.BISHOP);
                            addMove(PieceType.QUEEN);
                        } else {
                            addMove();
                        }
                        if(myPosition.getRow() == 1) {
                            newPosition.update(incRow, incCol);
                            if(isInBounds() && board.getPiece(newPosition) == null) {
                                addMove();
                            }
                        }
                    }
                    newPosition.resetTo(myPosition);
                    incCol++;
                }
            } else {
                int incRow = -1;
                int incCol = -1;
                for(int i = 0; i < 3; i++) {
                    newPosition.update(incRow, incCol);
                    if (i % 2 == 0 && board.getPiece(newPosition) != null && isInBounds() && board.getPiece(newPosition).pieceColor != board.getPiece(myPosition).pieceColor) {
                        if (myPosition.getRow() == 1) {
                            addMove(PieceType.ROOK);
                            addMove(PieceType.KNIGHT);
                            addMove(PieceType.BISHOP);
                            addMove(PieceType.QUEEN);
                        } else {
                            addMove();
                        }
                    } else if(i % 2 == 1 && isInBounds() && board.getPiece(newPosition) == null) {
                        if (myPosition.getRow() == 1) {
                            addMove(PieceType.ROOK);
                            addMove(PieceType.KNIGHT);
                            addMove(PieceType.BISHOP);
                            addMove(PieceType.QUEEN);
                        } else {
                            addMove();
                        }
                        if(myPosition.getRow() == 6) {
                            newPosition.update(incRow, incCol);
                            if(isInBounds() && board.getPiece(newPosition) == null) {
                                addMove();
                            }
                        }
                    }
                    newPosition.resetTo(myPosition);
                    incCol++;
                }
            }
        }
        return validMoves;
    }

    private ChessBoard board;
    private ChessPosition myPosition;
    private ChessPosition newPosition;
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;
    private Collection<ChessMove> validMoves = new ArrayList<>();
}

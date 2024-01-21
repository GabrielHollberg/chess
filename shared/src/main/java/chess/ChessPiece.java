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
        ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
        //Rook
        if (board.getPiece(myPosition).type == PieceType.ROOK) {
            int incRow = 1;
            int incCol = 0;
            for (int k = 0; k < 4; k++) {
                while (newPosition.getRow() + incRow < 8 && newPosition.getRow() + incRow >= 0 && newPosition.getColumn() + incCol < 8 && newPosition.getColumn() + incCol >= 0) {
                    newPosition.setRow(newPosition.getRow() + incRow);
                    newPosition.setColumn(newPosition.getColumn() + incCol);
                    if (board.getPiece(newPosition) == null || board.getPiece(newPosition).pieceColor != board.getPiece(myPosition).pieceColor) {
                        result.add(new ChessMove(myPosition, new ChessPosition(newPosition.getRow() + 1, newPosition.getColumn() + 1), null));
                    }
                    if (board.getPiece(newPosition) != null) {
                        break;
                    }
                }
                newPosition.setRow(myPosition.getRow());
                newPosition.setColumn(myPosition.getColumn());
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
                }
            }
        }
        //Knight
        else if (board.getPiece(myPosition).type == PieceType.KNIGHT) {
            int incRow = 2;
            int incCol = 1;
            for (int k = 0; k < 8; k++) {
                if (newPosition.getRow() + incRow < 8 && newPosition.getRow() + incRow >= 0 && newPosition.getColumn() + incCol < 8 && newPosition.getColumn() + incCol >= 0) {
                    newPosition.setRow(newPosition.getRow() + incRow);
                    newPosition.setColumn(newPosition.getColumn() + incCol);
                    if(board.getPiece(newPosition) == null || board.getPiece(newPosition).pieceColor != board.getPiece(myPosition).pieceColor) {
                        result.add(new ChessMove(myPosition, new ChessPosition(newPosition.getRow() + 1, newPosition.getColumn() + 1), null));
                    }
                }
                newPosition.setRow(myPosition.getRow());
                newPosition.setColumn(myPosition.getColumn());
                int temp = incRow;
                incRow = incCol;
                incCol = temp;
                if(k % 4 == 0) {
                    incRow *= -1;
                }
                else if(k % 4 == 2) {
                    incCol *= -1;
                }
            }
        }
        //Bishop
        else if (board.getPiece(myPosition).type == PieceType.BISHOP) {
            int incRow = 1;
            int incCol = 1;
            for (int k = 0; k < 4; k++) {
                while (newPosition.getRow() + incRow < 8 && newPosition.getRow() + incRow >= 0 && newPosition.getColumn() + incCol < 8 && newPosition.getColumn() + incCol >= 0) {
                    newPosition.setRow(newPosition.getRow() + incRow);
                    newPosition.setColumn(newPosition.getColumn() + incCol);
                    if(board.getPiece(newPosition) == null || board.getPiece(newPosition).pieceColor != board.getPiece(myPosition).pieceColor) {
                        result.add(new ChessMove(myPosition, new ChessPosition(newPosition.getRow() + 1, newPosition.getColumn() + 1), null));
                    }
                    if(board.getPiece(newPosition) != null){
                        break;
                    }
                }
                newPosition.setRow(myPosition.getRow());
                newPosition.setColumn(myPosition.getColumn());
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
                while (newPosition.getRow() + incRow < 8 && newPosition.getRow() + incRow >= 0 && newPosition.getColumn() + incCol < 8 && newPosition.getColumn() + incCol >= 0) {
                    newPosition.setRow(newPosition.getRow() + incRow);
                    newPosition.setColumn(newPosition.getColumn() + incCol);
                    if(board.getPiece(newPosition) == null || board.getPiece(newPosition).pieceColor != board.getPiece(myPosition).pieceColor) {
                        result.add(new ChessMove(myPosition, new ChessPosition(newPosition.getRow() + 1, newPosition.getColumn() + 1), null));
                    }
                    if(board.getPiece(newPosition) != null){
                        break;
                    }
                }
                newPosition.setRow(myPosition.getRow());
                newPosition.setColumn(myPosition.getColumn());
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
        /*//King
        else if (board.getPiece(myPosition).type == PieceType.KING) {
            int incRow = 1;
            int incCol = 0;
            for (int k = 0; k < 4; k++) {
                if (newPosition.getRow() + incRow < 8 && newPosition.getRow() + incRow >= 0 && newPosition.getColumn() + incCol < 8 && newPosition.getColumn() + incCol >= 0) {
                    newPosition.setRow(newPosition.getRow() + incRow);
                    newPosition.setColumn(newPosition.getColumn() + incCol);
                    if(board.getPiece(newPosition) == null || board.getPiece(newPosition).pieceColor != board.getPiece(myPosition).pieceColor) {
                        result.add(new ChessMove(myPosition, new ChessPosition(newPosition.getRow() + 1, newPosition.getColumn() + 1), null));
                    }
                }
                newPosition.setRow(myPosition.getRow());
                newPosition.setColumn(myPosition.getColumn());
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
                }
            }
        }*/
        /*//Pawn
        else if (board.getPiece(myPosition).type == PieceType.PAWN) {
            int incRow = 0;
            int incCol = 0;
            for (int k = 0; k < 4; k++) {
                while (newPosition.getRow() + incRow < 8 && newPosition.getRow() + incRow >= 0 && newPosition.getColumn() + incCol < 8 && newPosition.getColumn() + incCol >= 0) {
                    newPosition.setRow(newPosition.getRow() + incRow);
                    newPosition.setColumn(newPosition.getColumn() + incCol);
                    if(board.getPiece(newPosition) == null || board.getPiece(newPosition).pieceColor != board.getPiece(myPosition).pieceColor) {
                        result.add(new ChessMove(myPosition, new ChessPosition(newPosition.getRow() + 1, newPosition.getColumn() + 1), null));
                    }
                    if(board.getPiece(newPosition) != null){
                        break;
                    }
                }
                newPosition.setRow(myPosition.getRow());
                newPosition.setColumn(myPosition.getColumn());
                if (k % 2 == 0) {
                    incRow *= -1;
                } else {
                    incCol *= -1;
                }
            }
        }*/
        return result;
    }

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;
}

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

    public boolean newInBounds() {
        return newPosition.getRow() < 8 && newPosition.getRow() >= 0 && newPosition.getColumn() < 8 && newPosition.getColumn() >= 0;
    }

    public boolean validNewPosition() {
        return board.getPiece(newPosition) == null || newIsEnemy();
    }

    public boolean newIsEnemy() {
        return board.getPiece(newPosition).pieceColor != board.getPiece(myPosition).pieceColor;
    }

    public boolean newIsNull() {
        return board.getPiece(newPosition) == null;
    }

    public void updateNewPosition() {
        newPosition.setRow(newPosition.getRow() + itRow);
        newPosition.setColumn(newPosition.getColumn() + itCol);
    }

    public void addNewPosition() {
        validMoves.add(new ChessMove(myPosition, new ChessPosition(newPosition.getRow() + 1, newPosition.getColumn() + 1), null));
    }

    public void addNewPosition(PieceType promotionPiece) {
        validMoves.add(new ChessMove(myPosition, new ChessPosition(newPosition.getRow() + 1, newPosition.getColumn() + 1), promotionPiece));
    }

    public void resetNewPosition() {
        newPosition.setRow(myPosition.getRow());
        newPosition.setColumn(myPosition.getColumn());
    }

    public void updateIterators(int i) {
        if(board.getPiece(myPosition).getPieceType() == PieceType.ROOK) {
            switch (i) {
                case 0:
                    itRow = 0;
                    itCol = 1;
                    break;
                case 1:
                    itRow = -1;
                    itCol = 0;
                    break;
                case 2:
                    itRow = 0;
                    itCol = -1;
                    break;
            }
        } else if(board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT) {
            if(i % 2 == 0) {
                itRow *= -1;
            } else if(i % 2 == 1) {
                int temp = itRow;
                itRow = itCol;
                itCol = temp;
            }
        } else if(board.getPiece(myPosition).getPieceType() == PieceType.BISHOP) {
            switch (i) {
                case 0:
                    itRow = -1;
                    itCol = 1;
                    break;
                case 1:
                    itRow = 1;
                    itCol = -1;
                    break;
                case 2:
                    itRow = -1;
                    itCol = -1;
                    break;
            }
        } else if(board.getPiece(myPosition).getPieceType() == PieceType.QUEEN || board.getPiece(myPosition).getPieceType() == PieceType.KING) {
            switch (i) {
                case 0:
                    itRow = -1;
                    itCol = 1;
                    break;
                case 1:
                    itRow = 1;
                    itCol = -1;
                    break;
                case 2:
                    itRow = -1;
                    itCol = -1;
                    break;
                case 3:
                    itRow = 0;
                    itCol = 1;
                    break;
                case 4:
                    itRow = -1;
                    itCol = 0;
                    break;
                case 5:
                    itRow = 0;
                    itCol = -1;
                    break;
                case 6:
                    itRow = 1;
                    itCol = 0;
            }
        } else if(board.getPiece(myPosition).getPieceType() == PieceType.PAWN) {
            itCol++;
        }
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
        newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);

        //Rook
        if(board.getPiece(myPosition).getPieceType() == PieceType.ROOK) {
            itRow = 1;
            itCol = 0;

            for(int i = 0; i < 4; i++) {
                updateNewPosition();
                while (newInBounds()) {
                    if (validNewPosition()) {
                        addNewPosition();
                    }
                    if (!newIsNull()) {
                        break;
                    }
                    updateNewPosition();
                }
                resetNewPosition();
                updateIterators(i);
            }
        }

        //Knight
        else if (board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT) {
            itRow = 2;
            itCol = 1;

            for(int i = 0; i < 8; i++) {
                updateNewPosition();
                if (newInBounds()) {
                    if (validNewPosition()) {
                        addNewPosition();
                    }
                }
                resetNewPosition();
                updateIterators(i);
            }
        }

        //Bishop
        else if(board.getPiece(myPosition).getPieceType() == PieceType.BISHOP) {
            itRow = 1;
            itCol = 1;

            for(int i = 0; i < 4; i++) {
                updateNewPosition();
                while (newInBounds()) {
                    if (validNewPosition()) {
                        addNewPosition();
                    }
                    if (!newIsNull()) {
                        break;
                    }
                    updateNewPosition();
                }
                resetNewPosition();
                updateIterators(i);
            }
        }

        //Queen
        else if (board.getPiece(myPosition).getPieceType() == PieceType.QUEEN) {
            itRow = 1;
            itCol = 1;

            for(int i = 0; i < 8; i++) {
                updateNewPosition();
                while (newInBounds()) {
                    if (validNewPosition()) {
                        addNewPosition();
                    }
                    if (!newIsNull()) {
                        break;
                    }
                    updateNewPosition();
                }
                resetNewPosition();
                updateIterators(i);
            }
        }

        //King
        else if(board.getPiece(myPosition).getPieceType() == PieceType.KING) {
            itRow = 1;
            itCol = 1;

            for(int i = 0; i < 8; i++) {
                updateNewPosition();
                if (newInBounds()) {
                    if (validNewPosition()) {
                        addNewPosition();
                    }
                }
                resetNewPosition();
                updateIterators(i);
            }
        }

        //Pawn
        else if(board.getPiece(myPosition).getPieceType() == PieceType.PAWN) {
            if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
                itCol = -1;
                itRow = 1;

                for(int i = 0; i < 3; i++) {
                    updateNewPosition();
                    if (newInBounds()) {
                        if(i % 2 == 1) {
                            if (newIsNull()) {
                                if (myPosition.getRow() == 6) {
                                    addNewPosition(PieceType.ROOK);
                                    addNewPosition(PieceType.KNIGHT);
                                    addNewPosition(PieceType.BISHOP);
                                    addNewPosition(PieceType.QUEEN);
                                } else {
                                    addNewPosition();
                                    if(myPosition.getRow() == 1) {
                                        updateNewPosition();
                                        if (newIsNull()) {
                                            addNewPosition();
                                        }
                                    }
                                }
                            }
                        } else {
                            if (!newIsNull()) {
                                if (newIsEnemy()) {
                                    if (myPosition.getRow() == 6) {
                                        addNewPosition(PieceType.ROOK);
                                        addNewPosition(PieceType.KNIGHT);
                                        addNewPosition(PieceType.BISHOP);
                                        addNewPosition(PieceType.QUEEN);
                                    } else {
                                        addNewPosition();
                                    }
                                }
                            }
                        }
                    }
                    resetNewPosition();
                    updateIterators(i);
                }
            } else if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
                itCol = -1;
                itRow = -1;

                for(int i = 0; i < 3; i++) {
                    updateNewPosition();
                    if (newInBounds()) {
                        if(i % 2 == 1) {
                            if (newIsNull()) {
                                if (myPosition.getRow() == 1) {
                                    addNewPosition(PieceType.ROOK);
                                    addNewPosition(PieceType.KNIGHT);
                                    addNewPosition(PieceType.BISHOP);
                                    addNewPosition(PieceType.QUEEN);
                                } else {
                                    addNewPosition();
                                    if(myPosition.getRow() == 6) {
                                        updateNewPosition();
                                        if (newIsNull()) {
                                            addNewPosition();
                                        }
                                    }
                                }
                            }
                        } else {
                            if (!newIsNull()) {
                                if (newIsEnemy()) {
                                    if (myPosition.getRow() == 1) {
                                        addNewPosition(PieceType.ROOK);
                                        addNewPosition(PieceType.KNIGHT);
                                        addNewPosition(PieceType.BISHOP);
                                        addNewPosition(PieceType.QUEEN);
                                    } else {
                                        addNewPosition();
                                    }
                                }
                            }
                        }
                    }
                    resetNewPosition();
                    updateIterators(i);
                }
            }
        }

        return validMoves;
    }

    private int itRow;
    private int itCol;
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;
    private ChessBoard board;
    private ChessPosition myPosition;
    private ChessPosition newPosition;
    private final Collection<ChessMove> validMoves = new ArrayList<>();
}

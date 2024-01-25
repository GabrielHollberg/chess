package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovementRule extends MovementRule {
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        newPosition = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);

        if(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            itCol = -1;
            itRow = 1;

            for(int i = 0; i < 3; i++) {
                updateNewPosition();
                if (newInBounds()) {
                    if(i % 2 == 1) {
                        if (newIsNull()) {
                            if (position.getRow() == 6) {
                                addNewPosition(ChessPiece.PieceType.ROOK);
                                addNewPosition(ChessPiece.PieceType.KNIGHT);
                                addNewPosition(ChessPiece.PieceType.BISHOP);
                                addNewPosition(ChessPiece.PieceType.QUEEN);
                            } else {
                                addNewPosition();
                                if(position.getRow() == 1) {
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
                                if (position.getRow() == 6) {
                                    addNewPosition(ChessPiece.PieceType.ROOK);
                                    addNewPosition(ChessPiece.PieceType.KNIGHT);
                                    addNewPosition(ChessPiece.PieceType.BISHOP);
                                    addNewPosition(ChessPiece.PieceType.QUEEN);
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
        } else if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.BLACK) {
            itCol = -1;
            itRow = -1;

            for(int i = 0; i < 3; i++) {
                updateNewPosition();
                if (newInBounds()) {
                    if(i % 2 == 1) {
                        if (newIsNull()) {
                            if (position.getRow() == 1) {
                                addNewPosition(ChessPiece.PieceType.ROOK);
                                addNewPosition(ChessPiece.PieceType.KNIGHT);
                                addNewPosition(ChessPiece.PieceType.BISHOP);
                                addNewPosition(ChessPiece.PieceType.QUEEN);
                            } else {
                                addNewPosition();
                                if(position.getRow() == 6) {
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
                                if (position.getRow() == 1) {
                                    addNewPosition(ChessPiece.PieceType.ROOK);
                                    addNewPosition(ChessPiece.PieceType.KNIGHT);
                                    addNewPosition(ChessPiece.PieceType.BISHOP);
                                    addNewPosition(ChessPiece.PieceType.QUEEN);
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
        return validMoves;
    }
}

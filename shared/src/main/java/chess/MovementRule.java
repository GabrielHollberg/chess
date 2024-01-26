package chess;

import java.util.ArrayList;

public abstract class MovementRule {
    abstract ArrayList<ChessMove> validMoves(ChessBoard board, ChessPosition position);

    public boolean newInBounds() {
        return newPosition.getRow() < 8 && newPosition.getRow() >= 0 && newPosition.getColumn() < 8 && newPosition.getColumn() >= 0;
    }

    public boolean validNewPosition() {
        return board.getPiece(newPosition) == null || newIsEnemy();
    }

    public boolean newIsEnemy() {
        return board.getPiece(newPosition).getTeamColor() != board.getPiece(position).getTeamColor();
    }

    public boolean newIsNull() {
        return board.getPiece(newPosition) == null;
    }

    public void updateNewPosition() {
        newPosition.setRow(newPosition.getRow() + itRow);
        newPosition.setColumn(newPosition.getColumn() + itCol);
    }

    public void addNewPosition() {
        validMoves.add(new ChessMove(position, new ChessPosition(newPosition.getRow() + 1, newPosition.getColumn() + 1), null));
    }

    public void addNewPosition(ChessPiece.PieceType promotionPiece) {
        validMoves.add(new ChessMove(position, new ChessPosition(newPosition.getRow() + 1, newPosition.getColumn() + 1), promotionPiece));
    }

    public void resetNewPosition() {
        newPosition.setRow(position.getRow());
        newPosition.setColumn(position.getColumn());
    }

    public void updateIterators(int i) {
        if(board.getPiece(position).getPieceType() == ChessPiece.PieceType.ROOK) {
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
        } else if(board.getPiece(position).getPieceType() == ChessPiece.PieceType.KNIGHT) {
            if(i % 2 == 0) {
                itRow *= -1;
            } else if(i % 2 == 1) {
                int temp = itRow;
                itRow = itCol;
                itCol = temp;
            }
        } else if(board.getPiece(position).getPieceType() == ChessPiece.PieceType.BISHOP) {
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
        } else if(board.getPiece(position).getPieceType() == ChessPiece.PieceType.QUEEN || board.getPiece(position).getPieceType() == ChessPiece.PieceType.KING) {
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
        } else if(board.getPiece(position).getPieceType() == ChessPiece.PieceType.PAWN) {
            itCol++;
        }
    }

    int itRow = 0;
    int itCol = 0;
    ChessBoard board;
    ChessPosition position;
    ChessPosition newPosition;
    final ArrayList<ChessMove> validMoves = new ArrayList<>();
}
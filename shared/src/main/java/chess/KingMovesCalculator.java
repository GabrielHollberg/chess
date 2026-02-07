package chess;

import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        for (int i = 0; i < 8; i++) {
            newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            changeMoveDirection(i);
            moveNewPosition(newPosition);
            checkNewPosition(board, myPosition);
        }
        return moves;
    }

    public boolean isInCheck(ChessBoard board, ChessPosition kingPosition) {
        for (int i = 0; i < 4; i++) {
            possibleCheckPosition = new ChessPosition(kingPosition.getRow() + 1, kingPosition.getColumn() + 1);
            changeMoveDirection(i);
            moveNewPosition(possibleCheckPosition);
            if (checkPossibleCheck(board, kingPosition, i)) {
                return true;
            }
        }
        for (int i = 4; i < 8; i++) {
            possibleCheckPosition = new ChessPosition(kingPosition.getRow() + 1, kingPosition.getColumn() + 1);
            changeMoveDirection(i);
            moveNewPosition(possibleCheckPosition);
            if (checkPossibleCheck(board, kingPosition, i)) {
                return true;
            }
        }
        for (int i = 8; i < 16; i++) {
            possibleCheckPosition = new ChessPosition(kingPosition.getRow() + 1, kingPosition.getColumn() + 1);
            changeMoveDirection(i);
            moveNewPosition(possibleCheckPosition);
            if (checkPossibleCheck(board, kingPosition, i)) {
                return true;
            }
        }
        return false;
    }

    public void changeMoveDirection(int i) {
        switch (i) {
            case 0:
                dColumn = 1;
                dRow = 0;
                break;
            case 1:
                dColumn = -1;
                dRow = 0;
                break;
            case 2:
                dColumn = 0;
                dRow = 1;
                break;
            case 3:
                dColumn = 0;
                dRow = -1;
                break;
            case 4:
                dColumn = 1;
                dRow = 1;
                break;
            case 5:
                dColumn = 1;
                dRow = -1;
                break;
            case 6:
                dColumn = -1;
                dRow = 1;
                break;
            case 7:
                dColumn = -1;
                dRow = -1;
                break;
            case 8:
                dColumn = 1;
                dRow = 2;
                break;
            case 9:
                dColumn = 2;
                dRow = 1;
                break;
            case 10:
                dColumn = 2;
                dRow = -1;
                break;
            case 11:
                dColumn = 1;
                dRow = -2;
                break;
            case 12:
                dColumn = -1;
                dRow = -2;
                break;
            case 13:
                dColumn = -2;
                dRow = -1;
                break;
            case 14:
                dColumn = -2;
                dRow = 1;
                break;
            case 15:
                dColumn = -1;
                dRow = 2;
                break;
        }
    }
}

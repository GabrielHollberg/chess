package chess;

import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        for(int i = 0; i < 8; i++) {
            newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            changeMoveDirection(i);
            moveNewPosition(newPosition);
            checkNewPosition(board, myPosition);
        }
        return moves;
    }

    public void changeMoveDirection(int i) {
        switch (i) {
            case 0:
                dColumn = 1;
                dRow = 2;
                break;
            case 1:
                dColumn = 2;
                dRow = 1;
                break;
            case 2:
                dColumn = 2;
                dRow = -1;
                break;
            case 3:
                dColumn = 1;
                dRow = -2;
                break;
            case 4:
                dColumn = -1;
                dRow = -2;
                break;
            case 5:
                dColumn = -2;
                dRow = -1;
                break;
            case 6:
                dColumn = -2;
                dRow = 1;
                break;
            case 7:
                dColumn = -1;
                dRow = 2;
                break;
        }
    }
}

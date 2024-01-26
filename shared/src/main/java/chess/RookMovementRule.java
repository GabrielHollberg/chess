package chess;

import java.util.ArrayList;

public class RookMovementRule extends MovementRule {
    public ArrayList<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        newPosition = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
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
        return validMoves;
    }
}

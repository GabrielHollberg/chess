package chess;

import java.util.ArrayList;

public class KnightMovementRule extends MovementRule {
    public ArrayList<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        newPosition = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
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
        return validMoves;
    }
}

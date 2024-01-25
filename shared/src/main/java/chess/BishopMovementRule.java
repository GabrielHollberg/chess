package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovementRule extends MovementRule {
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        newPosition = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
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
        return validMoves;
    }
}

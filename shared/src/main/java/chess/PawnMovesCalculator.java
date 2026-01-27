package chess;

import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        for (int i = 0; i < 3; i++) {
            newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            changeMoveDirection(board, myPosition, i);
            moveNewPosition(newPosition);
            checkNewPawnPosition(board, myPosition);
        }
        return moves;
    }

    public void changeMoveDirection(ChessBoard board, ChessPosition myPosition, int i) {
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            switch (i) {
                case 0:
                    dColumn = -1;
                    dRow = 1;
                    break;
                case 1:
                    dColumn = 0;
                    dRow = 1;
                    break;
                case 2:
                    dColumn = 1;
                    dRow = 1;
                    break;
            }
        } else {
            switch (i) {
                case 0:
                    dColumn = -1;
                    dRow = -1;
                    break;
                case 1:
                    dColumn = 0;
                    dRow = -1;
                    break;
                case 2:
                    dColumn = 1;
                    dRow = -1;
                    break;
            }
        }
    }
}
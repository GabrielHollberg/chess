package chess;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class myTests {

    private static final String TRAPPED_PIECE_MOVES = "ChessGame validMoves returned valid moves for a trapped piece";

    @Test
    @DisplayName("Check")
    public void check() {

        var game = new ChessGame();
        ChessBoard board = new ChessBoard();
        board.addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        board.addPiece(new ChessPosition(2, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        game.setBoard(board);
        game.setTeamTurn(ChessGame.TeamColor.BLACK);
        if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
            System.out.println("Check");
        } else {
            System.out.println("No Check");
        }
    }
}

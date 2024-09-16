package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] board = new ChessPiece[8][8];

    public ChessBoard() {
        
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        // Iterate through entire board
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {

                // Determine the row
                switch (i) {

                    // Set up white pieces
                    case 0:
                        switch (j) {
                            case 0:
                                board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
                                break;
                            case 1:
                                board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
                                break;
                            case 2:
                                board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
                                break;
                            case 3:
                                board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
                                break;
                            case 4:
                                board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
                                break;
                            case 5:
                                board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
                                break;
                            case 6:
                                board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
                                break;
                            case 7:
                                board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
                                break;
                        }
                        break;

                    // Set up white pawns
                    case 1:
                        board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
                        break;

                    // Set up black pawns
                    case 6:
                        board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
                        break;

                    // Set up black pieces
                    case 7:
                        switch (j) {
                            case 0:
                                board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
                                break;
                            case 1:
                                board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
                                break;
                            case 2:
                                board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
                                break;
                            case 3:
                                board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
                                break;
                            case 4:
                                board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
                                break;
                            case 5:
                                board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
                                break;
                            case 6:
                                board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
                                break;
                            case 7:
                                board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
                                break;
                        }
                        break;
                }
            }
        }
    }
}

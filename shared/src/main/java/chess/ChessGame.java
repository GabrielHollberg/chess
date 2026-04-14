package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    TeamColor teamTurn;
    List<ChessBoard> boardHistory = new ArrayList<>(); // Keeps track of every state of the board so far
    private boolean gameOver = false;

    public ChessGame() {
        setTeamTurn(TeamColor.WHITE);
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        boardHistory.add(board);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(boardHistory, chessGame.boardHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, boardHistory);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "teamTurn=" + teamTurn +
                ", boardHistory=" + boardHistory +
                '}';
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessBoard board = boardHistory.getLast();
        ChessPiece piece = board.getPiece(startPosition);
        TeamColor color = piece.getTeamColor();
        // If there is a piece at given position continue, otherwise return null
        if (piece != null) {
            // get piece moves for piece at given position
            Collection<ChessMove> pieceMoves = piece.pieceMoves(board, startPosition);
            // Loop through moves
            Iterator<ChessMove> it = pieceMoves.iterator();
            while (it.hasNext()) {
                ChessMove move = it.next();
                // Move the piece
                boardHistory.add(new ChessBoard(this.boardHistory.getLast()));
                if (move.getPromotionPiece() == null) {
                    boardHistory.getLast().addPiece(move.getEndPosition(), boardHistory.getLast().getPiece(move.getStartPosition()));
                } else {
                    boardHistory.getLast().addPiece(move.getEndPosition(),
                            new ChessPiece(boardHistory.getLast().getPiece(move.getStartPosition()).getTeamColor(),
                                    move.getPromotionPiece()));
                }
                boardHistory.getLast().removePiece(move.getStartPosition());
                // Remove move if it results in checking own king
                if (isInCheck(color)) {
                    it.remove();
                }
                undoMove();
            }
            return pieceMoves;
        } else {
            return null;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (boardHistory.getLast().getPiece(move.getStartPosition()) != null
                && validMoves(move.getStartPosition()).contains(move)
                && boardHistory.getLast().getPiece(move.getStartPosition()).getTeamColor() == getTeamTurn()) {
            boardHistory.add(new ChessBoard(this.boardHistory.getLast()));
            if (move.getPromotionPiece() == null) {
                boardHistory.getLast().addPiece(move.getEndPosition(), boardHistory.getLast().getPiece(move.getStartPosition()));
            } else {
                boardHistory.getLast().addPiece(move.getEndPosition(),
                        new ChessPiece(boardHistory.getLast().getPiece(move.getStartPosition()).getTeamColor(),
                                move.getPromotionPiece()));
            }
            boardHistory.getLast().removePiece(move.getStartPosition());
            if (boardHistory.getLast().getPiece(move.getEndPosition()).getTeamColor() == TeamColor.WHITE) {
                setTeamTurn(TeamColor.BLACK);
            } else {
                setTeamTurn(TeamColor.WHITE);
            }
        } else {
            throw new InvalidMoveException("Not a Valid Move");
        }
    }

    public void undoMove() {
        boardHistory.removeLast();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = getKingPosition(teamColor);
        // Loop through all pieces
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isInCheck(teamColor, kingPosition, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInCheck(ChessGame.TeamColor teamColor, ChessPosition kingPosition, int i, int j) {
        ChessPosition position = new ChessPosition(i + 1, j + 1);
        ChessPiece piece = boardHistory.getLast().getPiece(position);
        // If opponent piece
        if (piece != null && piece.getTeamColor() != teamColor) {
            Collection<ChessMove> pieceMoves = piece.pieceMoves(boardHistory.getLast(), position);
            // Loop through piece's moves
            for (ChessMove move : pieceMoves) {
                // If move's end position is same as non-opponent king's position return true
                if (move.getEndPosition().getRow() == kingPosition.getRow()
                        && move.getEndPosition().getColumn() == kingPosition.getColumn()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // Return true if king of given color is in checkmate on given board
        // If king is in check continue, otherwise return false
        if (isInCheck(teamColor)) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (isInCheckmate(teamColor, i, j)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheckmate(ChessGame.TeamColor teamColor, int i, int j) {
        ChessPosition position = new ChessPosition(i + 1, j + 1);
        ChessBoard board = boardHistory.getLast();
        ChessPiece piece = board.getPiece(position);
        if (piece != null && piece.getTeamColor() == teamColor) {
            Collection<ChessMove> moves = validMoves(position);
            return moves.isEmpty();
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (isInStalemate(teamColor, i, j)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInStalemate(ChessGame.TeamColor teamColor, int i, int j) {
        ChessPosition position = new ChessPosition(i + 1, j + 1);
        ChessBoard board = boardHistory.getLast();
        ChessPiece piece = board.getPiece(position);
        if (piece != null && piece.getTeamColor() == teamColor) {
            Collection<ChessMove> moves = validMoves(position);
            return moves.isEmpty();
        }
        return true;
    }

    public ChessPosition getKingPosition(ChessGame.TeamColor color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition position = new ChessPosition(i + 1, j + 1);
                ChessPiece piece = boardHistory.getLast().getPiece(position);
                // Find non-opponent king's position
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color) {
                    return position;
                }
            }
        }
        return null;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.boardHistory.set(boardHistory.size() - 1, board);
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return boardHistory.getLast();
    }
}
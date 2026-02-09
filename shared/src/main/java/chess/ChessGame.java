package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    TeamColor teamTurn;
    List<ChessBoard> boardHistory = new ArrayList<>(); // Keeps track of every state of the board so far

    public ChessGame() {
        setTeamTurn(TeamColor.WHITE);
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        boardHistory.add(board);
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
        // Derive necessary variables from methods
        ChessBoard board = new ChessBoard(boardHistory.getLast());
        ChessPiece piece = board.getPiece(startPosition);
        // If there is a piece at given position continue, otherwise return null
        if (board.getPiece(startPosition) != null) {
            // get piece moves for piece at given position
            Collection<ChessMove> pieceMoves = piece.pieceMoves(board, startPosition);
            // Loop through moves
            Iterator<ChessMove> it = pieceMoves.iterator();
            while (it.hasNext()) {
                ChessMove move = it.next();
                if (resultCheck(board, move)) {
                    it.remove();
                }
                board = new ChessBoard(boardHistory.getLast());
            }
            return pieceMoves;
        } else {
            return null;
        }
    }

    public boolean resultCheck(ChessBoard board, ChessMove move) {
        // Derive variables
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        TeamColor team = board.getPiece(startPosition).getTeamColor();
        // Move piece
        board.addPiece(endPosition, board.getPiece(startPosition));
        board.removePiece(startPosition);
        // if move results in check return true, otherwise false
        CheckCalculator calculator = new CheckCalculator();
        return calculator.isInCheck(board, team);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (boardHistory.getLast().getPiece(move.getStartPosition()) != null && validMoves(move.getStartPosition()).contains(move) && boardHistory.getLast().getPiece(move.getStartPosition()).getTeamColor() == getTeamTurn()) {
            boardHistory.add(new ChessBoard(this.boardHistory.getLast()));
            if (move.getPromotionPiece() == null) {
                boardHistory.getLast().addPiece(move.getEndPosition(), boardHistory.getLast().getPiece(move.getStartPosition()));
            } else {
                boardHistory.getLast().addPiece(move.getEndPosition(), new ChessPiece(boardHistory.getLast().getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece()));
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
        CheckCalculator calculator = new CheckCalculator();
        return calculator.isInCheck(boardHistory.getLast(), teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        CheckCalculator calculator = new CheckCalculator();
        return calculator.isInCheckMate(boardHistory.getLast(), teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        CheckCalculator calculator = new CheckCalculator();
        return calculator.isInStaleMate(boardHistory.getLast(), teamColor);
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
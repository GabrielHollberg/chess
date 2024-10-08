package chess;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
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
        if(board.getPiece(startPosition) != null) {
            Collection<ChessMove> moves = board.getPiece(startPosition).pieceMoves(board, startPosition);
            Iterator<ChessMove> iterator = moves.iterator();
            while (iterator.hasNext()) {
                ChessMove move = iterator.next();
                if(testMoveCheck(move)) {
                    iterator.remove();
                }
            }
            return moves;
        } else {
            return null;
        }
    }

    public boolean testMoveCheck(ChessMove move) {
        ChessBoard oldBoard = new ChessBoard();
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                oldBoard.addPiece(new ChessPosition(i + 1, j + 1), board.getPiece(new ChessPosition(i + 1, j + 1)));
            }
        }
        board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        board.removePiece(move.getStartPosition());
        if(isInCheck(oldBoard.getPiece(move.getStartPosition()).getTeamColor())) {
            board = oldBoard;
            return true;
        } else {
            board = oldBoard;
            return false;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(board.getPiece(move.getStartPosition()) != null) {
            if (validMoves(move.getStartPosition()).contains(move)) {
                if (teamTurn == board.getPiece(move.getStartPosition()).getTeamColor()) {
                    if (board.getPiece(move.getStartPosition()).pieceMoves(board, move.getStartPosition()).contains(move)) {
                        if(move.getPromotionPiece() == null) {
                            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                        } else {
                            board.addPiece(move.getEndPosition(), new ChessPiece(teamTurn, move.getPromotionPiece()));
                        }
                        board.removePiece(move.getStartPosition());
                        if(teamTurn == TeamColor.WHITE) {
                            setTeamTurn(TeamColor.BLACK);
                        } else {
                            setTeamTurn(TeamColor.WHITE);
                        }
                    } else {
                        throw new InvalidMoveException("Not a valid move");
                    }
                } else {
                    throw new InvalidMoveException("Other team's turn");
                }
            } else {
                throw new InvalidMoveException("Not a valid move");
            }
        } else {
            throw new InvalidMoveException("No piece at start position");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                ChessPosition position = new ChessPosition(i + 1, j + 1);
                if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() != teamColor) {
                    moves.addAll(board.getPiece(position).pieceMoves(board, position));
                }
            }
        }
        for (ChessMove move : moves) {
            if (board.getPiece(move.getEndPosition()) != null && board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                return true;
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
        if(isInCheck(teamColor)) {
            Collection<ChessMove> moves = new ArrayList<ChessMove>();
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    ChessPosition position = new ChessPosition(i + 1, j + 1);
                    if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() == teamColor) {
                        moves.addAll(validMoves(position));
                    }
                }
            }
            return moves.isEmpty();
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(!isInCheck(teamColor)) {
            Collection<ChessMove> moves = new ArrayList<ChessMove>();
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    ChessPosition position = new ChessPosition(i + 1, j + 1);
                    if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() == teamColor) {
                        moves.addAll(validMoves(position));
                    }
                }
            }
            return moves.isEmpty();
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    private ChessBoard board;
    private TeamColor teamTurn;
}

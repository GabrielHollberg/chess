package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.ArrayList;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public ChessPiece(ChessGame.TeamColor color, ChessPiece.PieceType type) {
        this.color = color;
        this.type = type;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "color=" + color +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if(board.getPiece(myPosition).getPieceType() == PieceType.ROOK) {
            return rookMoves(board, myPosition);
        } else if(board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT) {
            return knightMoves(board, myPosition);
        } else if(board.getPiece(myPosition).getPieceType() == PieceType.BISHOP) {
            return bishopMoves(board, myPosition);
        } else if(board.getPiece(myPosition).getPieceType() == PieceType.QUEEN) {
            return queenMoves(board, myPosition);
        } else if(board.getPiece(myPosition).getPieceType() == PieceType.KING) {
            return kingMoves(board, myPosition);
        } else if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE && board.getPiece(myPosition).getPieceType() == PieceType.PAWN) {
            return whitePawnMoves(board, myPosition);
        } else if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK && board.getPiece(myPosition).getPieceType() == PieceType.PAWN) {
            return blackPawnMoves(board, myPosition);
        } else {
            return null;
        }
    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        this.board = board;
        this.myPosition = myPosition;
        for(int i = 0; i < 4; i++) {
            updateIterators(i);
            newPosition = new ChessPosition(myPosition.getRow() + yIt + 1, myPosition.getColumn() + xIt + 1);
            while(inBounds(newPosition)) {
                if (isNull(newPosition)) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                } else if (isEnemy(newPosition)) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                } else {
                    break;
                }
                updateNewPosition();
            }
            newPosition = myPosition;
        }
        return moves;
    }

    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        this.board = board;
        this.myPosition = myPosition;
        for(int i = 8; i < 16; i++) {
            updateIterators(i);
            newPosition = new ChessPosition(myPosition.getRow() + yIt + 1, myPosition.getColumn() + xIt + 1);
            if(inBounds(newPosition)) {
                if (isNull(newPosition)) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                } else if (isEnemy(newPosition)) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
                updateNewPosition();
            }
            newPosition = myPosition;
        }
        return moves;
    }

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        this.board = board;
        this.myPosition = myPosition;
        for(int i = 4; i < 8; i++) {
            updateIterators(i);
            newPosition = new ChessPosition(myPosition.getRow() + yIt + 1, myPosition.getColumn() + xIt + 1);
            while(inBounds(newPosition)) {
                if (isNull(newPosition)) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                } else if (isEnemy(newPosition)) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                } else {
                    break;
                }
                updateNewPosition();
            }
            newPosition = myPosition;
        }
        return moves;
    }

    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        this.board = board;
        this.myPosition = myPosition;
        for(int i = 0; i < 8; i++) {
            updateIterators(i);
            newPosition = new ChessPosition(myPosition.getRow() + yIt + 1, myPosition.getColumn() + xIt + 1);
            while(inBounds(newPosition)) {
                if (isNull(newPosition)) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                } else if (isEnemy(newPosition)) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                } else {
                    break;
                }
                updateNewPosition();
            }
            newPosition = myPosition;
        }
        return moves;
    }

    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        this.board = board;
        this.myPosition = myPosition;
        for(int i = 0; i < 8; i++) {
            updateIterators(i);
            newPosition = new ChessPosition(myPosition.getRow() + yIt + 1, myPosition.getColumn() + xIt + 1);
            if(inBounds(newPosition)) {
                if (isNull(newPosition)) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                } else if (isEnemy(newPosition)) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
                updateNewPosition();
            }
            newPosition = myPosition;
        }
        return moves;
    }

    public Collection<ChessMove> whitePawnMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        this.board = board;
        this.myPosition = myPosition;
        xIt = -1;
        yIt = 1;
        for(int i = 16; i < 19; i++) {
            newPosition = new ChessPosition(myPosition.getRow() + yIt + 1, myPosition.getColumn() + xIt + 1);
            if(i % 2 == 0) {
                if(inBounds(newPosition)) {
                    if(!isNull(newPosition)) {
                        if (isEnemy(newPosition)) {
                            if (myPosition.getRow() == 6) {
                                moves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                                moves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                                moves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                                moves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                            } else {
                                moves.add(new ChessMove(myPosition, newPosition, null));
                            }
                        } else {
                            break;
                        }
                    }
                }
            } else {
                if(inBounds(newPosition)) {
                    if(isNull(newPosition)) {
                        if(myPosition.getRow() == 6) {
                            moves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                            moves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                            moves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                        } else {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                            if (myPosition.getRow() == 1) {
                                if (isNull(new ChessPosition(newPosition.getRow() + 2, newPosition.getColumn() + 1))) {
                                    moves.add(new ChessMove(myPosition, new ChessPosition(newPosition.getRow() + 2, newPosition.getColumn() + 1), null));
                                }
                            }
                        }
                    }
                }
            }
            updateNewPosition();
            updateIterators(i);
            newPosition = myPosition;
        }
        return moves;
    }

    public Collection<ChessMove> blackPawnMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        this.board = board;
        this.myPosition = myPosition;
        xIt = -1;
        yIt = -1;
        for(int i = 19; i < 22; i++) {
            newPosition = new ChessPosition(myPosition.getRow() + yIt + 1, myPosition.getColumn() + xIt + 1);
            if(i % 2 == 1) {
                if(inBounds(newPosition)) {
                    if (!isNull(newPosition)) {
                        if (isEnemy(newPosition)) {
                            if (myPosition.getRow() == 1) {
                                moves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                                moves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                                moves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                                moves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                            } else {
                                moves.add(new ChessMove(myPosition, newPosition, null));
                            }
                        }
                    }
                }
            } else {
                if(inBounds(newPosition)) {
                    if(isNull(newPosition)) {
                        if(myPosition.getRow() == 1) {
                            moves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                            moves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                            moves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                        } else {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                            if (myPosition.getRow() == 6) {
                                if (isNull(new ChessPosition(newPosition.getRow(), newPosition.getColumn() + 1))) {
                                    moves.add(new ChessMove(myPosition, new ChessPosition(newPosition.getRow(), newPosition.getColumn() + 1), null));
                                }
                            }
                        }
                    }
                }
            }
            updateNewPosition();
            updateIterators(i);
            newPosition = myPosition;
        }
        return moves;
    }

    public boolean inBounds(ChessPosition newPosition) {
        return newPosition.getRow() <= 7 && newPosition.getRow() >= 0 && newPosition.getColumn() <= 7 && newPosition.getColumn() >= 0;
    }

    public boolean isNull(ChessPosition newPosition) {
        return board.getPiece(newPosition) == null;
    }

    public boolean isEnemy(ChessPosition newPosition) {
        return board.getPiece(myPosition).getTeamColor() != board.getPiece(newPosition).getTeamColor();
    }



    public void updateNewPosition() {
        newPosition = new ChessPosition(newPosition.getRow() + yIt + 1, newPosition.getColumn() + xIt + 1);
    }

    public void updateIterators(int i) {
        switch(i) {
            case 0:
                xIt = 1;
                yIt = 0;
                break;
            case 1:
                xIt = 0;
                yIt = -1;
                break;
            case 2:
                xIt = -1;
                yIt = 0;
                break;
            case 3:
                xIt = 0;
                yIt = 1;
                break;
            case 4:
                xIt = -1;
                yIt = -1;
                break;
            case 5:
                xIt = -1;
                yIt = 1;
                break;
            case 6:
                xIt = 1;
                yIt = -1;
                break;
            case 7:
                xIt = 1;
                yIt = 1;
                break;
            case 8:
                xIt = 2;
                yIt = 1;
                break;
            case 9:
                xIt = -1;
                yIt = 2;
                break;
            case 10:
                xIt = 2;
                yIt = -1;
                break;
            case 11:
                xIt = -1;
                yIt = -2;
                break;
            case 12:
                xIt = -2;
                yIt = -1;
                break;
            case 13:
                xIt = 1;
                yIt = -2;
                break;
            case 14:
                xIt = -2;
                yIt = 1;
                break;
            case 15:
                xIt = 1;
                yIt = 2;
                break;
            case 16:
                xIt = 0;
                yIt = 1;
                break;
            case 17:
                xIt = 1;
                yIt = 1;
                break;
            case 18:
                xIt = -1;
                yIt = 1;
                break;
            case 19:
                xIt = 0;
                yIt = -1;
                break;
            case 20:
                xIt = 1;
                yIt = -1;
                break;
            case 21:
                xIt = -1;
                yIt = -1;
                break;
        }
    }

    private int xIt, yIt;
    private ChessGame.TeamColor color;
    private PieceType type;
    private ChessBoard board;
    private ChessPosition myPosition, newPosition;
}

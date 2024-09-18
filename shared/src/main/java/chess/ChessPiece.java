package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "ChessPiece{" +
                "color=" + color +
                ", type=" + type +
                '}';
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

        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        if(board.getPiece(myPosition) == null) {

            return null;
        }
        else if(board.getPiece(myPosition).type == PieceType.BISHOP) {

            int x = myPosition.getColumn();
            int y = myPosition.getRow();
            int changex = 1;
            int changey = 1;

            for(int i = 0; i < 4; i++) {

                if(i % 2 == 0) {
                    changex *= -1;
                }
                else {
                    changey *= -1;
                }

                while(x >= 0 && x <= 7 && y >= 0 && y <= 7) {

                    x += changex;
                    y += changey;
                }
            }

            while(x > 0 && y > 0) {


            }
        }
    }

    private final ChessGame.TeamColor color;
    private final PieceType type;
}

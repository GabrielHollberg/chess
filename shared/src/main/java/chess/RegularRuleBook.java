package chess;

import java.util.ArrayList;

public class RegularRuleBook implements ChessRuleBook {
    public ArrayList<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        if (board.getPiece(position).getPieceType() == ChessPiece.PieceType.KING) {
            rule = new KingMovementRule();
        } else if (board.getPiece(position).getPieceType() == ChessPiece.PieceType.QUEEN) {
            rule = new QueenMovementRule();
        } else if (board.getPiece(position).getPieceType() == ChessPiece.PieceType.BISHOP) {
            rule = new BishopMovementRule();
        } else if (board.getPiece(position).getPieceType() == ChessPiece.PieceType.KNIGHT) {
            rule = new KnightMovementRule();
        } else if (board.getPiece(position).getPieceType() == ChessPiece.PieceType.ROOK) {
            rule = new RookMovementRule();
        } else if (board.getPiece(position).getPieceType() == ChessPiece.PieceType.PAWN) {
            rule = new PawnMovementRule();
        }
        return rule.validMoves(board, position);
    }
    public ArrayList<ChessMove> validMoves(ChessBoard board, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ChessPosition position = new ChessPosition(i + 1, j + 1);
                    if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        validWhiteMoves.addAll(validMoves(board, position));
                    }
                }
            }
            return validWhiteMoves;
        } else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ChessPosition position = new ChessPosition(i + 1, j + 1);
                    if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        validBlackMoves.addAll(validMoves(board, position));
                    }
                }
            }
            return validBlackMoves;
        }
    }
    public boolean isBoardValid(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }
    public boolean isInCheck(ChessBoard board, ChessGame.TeamColor color) {
        if(color == ChessGame.TeamColor.WHITE) {
            validBlackMoves.addAll(validMoves(board, ChessGame.TeamColor.BLACK));
            for(int i = 0; i < validBlackMoves.size(); i++) {
                if(board.getPiece(validBlackMoves.get(i).getEndPosition()) != null) {
                    if (board.getPiece(validBlackMoves.get(i).getEndPosition()).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(validBlackMoves.get(i).getEndPosition()).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        validBlackMoves.clear();
                        return true;
                    }
                }
            }
            return false;
        } else {
            validWhiteMoves.addAll(validMoves(board, ChessGame.TeamColor.WHITE));
            for(int i = 0; i < validWhiteMoves.size(); i++) {
                if(board.getPiece(validWhiteMoves.get(i).getEndPosition()) != null) {
                    if (board.getPiece(validWhiteMoves.get(i).getEndPosition()).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(validWhiteMoves.get(i).getEndPosition()).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        validWhiteMoves.clear();
                        return true;
                    }
                }
            }
            return false;
        }
    }
    public boolean isInCheckmate(ChessBoard board, ChessGame.TeamColor color) {
        if(isInCheck(board, color)) {
            if(color == ChessGame.TeamColor.WHITE) {
                validWhiteMoves.addAll(validMoves(board, ChessGame.TeamColor.WHITE));
                for(int i = 0; i < validWhiteMoves.size(); i++) {
                    board.movePiece(validWhiteMoves.get(i));
                    validBlackMoves.addAll(validMoves(board, ChessGame.TeamColor.BLACK));
                    if(!isInCheck(board, color)) {
                        board.movePieceBack(validWhiteMoves.get(i));
                        validBlackMoves.clear();
                        validWhiteMoves.clear();
                        return false;
                    }
                    board.movePieceBack(validWhiteMoves.get(i));
                    validBlackMoves.clear();
                }
                return true;
            } else {
                validBlackMoves.addAll(validMoves(board, ChessGame.TeamColor.BLACK));
                for(int i = 0; i < validBlackMoves.size(); i++) {
                    board.movePiece(validBlackMoves.get(i));
                    validWhiteMoves.addAll(validMoves(board, ChessGame.TeamColor.WHITE));
                    if(!isInCheck(board, color)) {
                        board.movePieceBack(validBlackMoves.get(i));
                        validWhiteMoves.clear();
                        validBlackMoves.clear();
                        return false;
                    }
                    board.movePieceBack(validBlackMoves.get(i));
                    validWhiteMoves.clear();
                }
                return true;
            }
        } else {
            return false;
        }
    }
    public boolean isInStalemate(ChessBoard board, ChessGame.TeamColor color) {
        throw new RuntimeException("Not implemented");
    }

    private MovementRule rule;
    private final ArrayList<ChessMove> validWhiteMoves = new ArrayList<>();
    private final ArrayList<ChessMove> validBlackMoves = new ArrayList<>();
}

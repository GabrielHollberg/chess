package request;

import chess.ChessGame;

public record UpdateGameRequest(int gameID, ChessGame chessGame) {
}

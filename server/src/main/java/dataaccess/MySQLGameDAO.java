package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.DataAccessException;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

// Provides methods for AuthData memory access
public class MySQLGameDAO implements GameDAO {

    public MySQLGameDAO() {}

    public int createGameData(GameData gameData) {
        try {
            try (var conn = DatabaseManager.getConnection()) {
                var preparedStatement = conn.prepareStatement("INSERT INTO game_data (white_username, black_username, game_name, game) VALUES(?, ?, ?, ?)",
                        java.sql.Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, gameData.whiteUsername());
                preparedStatement.setString(2, gameData.blackUsername());
                preparedStatement.setString(3, gameData.gameName());
                Gson gson = new Gson();
                preparedStatement.setString(4, gson.toJson(gameData.game()));
                preparedStatement.executeUpdate();
                try (var rs = preparedStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                    throw new RuntimeException("No generated key returned after insert");
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("failed to establish database connection", e);
        }
    }

    public GameData readGameData(int gameID) {
        try {
            try (var conn = DatabaseManager.getConnection()) {
                var preparedStatement = conn.prepareStatement("SELECT game_id, white_username, black_username, game_name, game FROM game_data WHERE game_id=?");
                preparedStatement.setInt(1, gameID);
                var rs = preparedStatement.executeQuery();
                int gameIDTemp;
                String whiteUsername;
                String blackUsername;
                String gameName;
                String game;
                if (rs.next()) {
                    gameIDTemp = rs.getInt("game_id");
                    whiteUsername = rs.getString("white_username");
                    blackUsername = rs.getString("black_username");
                    gameName = rs.getString("game_name");
                    game = rs.getString("game");
                    Gson gson = new Gson();
                    ChessGame chessGame = gson.fromJson(game, ChessGame.class);
                    return new GameData(gameIDTemp, whiteUsername, blackUsername, gameName, chessGame);
                }
                return null;
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException("failed to establish database connection", e);
        }
    }

    public ArrayList<GameData> readAllGameData() {
        try {
            ArrayList<GameData> allGameData = new ArrayList<>();
            try (var conn = DatabaseManager.getConnection()) {
                var preparedStatement = conn.prepareStatement("SELECT game_id, white_username, black_username, game_name, game FROM game_data");
                var rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    int gameID = rs.getInt("game_id");
                    String whiteUsername = rs.getString("white_username");
                    String blackUsername = rs.getString("black_username");
                    String gameName = rs.getString("game_name");
                    String game = rs.getString("game");
                    Gson gson = new Gson();
                    ChessGame chessGame = gson.fromJson(game, ChessGame.class);
                    allGameData.add(new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame));
                }
                return allGameData;
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException("failed to establish database connection", e);
        }
    }

    public void updateGameData(GameData gameData) {
        try {
            try (var conn = DatabaseManager.getConnection()) {
                var preparedStatement = conn.prepareStatement("UPDATE game_data SET white_username=?, black_username=?, game_name=?, game=? WHERE game_id=?");
                preparedStatement.setString(1, gameData.whiteUsername());
                preparedStatement.setString(2, gameData.blackUsername());
                preparedStatement.setString(3, gameData.gameName());
                Gson gson = new Gson();
                preparedStatement.setString(4, gson.toJson(gameData.game()));
                preparedStatement.setInt(5, gameData.gameID());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("failed to establish database connection", e);
        }
    }

    public void deleteAllGameData() {
        try {
            try (var conn = DatabaseManager.getConnection()) {
                var preparedStatement = conn.prepareStatement("TRUNCATE game_data");
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("failed to establish database connection", e);
        }
    }
}

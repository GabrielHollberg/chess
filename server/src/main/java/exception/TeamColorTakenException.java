package exception;

public class TeamColorTakenException extends DataAccessException {
    public TeamColorTakenException(String message) {
        super(message);
    }
}

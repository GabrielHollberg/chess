package exception;

public class AuthTakenException extends DataAccessException {
    public AuthTakenException(String message) {
        super(message);
    }
}

package exception;

public class AuthTakenException extends RuntimeException {
    public AuthTakenException(String message) {
        super(message);
    }
}

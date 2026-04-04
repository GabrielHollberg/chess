package exception;

public class ResponseException extends RuntimeException {
    public ResponseException(int status, String message) {
        super(message);
    }
}

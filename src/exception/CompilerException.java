package exception;

public class CompilerException extends Exception{

    public CompilerException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompilerException(String message) {
        super(message);
    }
}

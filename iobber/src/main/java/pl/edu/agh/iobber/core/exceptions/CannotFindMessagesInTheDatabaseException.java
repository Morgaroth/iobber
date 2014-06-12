package pl.edu.agh.iobber.core.exceptions;

public class CannotFindMessagesInTheDatabaseException extends Exception {
    public CannotFindMessagesInTheDatabaseException(Exception cause) {
        super(cause);
    }
}

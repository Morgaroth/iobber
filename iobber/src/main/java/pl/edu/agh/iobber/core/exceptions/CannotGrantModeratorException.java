package pl.edu.agh.iobber.core.exceptions;

public class CannotGrantModeratorException extends Exception {
    public CannotGrantModeratorException(Exception e) {
        super(e);
    }
}

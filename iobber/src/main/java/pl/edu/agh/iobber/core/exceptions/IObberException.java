package pl.edu.agh.iobber.core.exceptions;

import org.jivesoftware.smack.XMPPException;

public class IObberException extends Exception {
    public IObberException(XMPPException cause) {
        super(cause);
    }

    public IObberException() {
        super();
    }
}

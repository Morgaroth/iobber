package pl.edu.agh.iobber.core.exceptions;

import org.jivesoftware.smack.XMPPException;

public class UserNotExistsException extends IObberException {
    public UserNotExistsException(XMPPException cause) {
        super(cause);
    }
}

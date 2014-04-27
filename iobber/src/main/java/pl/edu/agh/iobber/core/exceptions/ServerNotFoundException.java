package pl.edu.agh.iobber.core.exceptions;

import org.jivesoftware.smack.XMPPException;

public class ServerNotFoundException extends IObberException {

    public ServerNotFoundException(XMPPException cause) {
        super(cause);
    }
}

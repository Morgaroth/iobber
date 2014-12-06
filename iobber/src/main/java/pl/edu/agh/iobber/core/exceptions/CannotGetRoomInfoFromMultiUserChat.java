package pl.edu.agh.iobber.core.exceptions;

import org.jivesoftware.smack.XMPPException;

public class CannotGetRoomInfoFromMultiUserChat extends Exception {
    public CannotGetRoomInfoFromMultiUserChat(Exception e) {super(e);
    }
}

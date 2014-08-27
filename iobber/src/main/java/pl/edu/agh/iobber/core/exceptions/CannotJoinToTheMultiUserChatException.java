package pl.edu.agh.iobber.core.exceptions;

import org.jivesoftware.smack.XMPPException;

/**
 * Created by HOUSE on 2014-08-26.
 */
public class CannotJoinToTheMultiUserChatException extends Exception {
    public CannotJoinToTheMultiUserChatException(Exception e) {super(e);}
}

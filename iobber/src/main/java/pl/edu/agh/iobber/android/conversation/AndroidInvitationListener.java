package pl.edu.agh.iobber.android.conversation;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.InvitationListener;

import java.util.logging.Logger;

public class AndroidInvitationListener implements InvitationListener {
    private Logger logger = Logger.getLogger(AndroidInvitationListener.class.getSimpleName());
    @Override
    public void invitationReceived(Connection connection, String s, String s2, String s3, String s4, Message message) {
        logger.info("Invitation is received");
        //TODO
    }
}

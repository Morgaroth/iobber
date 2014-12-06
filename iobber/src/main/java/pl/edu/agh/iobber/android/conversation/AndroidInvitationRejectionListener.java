package pl.edu.agh.iobber.android.conversation;

import org.jivesoftware.smackx.muc.InvitationRejectionListener;

import java.util.logging.Logger;

public class AndroidInvitationRejectionListener implements InvitationRejectionListener {
    private Logger logger = Logger.getLogger(AndroidInvitationRejectionListener.class.getSimpleName());
    @Override
    public void invitationDeclined(String s, String s2) {
        logger.info("invitationDeclined " + s + " " + s2);
        //TODO
    }
}

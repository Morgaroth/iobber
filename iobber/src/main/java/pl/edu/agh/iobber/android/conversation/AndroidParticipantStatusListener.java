package pl.edu.agh.iobber.android.conversation;

import org.jivesoftware.smackx.muc.ParticipantStatusListener;

import java.util.logging.Logger;

public class AndroidParticipantStatusListener implements ParticipantStatusListener {
    private Logger logger = Logger.getLogger(AndroidParticipantStatusListener.class.getSimpleName());
    @Override
    public void joined(String s) {
        logger.info("joined " + s);
        //TODO
    }

    @Override
    public void left(String s) {
        logger.info("left " + s);
        //TODO
    }

    @Override
    public void kicked(String s, String s2, String s3) {
        logger.info("kicked " + s + " " + s2 + " " + s3);
        //TODO
    }

    @Override
    public void voiceGranted(String s) {
        logger.info("voiceGranted " + s);
        //TODO
    }

    @Override
    public void voiceRevoked(String s) {
        logger.info("voideRevoked " + s);
        //TODO
    }

    @Override
    public void banned(String s, String s2, String s3) {
        logger.info("banned " + s + " " + s2 + " " + s3);
        //TODO
    }

    @Override
    public void membershipGranted(String s) {
        logger.info("membershipGranted " + s);
    }

    @Override
    public void membershipRevoked(String s) {
        logger.info("membershipRevoked " + s);
    }

    @Override
    public void moderatorGranted(String s) {
        logger.info("moderatorGranted " + s);
        //TODO
    }

    @Override
    public void moderatorRevoked(String s) {
        logger.info("moderatorRevoked " + s);
        //TODO
    }

    @Override
    public void ownershipGranted(String s) {
        logger.info("ownershipGranted " + s);
    }

    @Override
    public void ownershipRevoked(String s) {
        logger.info("ownershipRevoked " + s);
    }

    @Override
    public void adminGranted(String s) {
        logger.info("adminGranted " + s);
        //TODO
    }

    @Override
    public void adminRevoked(String s) {
        logger.info("adminRevoked " + s);
        //TODO
    }

    @Override
    public void nicknameChanged(String s, String s2) {
        logger.info("nicknameChanged " + s + " " + s2);
        //TODO
    }
}

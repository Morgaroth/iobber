package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.logging.Logger;

/**
 * Created by HOUSE on 2014-05-02.
 */
//TODO implement
public class AndroidMessageListener implements MessageListener {
    private Logger logger = Logger.getLogger(AndroidMessageListener.class.getSimpleName());
    @Override
    public void processMessage(Chat chat, Message message) {
        logger.info("processMessage");
    }
}

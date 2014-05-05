package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.logging.Logger;

import static java.lang.String.format;

public class MessageListenerAdapter implements MessageListener {

    private final MsgListener obj;
    private Logger logger = Logger.getLogger(MessageListenerAdapter.class.getSimpleName());

    public MessageListenerAdapter(MsgListener obj) {
        this.obj = obj;

    }

    @Override
    public void processMessage(Chat chat, Message message) {
        logger.info(format("on message %s in chat %s from %s", message.toString(), chat.toString(), chat.getParticipant()));
        if (obj != null) {
            // TODO implement stub
            obj.onMessage(new MessageAdapter(message, new ContactStub()));
        }
    }
}

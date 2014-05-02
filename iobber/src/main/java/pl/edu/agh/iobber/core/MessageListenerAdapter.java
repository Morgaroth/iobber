package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

public class MessageListenerAdapter implements MessageListener {

    private final MsgListener obj;

    public MessageListenerAdapter(MsgListener obj) {
        assert obj != null;
        this.obj = obj;
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        obj.onMessage(new MessageAdapter(message));
    }
}

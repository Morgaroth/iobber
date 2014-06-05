package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.packet.Message;

public class MessageAdapter extends SimpleMessage {
    private final Message obj;

    public MessageAdapter(Message obj, Contact author) {
        this.obj = obj;
        setBody(obj.getBody());
        setDate("customDate");
        setFrom(author.getXMPPIdentifier());
    }


}
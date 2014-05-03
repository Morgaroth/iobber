package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.packet.Message;

public class MessageAdapter extends Msg {
    private final Message obj;

    public MessageAdapter(Message obj) {
        super(obj.getBody());
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "MessageAdapter{" +
                "super(" + super.toString() + "), " +
                "obj=" + obj +
                '}';
    }
}
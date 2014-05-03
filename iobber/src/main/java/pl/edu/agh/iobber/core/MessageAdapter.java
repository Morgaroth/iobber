package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.packet.Message;

public class MessageAdapter extends Msg {
    private final Message obj;

    public MessageAdapter(Message obj) {
        this.obj = obj;
    }

}

package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.packet.Message;

public class MessageAdapter implements Msg {
    private final Message obj;
    private Contact author;

    public MessageAdapter(Message obj, Contact author) {
        this.obj = obj;
        this.author = author;
    }

    @Override
    public String toString() {
        return "MessageAdapter{" +
                "obj=" + obj +
                ", author=" + author +
                '}';
    }

    @Override
    public String getBody() {
        return obj.getBody();
    }

    @Override
    public String getDate() {
        return "customDate";
    }

    @Override
    public Contact getAuthor() {
        return author;
    }

}
package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.MessageListener;

public interface MsgListener {
    public void onMessage(SimpleMessage message);
}

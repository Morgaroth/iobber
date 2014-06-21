package pl.edu.agh.iobber.core;

import org.jivesoftware.smackx.muc.MultiUserChat;

public class MultiConversation implements MsgListener {

    private MultiUserChat muc;

    public MultiConversation(MultiUserChat multiUserChat){
        this.muc = multiUserChat;
    }

    @Override
    public void onMessage(SimpleMessage message) {

    }
}

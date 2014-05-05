package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import java.util.logging.Logger;

import pl.edu.agh.iobber.core.exceptions.IObberException;

import static java.lang.String.format;

public class Conversation {
    private Logger logger = Logger.getLogger(Conversation.class.getSimpleName());
    private String name;
    private Chat chat;
    private String simpleName;

    public Conversation(String title, Chat chat) {
        this.chat = chat;
        name = title;
        simpleName = name;
    }

    public String getName() {
        return name;
    }

    public Chat getChat() {
        return chat;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "name='" + name + '\'' +
                '}';
    }

    public void sendMessage(String text) throws IObberException, XMPPException {
        chat.sendMessage(text);
        logger.info(format("wiadomość \"%s\" wysłano", text));
    }

    public void addMessageListener(MsgListener msgListener) {
        logger.info(format("msg listener %s added", msgListener));
        chat.addMessageListener(new MessageListenerAdapter(msgListener));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return !(name != null ? !name.equals(that.name) : that.name != null);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public Msg getLastMessage() {
        // TODO implement
        return new Msg("stub last Message");
    }

    public String getSimpleName() {
        return (simpleName == null) ? getName() : simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public int unreadedMessages() {
        // TODO implement
        return 7;
    }

    // TODO kolejka wiadomosci + listener
    // TODO dekorator na Chat z smackapi, może coś więcej będziemy dokładać, jakieś metody chociaż, nie wiem, zobaczy sie
}

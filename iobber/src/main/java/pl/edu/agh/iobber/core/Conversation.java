package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import java.util.logging.Logger;

import pl.edu.agh.iobber.android.conversation.ConversationFragment;
import pl.edu.agh.iobber.core.exceptions.IObberException;

import static java.lang.String.format;

// TODO wskazówki

/**
 * to w sumie zrobiłem jako dekorator do Chat, nie wiem czy jest potrzeba, ale jakoś tak
 * może kiedyś się przydać do dodefiniowania jakichś metod
 */
public class Conversation {
    private Logger logger = Logger.getLogger(Conversation.class.getSimpleName());
    private String name;
    private Chat chat;

    public Conversation(String title, Chat chat) {
        this.chat = chat;
        name = title;
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
        chat.addMessageListener(new MessageListenerAdapter(msgListener));
    }

    // TODO dekorator na Chat z smackapi, może coś więcej będziemy dokładać, jakieś metody chociaż, nie wiem, zobaczy sie
}

package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import pl.edu.agh.iobber.android.fileTransfer.AndroidFileTransferListener;
import pl.edu.agh.iobber.android.fileTransfer.AndroidFileTransferManager;
import pl.edu.agh.iobber.core.exceptions.CannotFindMessagesInTheDatabaseException;
import pl.edu.agh.iobber.core.exceptions.CannotGetMessagesFromTheDatabaseException;
import pl.edu.agh.iobber.core.exceptions.CannotSendFileToUserException;
import pl.edu.agh.iobber.core.exceptions.CannotUpdateTheDatabasseException;
import pl.edu.agh.iobber.core.exceptions.IObberException;

import static java.lang.String.format;

public class Conversation implements MsgListener {
    private final LoggedUser owner;
    private final XMPPConnection xmppConnection;
    private BaseManagerMessages baseManagerMessages;
    private String title;
    private Logger logger = Logger.getLogger(Conversation.class.getSimpleName());
    private String name;
    private Chat chat;
    private String simpleName;
    private List<MsgListener> listeners = new LinkedList<MsgListener>();
    private InternalMessageListenerAdapter internalListener;

    public Conversation(String title, LoggedUser owner, BaseManagerMessages baseManagerMessages, XMPPConnection xmppConnection) {
        this.title = title;
        this.owner = owner;
        this.baseManagerMessages = baseManagerMessages;
        name = title;
        this.xmppConnection = xmppConnection;
        internalListener = new InternalMessageListenerAdapter(this, owner);
    }

    public static Conversation createWithoutChat(String title, LoggedUser owner, BaseManagerMessages baseManagerMessages, XMPPConnection xmppConnection) {
        Conversation conversation = new Conversation(title, owner, baseManagerMessages, xmppConnection);
        return conversation;
    }

    public InternalMessageListenerAdapter getInternalListener() {
        return internalListener;
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


    public void sendFile(String filepath) throws CannotSendFileToUserException {
        new AndroidFileTransferManager(xmppConnection).setDestinationUser(owner.getID()).setFilepathToTransferedFile(filepath).setFileTransferListener(new AndroidFileTransferListener()).sendFile();
    }

    public void sendMessage(String text) throws IObberException, XMPPException {
        chat.sendMessage(text);
        logger.info(format("wiadomość \"%s\" wysłano", text));

//        //test
//        try {
//            List<SimpleMessage> list = baseManagerMessages.getLastNMessagesForPerson("adeq9223@gmail.com", 1);
//            List<SimpleMessage> list2 = baseManagerMessages.getEarlierMessagesForPerson("adeq9223@gmail.com", 10, list.get(0));
//            logger.info("Finded messages " + list);
//            logger.info("Finded messages getEarliermessges " + list2);
//        } catch (CannotGetMessagesFromTheDatabaseException e) {
//            e.printStackTrace();
//        }
    }

    public void addMessageListener(MsgListener msgListener) {
        logger.info(format("msg listener %s added", msgListener));
        listeners.add(msgListener);
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
        SimpleMessage simpleMessage = baseManagerMessages.getUnreadedLastMessageForPerson(title, "");
        MsgImpl msg = new MsgImpl(simpleMessage.getBody(), new SimpleContact(simpleMessage.getFrom()));
        return msg;
    }

    public String getSimpleName() {
        return (simpleName == null) ? getName() : simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public int unreadedMessages() {
        return baseManagerMessages.countUnreadedMessagesForPerson(title, "");
    }

    @Override
    public void onMessage(SimpleMessage message) {
        // TODO wsadzenie new MessageAdapter(message, )
        for (MsgListener listener : listeners) {
            listener.onMessage(message);
        }
    }

    public void setUpChat(Chat chat) {
        this.chat = chat;
    }

    // TODO kolejka wiadomosci + listener
    // TODO dekorator na Chat z smackapi, może coś więcej będziemy dokładać, jakieś metody chociaż, nie wiem, zobaczy sie

}

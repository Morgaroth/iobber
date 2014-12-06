package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import pl.edu.agh.iobber.android.conversation.AndroidInvitationListener;
import pl.edu.agh.iobber.core.exceptions.IObberException;

public class LoggedUser implements ContactsResolver {
    private final BaseManagerMessages baseManagerMessages;
    private Logger logger = Logger.getLogger(LoggedUser.class.getSimpleName());

    private HashMap<String, Conversation> activeConversations = new HashMap<String, Conversation>();

    private User user;
    private XMPPConnection xmppConnection;
    private boolean logged;
    private String ID;
    //private List<Contact> contacts = Arrays.asList(new Contact("mietek"), new Contact("wacek"), new Contact("leonidas"), new Contact("Hania"));
    private List<Contact> contacts;
    private AndroidInvitationListener androidInvitationListener;

    public LoggedUser(User user, String ID, XMPPConnection xmppConnection, BaseManagerMessages baseManagerMessages) {
        this.user = user;
        this.ID = ID;
        this.xmppConnection = xmppConnection;
        this.baseManagerMessages = baseManagerMessages;
        logged = false;
        contacts = new ArrayList<Contact>();
        androidInvitationListener = new AndroidInvitationListener();
        MultiUserChat.addInvitationListener(xmppConnection, androidInvitationListener);
        getConctactFromServer();
    }

    private void getConctactFromServer() {
        Roster roster = xmppConnection.getRoster();
        for (RosterEntry rosterEntry : roster.getEntries()) {
            Contact contact = new Contact();
            contact.setRosterEntry(rosterEntry);
            contacts.add(contact);
        }
    }

    public void logout() throws IObberException, XMPPException {
        xmppConnection.disconnect();
    }

    public boolean isLogged() {
        return logged;
    }

    public Roster getRoster() {
        return xmppConnection.getRoster();
    }

    public Collection<Conversation> getActiveConversations() {
        return activeConversations.values();
    }

    public Conversation getOrCreateConversation(String xmppID) {
        Conversation conversation = getConversation(xmppID);
        if (conversation != null) {
            return conversation;
        } else {
            return startConversation(resolve(xmppID));
        }
    }

    public Conversation getConversation(String title) {
        return activeConversations.get(title);
    }

//    public Conversation startConversation(String title, List<Contact> others, MessageListener messageListener) {
//        // TODO tu brakuje połaczenia z serwerem, ustalenia rozmowy itd.........
//        ChatManager chatManager = xmppConnection.getChatManager();
//        Chat chat = chatManager.createChat(title, messageListener);
//        Conversation conversation = new Conversation(title, chat);
//        activeConversations.put(title, conversation);
//        return conversation;
//    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Conversation startConversation(Contact contact) {
        if (activeConversations.containsKey(contact.getXMPPIdentifier())) {
            return activeConversations.get(contact.getXMPPIdentifier());
        } else {
            ChatManager chatManager = xmppConnection.getChatManager();
            Conversation conversation = Conversation.createWithoutChat(contact.getXMPPIdentifier(), this, baseManagerMessages, xmppConnection);
            Chat chat = chatManager.createChat(contact.getRosterEntry().getUser(), conversation.getInternalListener());
            baseManagerMessages.registerContactYouAreChattingWith(contact);
            conversation.setUpChat(chat);
            activeConversations.put(contact.getXMPPIdentifier(), conversation);
            return conversation;
        }
    }

    public void stopConversation(Contact contact) {
        baseManagerMessages.unregisterContactYouAreChattingWith(contact);
    }

    public XMPPConnection getXmppConnection() {
        return xmppConnection;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public Contact resolve(String name) {
        for (Contact contact : getContacts()) {
            if (contact.getXMPPIdentifier().equals(name)) {
                return contact;
            }
        }
        return null;
    }

    public Conversation joinToConversation(Chat chat) {
        Conversation conversation = Conversation.createWithoutChat(chat.getParticipant().split("/")[0], this, baseManagerMessages, xmppConnection);
        chat.addMessageListener(conversation.getInternalListener());

        Contact contact = new Contact();
        Roster roster = xmppConnection.getRoster();
        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            if (entry.getUser().equals(chat.getParticipant().split("/")[0])) {
                contact.setRosterEntry(entry);
            }
        }

        baseManagerMessages.registerContactYouAreChattingWith(contact);
        conversation.setUpChat(chat);
        activeConversations.put(contact.getXMPPIdentifier(), conversation);
        return conversation;
    }
}

package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import pl.edu.agh.iobber.core.exceptions.IObberException;

public class LoggedUser {
    private Logger logger = Logger.getLogger(LoggedUser.class.getSimpleName());

    private HashMap<String, Conversation> activeConversations = new HashMap<String, Conversation>();

    private User user;
    private XMPPConnection xmppConnection;
    private boolean logged;
    private String ID;
    //private List<Contact> contacts = Arrays.asList(new Contact("mietek"), new Contact("wacek"), new Contact("leonidas"), new Contact("Hania"));
    private List<Contact> contacts;

    public LoggedUser(User user, String ID, XMPPConnection xmppConnection) {
        this.user = user;
        this.ID = ID;
        this.xmppConnection = xmppConnection;
        logged = false;
        contacts = new ArrayList<Contact>();
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
        if (activeConversations.containsKey(contact.getName())) {
            return activeConversations.get(contact.getName());
        } else {
            ChatManager chatManager = xmppConnection.getChatManager();
            MessageListener messageListener = new MessageListenerAdapter(null);
            Chat chat = chatManager.createChat(contact.getRosterEntry().getUser(), messageListener);
            Conversation conversation = new Conversation(contact.getName(), chat);
            // TODO zajebiste miejsce na wsadzenie listenera kolejka <-> smackapi
            activeConversations.put(contact.getName(), conversation);
            return conversation;
        }
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
}

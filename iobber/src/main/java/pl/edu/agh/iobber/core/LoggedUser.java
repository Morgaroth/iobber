package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import pl.edu.agh.iobber.core.exceptions.IObberException;

// TODO wskazówki

/**
 * tu proponowałbym wsadzić całą logikę odpowiedzialną za interakcje użytkownika z serwerem xmpp, ale
 * kompletnie niezależne od platform,ma nie mieć pojęcia, że działa na androidzie
 * czyli tu wszystko co użytkownik może mieć, lub zrobić
 */
public class LoggedUser {
    private Logger logger = Logger.getLogger(LoggedUser.class.getSimpleName());

    private XMPPConnection alboCosInnegoCoDostaniePoZalogowaniuCzegoMozeUzywacDoZarzadzaniaSwoimKontem;
    private HashMap<String, Conversation> activeConversations = new HashMap<String, Conversation>();

    private User user;
    private XMPPConnection xmppConnection;
    private boolean logged;
    private String ID;
    private List<Contact> contacts = Arrays.asList(new Contact("mietek"), new Contact("wacek"), new Contact("leonidas"));

    public LoggedUser(User user, String ID, XMPPConnection xmppConnection) {
        this.user = user;
        this.ID = ID;
        this.xmppConnection = xmppConnection;
        logged = false;
    }

    public void logout() throws IObberException, XMPPException {
        xmppConnection.disconnect();
    }

    public boolean isLogged() {
        return logged;
    }

    public Collection<Conversation> getActiveConversations() {
        return activeConversations.values();
    }

    public Conversation getConversation(String title) {
        return activeConversations.get(title);
    }

    public Conversation startConversation(String title, List<Contact> others) {
        // TODO tu brakuje połaczenia z serwerem, ustalenia rozmowy itd
        Conversation conversation = new Conversation(title);
        activeConversations.put(title, conversation);
        return conversation;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Conversation startConversation(Contact contact) {
        // TODO tu brakuje połaczenia z serwerem, ustalenia rozmowy itd
        Conversation conversation = new Conversation(contact.getName());
        activeConversations.put(contact.getName(), conversation);
        return conversation;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
}

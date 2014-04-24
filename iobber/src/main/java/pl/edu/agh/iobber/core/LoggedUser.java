package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import java.util.Collection;
import java.util.logging.Logger;

import pl.edu.agh.iobber.core.exceptions.IObberException;
import pl.edu.agh.iobber.core.exceptions.KurwaZapomnialemZaimplementowac;

// TODO wskazówki

/**
 * tu proponowałbym wsadzić całą logikę odpowiedzialną za interakcje użytkownika z serwerem xmpp, ale
 * kompletnie niezależne od platform,ma nie mieć pojęcia, że działa na androidzie
 * czyli tu wszystko co użytkownik może mieć, lub zrobić
 */
public class LoggedUser {
    private Logger logger = Logger.getLogger(LoggedUser.class.getSimpleName());

    private XMPPConnection alboCosInnegoCoDostaniePoZalogowaniuCzegoMozeUzywacDoZarzadzaniaSwoimKontem;
    private Collection<Conversation> activeConversations;

    private User user;
    private XMPPConnection xmppConnection;
    private boolean logged;

    public LoggedUser(User user, XMPPConnection xmppConnection) {
        this.user = user;
        this.xmppConnection = xmppConnection;
        logged = false;
    }

    public void logout() throws IObberException, XMPPException {
        xmppConnection.disconnect();
    }

    public boolean isLogged(){
        return logged;
    }

    public Collection<Conversation> getActiveConversations() {
        return activeConversations;
    }

    public Conversation getConversation(String title) {
        throw new KurwaZapomnialemZaimplementowac();
    }

    public Conversation startConversation(Object... arguments) {
        throw new KurwaZapomnialemZaimplementowac();
    }
}

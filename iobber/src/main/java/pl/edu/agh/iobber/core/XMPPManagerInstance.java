package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.iobber.core.exceptions.InternetNotFoundException;
import pl.edu.agh.iobber.core.exceptions.NotConnectedToTheServerException;
import pl.edu.agh.iobber.core.exceptions.ServerNotFoundException;
import pl.edu.agh.iobber.core.exceptions.UserNotExistsException;

public class XMPPManagerInstance {

    private Map<String, LoggedUser> loggedUsers;

    protected XMPPManagerInstance() {
        loggedUsers = new HashMap<String, LoggedUser>();
    }

    public LoggedUser loginUser(User user) throws ServerNotFoundException, InternetNotFoundException, UserNotExistsException, NotConnectedToTheServerException {
        XMPPConnection xmppConnection = connectToServer(user);
        try {
            loginUserToServer(xmppConnection, user);
            LoggedUser loggedUser = new LoggedUser(user, user.getLogin(), xmppConnection);
            loggedUsers.put(loggedUser.getID(), loggedUser);
            return loggedUser;
        } finally {
            xmppConnection.disconnect();
        }
    }

    private XMPPConnection connectToServer(User user) throws InternetNotFoundException, ServerNotFoundException {
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo niWIFI = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        NetworkInfo niMOBILE = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        if ((niMOBILE.isConnected() == false) && (niWIFI.isConnected() == false)) {
//            throw new InternetNotFoundException();
//        }
        String server = user.getServerAddress();
        int port = Integer.parseInt(user.getPort());
        boolean authenticationSASL = user.isSslEnabled();
        String login = user.getLogin();

        // TODO walidacja, jakiś wyjątek albo coś, żeby null pointer nie leciał
        String service = login.split("@")[1];

        ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(server, port, service);
        connectionConfiguration.setReconnectionAllowed(true);
        connectionConfiguration.setSASLAuthenticationEnabled(authenticationSASL);
        XMPPConnection xmppConnection = new XMPPConnection(connectionConfiguration);
        try {
            xmppConnection.connect();
            return xmppConnection;
        } catch (XMPPException e) {
            throw new ServerNotFoundException(e);
        }
    }

    private void loginUserToServer(XMPPConnection xmppConnection, User user) throws UserNotExistsException, NotConnectedToTheServerException {
        if (!xmppConnection.isConnected()) {
            throw new NotConnectedToTheServerException();
        }
        String password = user.getPassword();
        // TODO walidacja, jakiś wyjątek albo coś, żeby null pointer nie leciał
        String name = user.getLogin().split("@")[0];
        try {
            xmppConnection.login(name, password);
        } catch (XMPPException e) {
            throw new UserNotExistsException(e);
        }
    }

    public LoggedUser getLoggedUser(String ID) {
        return loggedUsers.get(ID);
    }
}

package pl.edu.agh.iobber.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import pl.edu.agh.iobber.core.exceptions.InternetNotFoundException;
import pl.edu.agh.iobber.core.exceptions.NotConnectedToTheServerException;
import pl.edu.agh.iobber.core.exceptions.ServerNotFoundException;
import pl.edu.agh.iobber.core.exceptions.UserNotExistsException;

public class XMPPManager {


    public static final String PORT = "PORT";
    private static Context context;
    private XMPPConnection xmppConnection;
    private ConnectionConfiguration connectionConfiguration;
    private User user;

    public XMPPManager(User user) {
        this.user = user;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void connectToServer() throws InternetNotFoundException, ServerNotFoundException {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo niWIFI = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo niMOBILE = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((niMOBILE.isConnected() == false) && (niWIFI.isConnected() == false)) {
            throw new InternetNotFoundException();
        }
        String server = user.getValue("SERVER");
        int port = Integer.parseInt(user.getValue(PORT));
        boolean authenticationSASL = user.getValue("SASLAUTH").equals("TAK") ? true : false;
        String login = user.getValue("LOGIN");

        String name, service;
        name = login.split("@")[0];
        service = login.split("@")[1];

        connectionConfiguration = new ConnectionConfiguration("jabber.agh.edu.pl", 5222, "student.agh.edu.pl");
        connectionConfiguration.setReconnectionAllowed(true);
        connectionConfiguration.setSASLAuthenticationEnabled(true);
        xmppConnection = new XMPPConnection(connectionConfiguration);
        try {
            xmppConnection.connect();
        } catch (XMPPException e) {
            throw new ServerNotFoundException();
        }
    }

    public void loginToServer() throws UserNotExistsException, NotConnectedToTheServerException {
        if (!xmppConnection.isConnected()) {
            throw new NotConnectedToTheServerException();
        }
        String password = user.getValue("PASSWORD");
        String login = user.getValue("LOGIN");
        String name = login.split("@")[0];
        try {
            xmppConnection.login("klusek", "fotidep");
        } catch (XMPPException e) {
            throw new UserNotExistsException();
        }
    }

    public XMPPConnection getXMPPConnection() {


        return xmppConnection;
    }

    public void closeConnection() {
        xmppConnection.disconnect();
    }
}

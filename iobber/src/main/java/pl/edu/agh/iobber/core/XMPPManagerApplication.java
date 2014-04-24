package pl.edu.agh.iobber.core;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import pl.edu.agh.iobber.core.exceptions.InternetNotFoundException;
import pl.edu.agh.iobber.core.exceptions.NotConnectedToTheServerException;
import pl.edu.agh.iobber.core.exceptions.ServerNotFoundException;
import pl.edu.agh.iobber.core.exceptions.UserNotExistsException;
import pl.edu.agh.iobber.core.exceptions.WrongPasswordException;

public class XMPPManagerApplication extends Application{

    private static final String PREF = "SharedLogInPreferences";

    private XMPPConnection xmppConnection;
    private ConnectionConfiguration connectionConfiguration;

    public void connectToServer() throws InternetNotFoundException, ServerNotFoundException {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo niWIFI = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo niMOBILE = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((niMOBILE.isConnected() == false) && (niWIFI.isConnected() == false)){
            throw new InternetNotFoundException();
        }

        SharedPreferences sharedPreferences = getSharedPreferences(PREF, 0);

        String server = sharedPreferences.getString("SERVER", "");
        int port = sharedPreferences.getInt("PORT", 0);
        boolean authenticationSASL = sharedPreferences.getInt("AUTH", 0) > 0 ? true : false;
        String login = sharedPreferences.getString("LOGIN", "");
        String name, service;
        name = login.split("@")[0];
        service = login.split("@")[1];

        connectionConfiguration = new ConnectionConfiguration(server, port, service);
        connectionConfiguration.setReconnectionAllowed(true);
        connectionConfiguration.setSASLAuthenticationEnabled(authenticationSASL);
        xmppConnection = new XMPPConnection(connectionConfiguration);
        try {
            xmppConnection.connect();
        } catch (XMPPException e) {
            throw new ServerNotFoundException();
        }
    }

    public void loginToServer() throws UserNotExistsException, NotConnectedToTheServerException {
        if(!xmppConnection.isConnected()){
            throw new NotConnectedToTheServerException();
        }
        SharedPreferences sharedPreferences = getSharedPreferences(PREF, 0);
        String password = sharedPreferences.getString("PASSWORD", "");
        String login = sharedPreferences.getString("LOGIN", "");
        String name = login.split("@")[0];
        try {
            xmppConnection.login(name, password);
        } catch (XMPPException e) {
            throw new UserNotExistsException();
        }
    }

    public XMPPConnection getXMPPConnection() {
        return xmppConnection;
    }

    public void closeConnection(){
        xmppConnection.disconnect();
    }
}

package pl.edu.agh.iobber.core;

import android.os.AsyncTask;
import android.os.Build;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import pl.edu.agh.iobber.android.ChatManagerListener.AndroidChatManagerListenerCore;
import pl.edu.agh.iobber.android.baseMessages.exceptions.CannotAddNewMessageToDatabase;
import pl.edu.agh.iobber.android.baseUsers.exceptions.CannotAddNewUserToDatebase;
import pl.edu.agh.iobber.android.baseUsers.exceptions.CannotDeleteUserFromDatabaseException;
import pl.edu.agh.iobber.android.baseUsers.exceptions.CannotGetUsersFromDatabase;
import pl.edu.agh.iobber.core.exceptions.InternetNotFoundException;
import pl.edu.agh.iobber.core.exceptions.NotConnectedToTheServerException;
import pl.edu.agh.iobber.core.exceptions.NotValidLoginException;
import pl.edu.agh.iobber.core.exceptions.ServerNotFoundException;
import pl.edu.agh.iobber.core.exceptions.UserNotExistsException;

public class XMPPManagerInstance {
    private static final int PACKET_REPLY_TIMEOUT = 3000;
    private Map<String, LoggedUser> loggedUsers;
    private BaseManager baseManager;
    private List<User> users;
    private Logger logger = Logger.getLogger(XMPPManagerInstance.class.getSimpleName());
    private RosterListener temporaryRosterListener;
    private BaseManagerMessages baseManagerMessages;
    private PacketListener packetListenerReceiver;
    private PacketListener packetListenerSent;
    private AndroidChatManagerListenerCore chatManagerListenerCore;
    private XMPPConnection xmppConnection;
    private String serverDomainName;

    protected XMPPManagerInstance() {
        users = new LinkedList<User>();
        loggedUsers = new HashMap<String, LoggedUser>();
    }

    public String getServerDomainName() {
        return serverDomainName;
    }

    public List<User> getAvailableUsersToConnect() {
        try {
            return baseManager.getAvailableUsers();
        } catch (CannotGetUsersFromDatabase cannotGetUsersFromDatabase) {
        }
        return null;
    }

    private void putNewUserToBase(User user) {
        try {
            baseManager.addNewUser(user);
        } catch (CannotAddNewUserToDatebase cannotAddNewUserToDatebase) {
        }
    }

    public void removeUserFromBase(User user) {
        try {
            baseManager.deleteUser(user);
        } catch (CannotDeleteUserFromDatabaseException e) {
        }
    }

    public LoggedUser loginUser(User user) throws ServerNotFoundException, InternetNotFoundException, UserNotExistsException, NotConnectedToTheServerException, NotValidLoginException {
        xmppConnection = connectToServer(user);

        addDefaultChatManagerListener(xmppConnection);
        loginUserToServer(xmppConnection, user);
        addDefaultPacketListener(xmppConnection);
        addRosterListener(xmppConnection);
        LoggedUser loggedUser = new LoggedUser(user, user.getLogin(), xmppConnection, baseManagerMessages);
        loggedUsers.put(loggedUser.getID(), loggedUser);
        putNewUserToBase(user);
        return loggedUser;

    }

    private void addDefaultChatManagerListener(XMPPConnection xmppConnection) {
        if (chatManagerListenerCore != null) {
            xmppConnection.getChatManager().addChatListener(chatManagerListenerCore);
        }
    }

    private void addDefaultPacketListener(XMPPConnection xmppConnection) {
        //listener for receiving packets
        packetListenerReceiver = new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                logger.info("New message received");
                String body = packet.toXML().toString().split("<body>")[1].split("</body>")[0];
                SimpleMessage simpleMessage = new SimpleMessage().from(packet.getFrom().split("/")[0]).to(packet.getTo().split("/")[0]).isReaded(false)
                        .date(new SimpleDateFormat().format(Calendar.getInstance().getTime())).body(body);
                logger.info("New message received " + simpleMessage);
                try {
                    baseManagerMessages.addNewMessage(simpleMessage);
                } catch (CannotAddNewMessageToDatabase cannotAddNewMessageToDatabase) {
                    logger.info("Can not add message to the database in pocketlistener");
                }
            }
        };

        xmppConnection.addPacketListener(packetListenerReceiver, new PacketFilter() {
            @Override
            public boolean accept(Packet packet) {
                logger.info("Packetfilter used" + packet.toXML());
                return packet instanceof Message;
            }
        });

        //listener for sent packets
        packetListenerSent = new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                logger.info("New message sent");
                String body = packet.toXML().toString().split("<body>")[1].split("</body>")[0];
                SimpleMessage simpleMessage = new SimpleMessage().from(packet.getFrom().split("/")[0]).to(packet.getTo().split("/")[0]).isReaded(true)
                        .date(new SimpleDateFormat().format(Calendar.getInstance().getTime())).body(body);
                logger.info("New message sent " + simpleMessage);
                try {
                    baseManagerMessages.addNewSentMessage(simpleMessage);
                } catch (CannotAddNewMessageToDatabase cannotAddNewMessageToDatabase) {
                    logger.info("Can not add message to the database in pocketlistener");
                }
            }
        };

        xmppConnection.addPacketWriterListener(packetListenerSent, new PacketFilter() {
            @Override
            public boolean accept(Packet packet) {
                logger.info("Packetfilter used" + packet.toXML());
                return packet instanceof Message;
            }
        });
    }

    private XMPPConnection connectToServer(User user) throws InternetNotFoundException, ServerNotFoundException, NotValidLoginException, NotConnectedToTheServerException {
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

        if (!login.contains("@")) {
            throw new NotValidLoginException();
        }
        serverDomainName = login.split("@")[1];

        ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(server, port, serverDomainName);
        connectionConfiguration.setReconnectionAllowed(true);
        connectionConfiguration.setSASLAuthenticationEnabled(authenticationSASL);
        connectionConfiguration.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
        //connectionConfiguration.setSelfSignedCertificateEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            logger.severe("====================================================================");
            connectionConfiguration.setTruststoreType("AndroidCAStore");
            connectionConfiguration.setTruststorePassword(null);
            connectionConfiguration.setTruststorePath(null);
        } else {
            logger.severe("--------------------------------------------------------------------");
            connectionConfiguration.setTruststoreType("BKS");
            String path = System.getProperty("javax.net.ssl.trustStore");
            if (path == null)
                path = System.getProperty("java.home") + File.separator + "etc"
                        + File.separator + "security" + File.separator
                        + "cacerts.bks";
            connectionConfiguration.setTruststorePath(path);
        }

        SmackConfiguration.setPacketReplyTimeout(PACKET_REPLY_TIMEOUT);

        final XMPPConnection temporaryXmppConnection = new XMPPConnection(connectionConfiguration);
        try {
            XMPPException execute = new AsyncTask<Void, Void, XMPPException>() {
                @Override
                protected XMPPException doInBackground(Void... voids) {
                    try {
                        temporaryXmppConnection.connect();
                        return null;
                    } catch (XMPPException e) {
                        e.printStackTrace();
                        return e;
                    }
                }
            }.execute().get();
            if (execute != null) {
                throw execute;
            }
            return temporaryXmppConnection;
        } catch (XMPPException e) {
            logger.severe("cached xmppexception " + e.getMessage() + " | " + e.toString());
            throw new ServerNotFoundException(e);
        } catch (Exception e) {
            logger.severe("cached exception " + e.getMessage() + " | " + e.toString());
            throw new NotConnectedToTheServerException();
        }
    }

    public void setRosterListener(RosterListener rosterListener) {
        this.temporaryRosterListener = rosterListener;
    }

    private void addRosterListener(XMPPConnection xmppConnection) {
        if (temporaryRosterListener != null) {
            Roster roster = xmppConnection.getRoster();
            roster.addRosterListener(temporaryRosterListener);
            roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
        }
    }

    private void loginUserToServer(XMPPConnection xmppConnection, User user) throws UserNotExistsException, NotConnectedToTheServerException {
        if (!xmppConnection.isConnected()) {
            throw new NotConnectedToTheServerException();
        }
        String password = user.getPassword();

        String name;
        if (user.getLogin().contains("@")) {
            name = user.getLogin().split("@")[0];
        } else {
            name = user.getLogin();
        }
        try {
            xmppConnection.login(name, password);
        } catch (XMPPException e) {
            logger.severe(e.getMessage() + "|" + e.getLocalizedMessage() + "|" + e);
            throw new UserNotExistsException(e);
        }
    }

    public LoggedUser getLoggedUser(String ID) {
        if (loggedUsers.containsKey(ID) == false) {
            return null;
        }
        return loggedUsers.get(ID);
    }

    public void addBaseManagerMessage(BaseManagerMessages baseManagerMessages) {
        this.baseManagerMessages = baseManagerMessages;
    }

    public BaseManagerMessages getBaseManager() {
        return baseManagerMessages;
    }

    public void setBaseManager(BaseManager baseManager) {
        this.baseManager = baseManager;
    }

    public void setChatManagetListener(AndroidChatManagerListenerCore androidChatManagerListenerCore) {
        this.chatManagerListenerCore = androidChatManagerListenerCore;
    }
}

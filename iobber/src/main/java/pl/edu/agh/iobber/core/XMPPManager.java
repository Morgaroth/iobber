package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.RosterListener;

import pl.edu.agh.iobber.android.ChatManagerListener.AndroidChatManagerListenerCore;
import pl.edu.agh.iobber.core.exceptions.InternetNotFoundException;
import pl.edu.agh.iobber.core.exceptions.NotConnectedToTheServerException;
import pl.edu.agh.iobber.core.exceptions.NotValidLoginException;
import pl.edu.agh.iobber.core.exceptions.ServerNotFoundException;
import pl.edu.agh.iobber.core.exceptions.UserNotExistsException;

public class XMPPManager {
    public static XMPPManagerInstance instance;

    public static XMPPManagerInstance init() {
        if (instance == null) {
            instance = new XMPPManagerInstance();
        }
        return instance;
    }

    public static void addBaseManager(BaseManager baseManager) {
        instance.setBaseManager(baseManager);
    }

    public static LoggedUser loginUser(User user) throws UserNotExistsException, NotConnectedToTheServerException, ServerNotFoundException, InternetNotFoundException, NotValidLoginException {
        return instance.loginUser(user);
    }

    public static LoggedUser getLoggedUser(String userID) {
        return instance.getLoggedUser(userID);
    }

    public static void setRosterListener(RosterListener rosterListener) {
        instance.setRosterListener(rosterListener);
    }

    public static void addBaseManagerMessage(BaseManagerMessages androidBaseManagerMessages) {
        instance.addBaseManagerMessage(androidBaseManagerMessages);
    }

    public static void setChatManagerListener(AndroidChatManagerListenerCore androidChatManagerListenerCore) {
        instance.setChatManagetListener(androidChatManagerListenerCore);
    }
}
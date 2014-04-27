package pl.edu.agh.iobber.core;

import pl.edu.agh.iobber.core.exceptions.InternetNotFoundException;
import pl.edu.agh.iobber.core.exceptions.NotConnectedToTheServerException;
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

    public static LoggedUser loginUser(User user) throws UserNotExistsException, NotConnectedToTheServerException, ServerNotFoundException, InternetNotFoundException {
        return instance.loginUser(user);
    }

    public static LoggedUser getLoggedUser(String userID) {
        return instance.getLoggedUser(userID);
    }
}
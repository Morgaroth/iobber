package pl.edu.agh.iobber;

import android.app.Application;

import java.util.logging.Logger;

import pl.edu.agh.iobber.core.User;
import pl.edu.agh.iobber.core.XMPPManager;


public class GlobalVars extends Application {
    // TODO może jakieś lepsze nazwy
    public XMPPManager xmppManager;
    private Logger logger = Logger.getLogger(GlobalVars.class.getSimpleName());

    public GlobalVars() {
        logger.info("GlobalVars constructed");
        //XMPPManager.setContext(this);
        //xmppManager = new XMPPManager(new User());
    }

}

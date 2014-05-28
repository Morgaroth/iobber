package pl.edu.agh.iobber.android;

import android.app.Application;

import java.util.logging.Logger;

import pl.edu.agh.iobber.core.XMPPManager;
import pl.edu.agh.iobber.core.XMPPManagerInstance;


public class GlobalVars extends Application {
    // TODO może jakieś lepsze nazwy
    public XMPPManagerInstance xmppManager;
    private Logger logger = Logger.getLogger(GlobalVars.class.getSimpleName());

    public GlobalVars() {
        logger.info("GlobalVars constructed");
        xmppManager = XMPPManager.init();
    }

}

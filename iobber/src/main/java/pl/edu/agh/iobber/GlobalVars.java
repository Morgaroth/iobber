package pl.edu.agh.iobber;

import android.app.Application;

import java.util.logging.Logger;

import pl.edu.agh.iobber.core.XMPPManagerApplication;

public class GlobalVars extends Application {
    // TODO może jakieś lepsze nazwy
    public XMPPManagerApplication xmppManager;
    private Logger logger = Logger.getLogger(GlobalVars.class.getSimpleName());

    public GlobalVars() {
        logger.info("GlobalVars constructed");
        xmppManager = new XMPPManagerApplication();
    }

}

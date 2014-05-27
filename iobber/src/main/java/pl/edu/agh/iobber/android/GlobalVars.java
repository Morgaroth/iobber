package pl.edu.agh.iobber.android;

import android.app.Application;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import org.jivesoftware.smack.SmackAndroid;

import java.util.logging.Logger;

import pl.edu.agh.iobber.R;
import pl.edu.agh.iobber.core.XMPPManager;
import pl.edu.agh.iobber.core.XMPPManagerInstance;


public class GlobalVars extends Application {
    // TODO może jakieś lepsze nazwy
    public XMPPManagerInstance xmppManager;
    private Logger logger = Logger.getLogger(GlobalVars.class.getSimpleName());

    public GlobalVars() {
        logger.info("GlobalVars constructed");
        xmppManager = XMPPManager.init();
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                SystemClock.sleep(100);
                SmackAndroid.init(GlobalVars.this);
                logger.info("Smack android initialized");
                return null;
            }
        }.execute();
    }


}

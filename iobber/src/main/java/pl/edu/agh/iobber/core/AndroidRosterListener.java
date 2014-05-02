package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;

import java.util.Collection;
import java.util.logging.Logger;

/**
 * Created by HOUSE on 2014-05-02.
 */
//TODO implement
public class AndroidRosterListener implements RosterListener {
    private Logger logger = Logger.getLogger(AndroidRosterListener.class.getSimpleName());
    @Override
    public void entriesAdded(Collection<String> strings) {
        logger.info("ectriesAdded");
    }
    @Override
    public void entriesUpdated(Collection<String> strings) {
        logger.info("entriesUpdated");
    }
    @Override
    public void entriesDeleted(Collection<String> strings) {

        logger.info("entriesDeleted");
    }
    @Override
    public void presenceChanged(Presence presence) {
        logger.info("presenceChange");
    }
}

package pl.edu.agh.iobber.android;

import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;

import java.util.Collection;
import java.util.logging.Logger;

//TODO implement
public class AndroidRosterListener implements RosterListener {
    private Logger logger = Logger.getLogger(AndroidRosterListener.class.getSimpleName());

    @Override
    public void entriesAdded(Collection<String> strings) {
        logger.info("ectriesAdded " + strings);
    }

    @Override
    public void entriesUpdated(Collection<String> strings) {
        logger.info("entriesUpdated " + strings);
    }

    @Override
    public void entriesDeleted(Collection<String> strings) {

        logger.info("entriesDeleted " + strings);
    }

    @Override
    public void presenceChanged(Presence presence) {
        logger.info("presenceChange " + presence);
    }
}

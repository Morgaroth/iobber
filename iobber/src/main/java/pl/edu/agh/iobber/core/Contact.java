package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.RosterEntry;

public class Contact {
    private RosterEntry rosterEntry;

    public Contact() {
        rosterEntry = null;
    }

    public String getName() {
        if (rosterEntry != null) {
            return rosterEntry.getUser();
        }
        return "STUB!";
    }

    public void setName(String name) {
        rosterEntry.setName(name);
    }

    public RosterEntry getRosterEntry() {
        return rosterEntry;
    }

    public void setRosterEntry(RosterEntry rosterEntry) {
        this.rosterEntry = rosterEntry;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + getName() + '\'' +
                '}';
    }
}

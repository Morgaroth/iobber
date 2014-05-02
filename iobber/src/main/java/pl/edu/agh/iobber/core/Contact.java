package pl.edu.agh.iobber.core;

import android.content.Context;

import org.jivesoftware.smack.RosterEntry;

public class Contact {
    private RosterEntry rosterEntry;

    public Contact(){
        rosterEntry = null;
    }

    public String getName() {
        if(rosterEntry != null){
            return rosterEntry.getUser();
        }
        return null;
    }

    public void setName(String name){
        rosterEntry.setName(name);
    }

    public void setRosterEntry(RosterEntry rosterEntry){
        this.rosterEntry = rosterEntry;
    }

    public RosterEntry getRosterEntry(){
        return rosterEntry;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + getName() + '\'' +
                '}';
    }
}

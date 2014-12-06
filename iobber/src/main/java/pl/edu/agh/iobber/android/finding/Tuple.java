package pl.edu.agh.iobber.android.finding;

import pl.edu.agh.iobber.core.SimpleMessage;

public class Tuple {
    public SimpleMessage msgBefore;
    public SimpleMessage msgExact;
    public SimpleMessage msgAfter;

    Tuple(SimpleMessage msgBefore, SimpleMessage msgExact, SimpleMessage msgAfter) {
        this.msgBefore = msgBefore;
        this.msgExact = msgExact;
        this.msgAfter = msgAfter;
    }
}
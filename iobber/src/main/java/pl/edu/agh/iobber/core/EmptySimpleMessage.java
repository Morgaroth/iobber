package pl.edu.agh.iobber.core;

/**
 * Created by HOUSE on 2014-05-21.
 */
public class EmptySimpleMessage extends SimpleMessage {

    @Override
    public String toString() {
        return "";
    }

    @Override
    public boolean isStub() {
        return true;
    }
}

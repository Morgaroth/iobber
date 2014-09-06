package pl.edu.agh.iobber.core;

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

package pl.edu.agh.iobber.core;

public class ContactStub extends Contact {
    @Override
    public String getXMPPIdentifier() {
        return "Stub Name!";
    }

    @Override
    public String toString() {
        return "ContactStub {}";
    }
}

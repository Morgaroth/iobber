package pl.edu.agh.iobber.core;

public class SimpleContact extends Contact {

    private String name;

    public SimpleContact(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}

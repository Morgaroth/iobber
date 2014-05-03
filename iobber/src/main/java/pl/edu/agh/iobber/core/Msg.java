package pl.edu.agh.iobber.core;

public class Msg {
    private String text;

    public Msg(String text) {

        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "text='" + text + '\'' +
                '}';
    }
}

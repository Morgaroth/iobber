package pl.edu.agh.iobber.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MsgImpl implements Msg {
    private final String text;
    private String date;
    private Contact author;

    public MsgImpl(String text, Contact author) {
        this.author = author;
        this.date = new SimpleDateFormat().format(Calendar.getInstance().getTime());
        this.text = text;
    }

    @Override
    public String getBody() {
        return text;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public Contact getAuthor() {
        return author;
    }
}

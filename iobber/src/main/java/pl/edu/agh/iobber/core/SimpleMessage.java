package pl.edu.agh.iobber.core;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = "messages")
public class SimpleMessage {

    public static final String ID_FIELD_MESSAGE = "id";
    public static final String FROM_FIELD_MESSAGE = "from";
    public static final String TO_FIELD_MESSAGE = "to";
    public static final String DATE_FIELD_MESSAGE = "date";
    public static final String BODY_FIELD_MESSAGE = "body";
    public static final String ISREADED_FIELD_MESSAGE = "isReaded";
    public static final String NAME_OF_ROOM_FIELD_MESSAGE = "nameOfRoom";

    @DatabaseField(generatedId = true, useGetSet = true, columnName = ID_FIELD_MESSAGE)
    private Integer id;

    @DatabaseField(useGetSet = true, columnName = FROM_FIELD_MESSAGE)
    private String from;

    @DatabaseField(useGetSet = true, columnName = TO_FIELD_MESSAGE)
    private String to;

    @DatabaseField(useGetSet = true, columnName = DATE_FIELD_MESSAGE)
    private String date;

    @DatabaseField(useGetSet = true, columnName = BODY_FIELD_MESSAGE)
    private String body;

    @DatabaseField(useGetSet = true, columnName = ISREADED_FIELD_MESSAGE)
    private boolean isReaded;

    @DatabaseField(useGetSet = true, columnName = NAME_OF_ROOM_FIELD_MESSAGE)
    private String nameOfRoom;

    public SimpleMessage() {
        nameOfRoom = "";
    }

    public SimpleMessage from(String from) {
        this.from = from;
        return this;
    }

    public SimpleMessage to(String to) {
        this.to = to;
        return this;
    }

    public SimpleMessage date(String date) {
        this.date = date;
        return this;
    }

    public SimpleMessage body(String body) {
        this.body = body;
        return this;
    }

    public SimpleMessage isReaded(boolean isReaded) {
        this.isReaded = isReaded;
        return this;
    }

    public SimpleMessage nameOfRoom(String nameOfRoom) {
        this.nameOfRoom = nameOfRoom;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean getIsReaded() {
        return isReaded;
    }

    public void setIsReaded(boolean isReaded) {
        this.isReaded = isReaded;
    }

    public String getNameOfRoom(){return nameOfRoom;}

    public void setNameOfRoom(String nameOfRoom){this.nameOfRoom = nameOfRoom;}

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof SimpleMessage)) return false;

        SimpleMessage other = (SimpleMessage) o;
        return this.getFrom().equals(other.getFrom()) &&
                this.getIsReaded() == other.getIsReaded() &&
                this.getBody().equals(other.getBody()) &&
                this.getTo().equals(other.getTo()) &&
                this.getDate().equals(other.getDate()) &&
                this.getNameOfRoom().equals(other.getNameOfRoom());
    }

    @Override
    public int hashCode() {
        return getFrom().hashCode() * 3 +
                getBody().hashCode() * 7 +
                getTo().hashCode() * 11 +
                getDate().hashCode() * 13 +
                (getIsReaded() == true ? 1 : 2) +
                getNameOfRoom().hashCode() * 17;
    }

    public boolean isStub() {
        return false;
    }

    @Override
    public String toString() {
        return "SimpleMessage{" +
                "id=" + id +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", date='" + date + '\'' +
                ", nameOfRoom='" + nameOfRoom + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}

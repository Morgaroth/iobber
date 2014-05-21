package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.PacketListener;

import java.util.List;

import pl.edu.agh.iobber.android.baseMessages.exceptions.CannotAddNewMessageToDatabase;
import pl.edu.agh.iobber.core.SimpleMessage;
import pl.edu.agh.iobber.core.exceptions.CannotFindMessagesInTheDatabaseException;
import pl.edu.agh.iobber.core.exceptions.CannotGetMessagesFromTheDatabaseException;
import pl.edu.agh.iobber.core.exceptions.CannotUpdateTheDatabasseException;

public interface BaseManagerMessages {

    void addNewMessage(SimpleMessage simpleMessage) throws CannotAddNewMessageToDatabase;

    void addNewSentMessage(SimpleMessage simpleMessage) throws CannotAddNewMessageToDatabase;

    List<SimpleMessage> getUnreadedMessagerForPerson(Contact contact);

    SimpleMessage getUnreadedLastMessageForPerson(String title);

    int countUnreadedMessagesForPerson(String title);

    void markMessageAsReaded(SimpleMessage simpleMessage) throws CannotUpdateTheDatabasseException;

    List<SimpleMessage> getLaterMessagesForPerson(Contact contact, int number, SimpleMessage messageToBeExtractedFrom) throws CannotGetMessagesFromTheDatabaseException;

    List<SimpleMessage> getEarlierMessagesForPerson(Contact contact, int number, SimpleMessage messageToBeExtractedFrom) throws CannotGetMessagesFromTheDatabaseException;

    List<SimpleMessage> findMessages(String contact, String dateFrom, String dateTo, String body) throws CannotFindMessagesInTheDatabaseException;

    void addtoUnreadedMessages(SimpleMessage simpleMessage) throws CannotAddNewMessageToDatabase;

    void setBaseManagerMessagesConfiguration(BaseManagerMessagesConfiguration baseManagerMessagesConfiguration);

    void registerContactYouAreChattingWith(Contact contact);

    void unregisterContactYouAreChattingWith(Contact contact);
}
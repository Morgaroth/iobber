package pl.edu.agh.iobber.android.baseMessages;

import android.content.SharedPreferences;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import pl.edu.agh.iobber.android.baseMessages.exceptions.CannotAddNewMessageToDatabase;
import pl.edu.agh.iobber.core.BaseManagerMessages;
import pl.edu.agh.iobber.core.BaseManagerMessagesConfiguration;
import pl.edu.agh.iobber.core.Contact;
import pl.edu.agh.iobber.core.EmptySimpleMessage;
import pl.edu.agh.iobber.core.SimpleMessage;
import pl.edu.agh.iobber.core.exceptions.CannotFindMessagesInTheDatabaseException;
import pl.edu.agh.iobber.core.exceptions.CannotGetMessagesFromTheDatabaseException;
import pl.edu.agh.iobber.core.exceptions.CannotUpdateTheDatabasseException;

/*
W listach data zapisana jest w postaci dd-MM-yy hh:mm
W bazie danych data jest przekonwenterwana do sekund ktore uplynely od 1970
Dlatego trzeba za kazdym razem konwenterowac te dane
 */

public class AndroidBaseManagerMessages implements BaseManagerMessages {

    private Dao<SimpleMessage, Integer> simpleMessageDao;
    private Logger logger = Logger.getLogger(AndroidBaseManagerMessages.class.getSimpleName());

    private List<SimpleMessage> unreadedMessages;
    private BaseManagerMessagesConfiguration baseManagerMessagesConfiguration;
    private Object obj;
    private Map<String, List<SimpleMessage>> messagesForConversations;
    private DateFormat sdff;
    private SharedPreferences sharedPreferences;
    private long timeLifeOfMesssagesInSeconds = 0;

    public AndroidBaseManagerMessages(Dao<SimpleMessage, Integer> dao, SharedPreferences sharedPreferences) {
        sdff = new SimpleDateFormat("dd-MM-yy hh:mm");
        this.sharedPreferences = sharedPreferences;
        unreadedMessages = new LinkedList<SimpleMessage>();
        messagesForConversations = new HashMap<String, List<SimpleMessage>>();
        this.simpleMessageDao = dao;
        obj = new Object();
        setTimeLifeOfMessages();
        refreshTheDatabase();
        readFromTheDatabaseUnreadedEalierMessages();
    }

    private void readFromTheDatabaseUnreadedEalierMessages() {
        QueryBuilder<SimpleMessage, Integer> queryBuilder = simpleMessageDao.queryBuilder();
        Where<SimpleMessage, Integer> where = queryBuilder.where();
        try {
            where.eq(SimpleMessage.ISREADED_FIELD_MESSAGE, false);
            unreadedMessages = queryBuilder.query();
        } catch (SQLException e) {
            logger.info("Cannnot read messages from the database so as to initial map");
        }
    }

    private void setTimeLifeOfMessages() {
        timeLifeOfMesssagesInSeconds = sharedPreferences.getInt(BaseManagerMessagesConfiguration.NUMBER_OF_DAYS, 0);
        timeLifeOfMesssagesInSeconds *= 86400;
    }

    @Override
    public void addNewMessage(SimpleMessage simpleMessage) throws CannotAddNewMessageToDatabase {
        try {
            Date date = new Date();
            try {
                date = sdff.parse(simpleMessage.getDate());
            } catch (ParseException e) {
                logger.info("Cannot cast date in addNewMessage");
            }
            String convertedTime = String.valueOf(date.getTime() / 1000);
            sendMessageToProperQueue(simpleMessage);
            simpleMessage.setDate(convertedTime);
            simpleMessageDao.create(simpleMessage);
            logger.info("New message added to the database " + simpleMessage);
        } catch (SQLException e) {
            logger.info("Cannot add new message to the database");
            throw new CannotAddNewMessageToDatabase();
        }
    }

    private void sendMessageToProperQueue(SimpleMessage simpleMessage) {
        synchronized (obj) {
            if (messagesForConversations.containsKey(simpleMessage.getFrom())) {
                messagesForConversations.get(simpleMessage.getFrom()).add(simpleMessage);
            } else {
                unreadedMessages.add(simpleMessage);
            }
        }
    }

    @Override
    public void addNewSentMessage(SimpleMessage simpleMessage) throws CannotAddNewMessageToDatabase {
        try {
            Date date = new Date();
            try {
                date = sdff.parse(simpleMessage.getDate());
            } catch (ParseException e) {
                logger.info("Cannot cast date in addNewMessage");
            }
            String convertedTime = String.valueOf(date.getTime() / 1000);
            simpleMessage.setDate(convertedTime);
            simpleMessageDao.create(simpleMessage);
            logger.info("New sent message added to the database " + simpleMessage);
        } catch (SQLException e) {
            logger.info("New sent message can not be added to the database " + simpleMessage);
            throw new CannotAddNewMessageToDatabase();
        }
    }

    @Override
    public List<SimpleMessage> getUnreadedMessagerForPerson(Contact contact) {
        synchronized (obj) {
            return messagesForConversations.get(contact.getName());
        }
    }

    @Override
    public List<SimpleMessage> getLastNMessagesForPerson(String title, int numberOfMessages) throws CannotGetMessagesFromTheDatabaseException {
        List<SimpleMessage> list = null;

        QueryBuilder<SimpleMessage, Integer> queryBuilder = simpleMessageDao.queryBuilder();
        Where<SimpleMessage, Integer> where = queryBuilder.where();

        try {
            where.or(where.eq(SimpleMessage.TO_FIELD_MESSAGE, title), where.eq(SimpleMessage.FROM_FIELD_MESSAGE, title));
            list = queryBuilder.limit(numberOfMessages).orderBy(SimpleMessage.ID_FIELD_MESSAGE, false).query();
        } catch (SQLException e) {
            logger.info("Cannnot read messages from the database so as to initial map");
            throw new CannotGetMessagesFromTheDatabaseException();
        }

        return list;
    }

    @Override
    public SimpleMessage getUnreadedLastMessageForPerson(String recipient) {
        SimpleMessage sim;
        synchronized (obj) {
            if (messagesForConversations.get(recipient).size() > 0) {
                sim = messagesForConversations.get(recipient).get(0);
            } else {
                sim = new SimpleMessage().to(" ").isReaded(true).from(" ").date("01-01-70 00:00").body(" ");
            }
        }
        return sim;
    }

    @Override
    public int countUnreadedMessagesForPerson(String title) {
        int number;
        synchronized (obj) {
            number = messagesForConversations.get(title).size();
        }
        return number;
    }

    @Override
    public void markMessageAsReaded(SimpleMessage simpleMessage) throws CannotUpdateTheDatabasseException {
        UpdateBuilder<SimpleMessage, Integer> updateBuilder = simpleMessageDao.updateBuilder();
        try {
            Date date = new Date();
            try {
                date = sdff.parse(simpleMessage.getDate());
            } catch (ParseException e) {
                logger.info("Cannot cast date in addNewMessage");
            }
            String convertedTime = String.valueOf(date.getTime() / 1000);

            updateBuilder.updateColumnValue(SimpleMessage.ISREADED_FIELD_MESSAGE, true);
            updateBuilder.where().eq(SimpleMessage.BODY_FIELD_MESSAGE, simpleMessage.getBody()).and().
                    eq(SimpleMessage.DATE_FIELD_MESSAGE, convertedTime).and().
                    eq(SimpleMessage.FROM_FIELD_MESSAGE, simpleMessage.getFrom()).and().
                    eq(SimpleMessage.TO_FIELD_MESSAGE, simpleMessage.getTo());
            updateBuilder.update();
            synchronized (obj) {
                messagesForConversations.get(simpleMessage.getFrom()).remove(simpleMessage);
            }
            logger.info("markMessageAsReaded executed ok " + simpleMessage);
        } catch (SQLException e) {
            logger.info("markMessageAsReaded fail " + simpleMessage);
            throw new CannotUpdateTheDatabasseException();
        }
    }

    @Override
    public List<SimpleMessage> getEarlierMessagesForPerson(Contact contact, int number, SimpleMessage messageToBeExtractedFrom) throws CannotGetMessagesFromTheDatabaseException {
        //search id of the message
        long id;
        QueryBuilder<SimpleMessage, Integer> queryBuilder = simpleMessageDao.queryBuilder();
        Where<SimpleMessage, Integer> where = queryBuilder.where();

        try {
            where.eq(SimpleMessage.BODY_FIELD_MESSAGE, messageToBeExtractedFrom.getBody()).and().
                    eq(SimpleMessage.DATE_FIELD_MESSAGE, messageToBeExtractedFrom).and().
                    eq(SimpleMessage.FROM_FIELD_MESSAGE, messageToBeExtractedFrom.getFrom()).and().
                    eq(SimpleMessage.TO_FIELD_MESSAGE, messageToBeExtractedFrom.getTo());
            SimpleMessage simpleMessage = queryBuilder.limit(1).query().get(0);
            id = simpleMessage.getId();
        } catch (SQLException e) {
            logger.info("Cannot get messages from the database");
            throw new CannotGetMessagesFromTheDatabaseException();
        }

        //Find messages
        List<SimpleMessage> list = null;
        QueryBuilder<SimpleMessage, Integer> queryBuilder2 = simpleMessageDao.queryBuilder();
        Where<SimpleMessage, Integer> where2 = queryBuilder2.where();
        try {
            where2.or(where2.eq(SimpleMessage.TO_FIELD_MESSAGE, contact), where2.eq(SimpleMessage.FROM_FIELD_MESSAGE, contact)).and().
                    lt(SimpleMessage.ID_FIELD_MESSAGE, id);
            list = queryBuilder2.offset(id).limit(number).query();
        } catch (SQLException e) {
            logger.info("Cannot get messages from the database");
            throw new CannotGetMessagesFromTheDatabaseException();
        }

        return list;
    }

    @Override
    public List<SimpleMessage> getLaterMessagesForPerson(Contact contact, int number, SimpleMessage messageToBeExtractedFrom) throws CannotGetMessagesFromTheDatabaseException {
        //search id of the message
        long id;
        QueryBuilder<SimpleMessage, Integer> queryBuilder = simpleMessageDao.queryBuilder();
        Where<SimpleMessage, Integer> where = queryBuilder.where();

        try {
            where.eq(SimpleMessage.BODY_FIELD_MESSAGE, messageToBeExtractedFrom.getBody()).and().
                    eq(SimpleMessage.DATE_FIELD_MESSAGE, messageToBeExtractedFrom).and().
                    eq(SimpleMessage.FROM_FIELD_MESSAGE, messageToBeExtractedFrom.getFrom()).and().
                    eq(SimpleMessage.TO_FIELD_MESSAGE, messageToBeExtractedFrom.getTo());
            SimpleMessage simpleMessage = queryBuilder.limit(1).query().get(0);
            id = simpleMessage.getId();
        } catch (SQLException e) {
            logger.info("Cannot get messages from the database");
            throw new CannotGetMessagesFromTheDatabaseException();
        }

        //Find messages
        List<SimpleMessage> list = null;
        QueryBuilder<SimpleMessage, Integer> queryBuilder2 = simpleMessageDao.queryBuilder();
        Where<SimpleMessage, Integer> where2 = queryBuilder2.where();
        try {
            where2.or(where2.eq(SimpleMessage.TO_FIELD_MESSAGE, contact), where2.eq(SimpleMessage.FROM_FIELD_MESSAGE, contact)).and().
                    gt(SimpleMessage.ID_FIELD_MESSAGE, id);
            list = queryBuilder2.offset(id).limit(number).query();
        } catch (SQLException e) {
            logger.info("Cannot get messages from the database");
            throw new CannotGetMessagesFromTheDatabaseException();
        }

        return list;

    }

    @Override   //czas przyjmowany jest tutaj w postaci dd-MM-yy hh:mm
    public List<SimpleMessage> findMessages(String contact, String dateFrom, String dateTo, String body) throws CannotFindMessagesInTheDatabaseException {
        List<SimpleMessage> list;

        QueryBuilder<SimpleMessage, Integer> queryBuilder = simpleMessageDao.queryBuilder();
        Where<SimpleMessage, Integer> where = queryBuilder.where();

        String modifiedBody = new StringBuilder().append("%").append(body).append("%").toString();

        try {
            where.or(where.and(where.eq(SimpleMessage.TO_FIELD_MESSAGE, contact), where.like(SimpleMessage.BODY_FIELD_MESSAGE, modifiedBody)),
                    where.and(where.eq(SimpleMessage.FROM_FIELD_MESSAGE, contact), where.like(SimpleMessage.BODY_FIELD_MESSAGE, modifiedBody)));
            if (dateFrom != null) {
                Date date = new Date();
                try {
                    date = sdff.parse(dateFrom);
                } catch (ParseException e) {
                    logger.info("Cannot cast date in addNewMessage");
                }
                String convertedTime = String.valueOf(date.getTime() / 1000);
                where.and().gt(SimpleMessage.DATE_FIELD_MESSAGE, convertedTime);
            }
            if (dateTo != null) {
                Date date = new Date();
                try {
                    date = sdff.parse(dateTo);
                } catch (ParseException e) {
                    logger.info("Cannot cast date in addNewMessage");
                }
                String convertedTime = String.valueOf(date.getTime() / 1000);
                where.and().lt(SimpleMessage.DATE_FIELD_MESSAGE, convertedTime);
            }
        } catch (SQLException e) {
            logger.info("Cannot find messages in the database");
            throw new CannotFindMessagesInTheDatabaseException(e);
        }

        try {
            list = queryBuilder.query();
        } catch (SQLException e) {
            logger.info("Cannot query the database with messages");
            throw new CannotFindMessagesInTheDatabaseException(e);
        }

        List<SimpleMessage> returnList = new ArrayList<SimpleMessage>();

        for (SimpleMessage s : list) {
            try {
                QueryBuilder<SimpleMessage, Integer> queryBuilder2 = simpleMessageDao.queryBuilder();
                Where<SimpleMessage, Integer> where2 = queryBuilder2.where();
                where2.or(where2.eq(SimpleMessage.TO_FIELD_MESSAGE, contact), where2.eq(SimpleMessage.FROM_FIELD_MESSAGE, contact)).and().lt(SimpleMessage.ID_FIELD_MESSAGE, s.getId());
                List<SimpleMessage> sim = queryBuilder2.orderBy(SimpleMessage.ID_FIELD_MESSAGE, false).limit(1).query();
                if (sim.size() > 0) {
                    returnList.add(sim.get(0));
                } else {
                    returnList.add(new EmptySimpleMessage());
                }

                returnList.add(s);

                QueryBuilder<SimpleMessage, Integer> queryBuilder3 = simpleMessageDao.queryBuilder();
                Where<SimpleMessage, Integer> where3 = queryBuilder3.where();
                where3.or(where3.eq(SimpleMessage.TO_FIELD_MESSAGE, contact), where3.eq(SimpleMessage.FROM_FIELD_MESSAGE, contact)).and().gt(SimpleMessage.ID_FIELD_MESSAGE, s.getId());
                List<SimpleMessage> sim3 = queryBuilder3.orderBy(SimpleMessage.ID_FIELD_MESSAGE, true).limit(1).query();
                if (sim3.size() > 0) {
                    returnList.add(sim3.get(0));
                } else {
                    returnList.add(new EmptySimpleMessage());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return returnList;
    }

    @Override
    public void addtoUnreadedMessages(SimpleMessage simpleMessage) throws CannotAddNewMessageToDatabase {
        logger.info("new message added to unreaded " + simpleMessage);
        this.unreadedMessages.add(simpleMessage);
        this.addNewMessage(simpleMessage);
    }

    @Override
    public void setBaseManagerMessagesConfiguration(BaseManagerMessagesConfiguration baseManagerMessagesConfiguration) {
        this.baseManagerMessagesConfiguration = baseManagerMessagesConfiguration;
        updateDatabase();
    }

    private void updateDatabase() {
        updateSharedPreferences();
        adjustTheBaseForTheNewSettings();
    }

    private void updateSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(BaseManagerMessagesConfiguration.NUMBER_OF_DAYS, baseManagerMessagesConfiguration.getNumberOfDaysToSaveHistory());
        editor.commit();
    }

    private void adjustTheBaseForTheNewSettings() {
        DeleteBuilder<SimpleMessage, Integer> deleteBuilder = simpleMessageDao.deleteBuilder();
        try {
            deleteBuilder.where().gt(SimpleMessage.DATE_FIELD_MESSAGE, timeLifeOfMesssagesInSeconds);
            deleteBuilder.delete();
        } catch (SQLException e) {
            logger.info("Cannot delete old messages from the database");
        }
    }

    private void refreshTheDatabase() {
        adjustTheBaseForTheNewSettings();
    }

    @Override
    public void registerContactYouAreChattingWith(Contact contact) {
        messagesForConversations.put(contact.getName(), new LinkedList<SimpleMessage>());
    }

    @Override
    public void unregisterContactYouAreChattingWith(Contact contact) {
        messagesForConversations.remove(contact.getName());
    }
}

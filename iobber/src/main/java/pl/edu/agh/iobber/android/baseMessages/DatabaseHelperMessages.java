package pl.edu.agh.iobber.android.baseMessages;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import pl.edu.agh.iobber.R;
import pl.edu.agh.iobber.core.SimpleMessage;
import pl.edu.agh.iobber.core.User;

public class DatabaseHelperMessages extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "messages.db";
    private static final int DATABASE_VERSION = 5;

    private Dao<SimpleMessage, Integer> messagesDao = null;

    private static DatabaseHelperMessages helper = null;
    private static final AtomicInteger usageCounter = new AtomicInteger(0);

    private Logger logger = Logger.getLogger(DatabaseHelperMessages.class.getSimpleName());

    public DatabaseHelperMessages(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        logger.info("DatabaseHelperMessages executed");
    }

    public static synchronized DatabaseHelperMessages getHelper(Context context) {
        if (helper == null) {
            helper = new DatabaseHelperMessages(context);
        }
        usageCounter.incrementAndGet();
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelperMessages.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, SimpleMessage.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelperMessages.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelperMessages.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, SimpleMessage.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelperMessages.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<SimpleMessage, Integer> getSimpleMessageDao() throws SQLException {
        if (messagesDao == null) {
            messagesDao = getDao(SimpleMessage.class);
        }
        return messagesDao;
    }

    @Override
    public void close() {
        if (usageCounter.decrementAndGet() == 0) {
            super.close();
            messagesDao = null;
            helper = null;
        }
    }

}

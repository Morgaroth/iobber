package pl.edu.agh.iobber.android.baseUsers;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import pl.edu.agh.iobber.android.baseUsers.exceptions.CannotAddNewUserToDatebase;
import pl.edu.agh.iobber.android.baseUsers.exceptions.CannotDeleteUserFromDatabaseException;
import pl.edu.agh.iobber.android.baseUsers.exceptions.CannotGetUsersFromDatabase;
import pl.edu.agh.iobber.core.BaseManager;
import pl.edu.agh.iobber.core.User;

import static java.lang.String.format;

/**
 * Created by HOUSE on 2014-04-30.
 */
public class AndroidBaseManager implements BaseManager {

    private Logger logger = Logger.getLogger(AndroidBaseManager.class.getSimpleName());

    private Dao<User, String> userDao;

    public AndroidBaseManager(Dao<User, String> userDao){
        this.userDao = userDao;
    }

    public void addNewUser(User user) throws CannotAddNewUserToDatebase{
        try {
            userDao.create(user);
            logger.info("New user is added to the database");
        } catch (SQLException e) {
            logger.info("Cannot add new user to the database");
            throw new CannotAddNewUserToDatebase();
        }
    }
    public void deleteUser(User user) throws CannotDeleteUserFromDatabaseException{
        try {
            userDao.delete(user);
            logger.info("User is deleted from the database");
        } catch (SQLException e) {
            logger.info("Cannot delete user from the database");
            throw new CannotDeleteUserFromDatabaseException();
        }
    }
    public List<User> getAvailableUsers() throws CannotGetUsersFromDatabase{
        try {
            List<User> u = userDao.queryForAll();
            logger.info("Users getted from the database");
            return u;
        } catch (SQLException e) {
            logger.info("Cannot gezt users from the database");
            throw new CannotGetUsersFromDatabase();
        }
    }

}

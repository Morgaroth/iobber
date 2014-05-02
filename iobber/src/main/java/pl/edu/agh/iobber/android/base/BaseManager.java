package pl.edu.agh.iobber.android.base;

import java.util.List;

import pl.edu.agh.iobber.android.base.exceptions.CannotAddNewUserToDatebase;
import pl.edu.agh.iobber.android.base.exceptions.CannotDeleteUserFromDatabaseException;
import pl.edu.agh.iobber.android.base.exceptions.CannotGetUsersFromDatabase;
import pl.edu.agh.iobber.core.User;

/**
 * Created by HOUSE on 2014-04-30.
 */
public interface BaseManager {

    void addNewUser(User user) throws CannotAddNewUserToDatebase;
    void deleteUser(User user) throws CannotDeleteUserFromDatabaseException;
    List<User> getAvailableUsers() throws CannotGetUsersFromDatabase;

}

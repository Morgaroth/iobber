package pl.edu.agh.iobber.core;

import java.util.List;

import pl.edu.agh.iobber.android.baseUsers.exceptions.CannotAddNewUserToDatebase;
import pl.edu.agh.iobber.android.baseUsers.exceptions.CannotDeleteUserFromDatabaseException;
import pl.edu.agh.iobber.android.baseUsers.exceptions.CannotGetUsersFromDatabase;
import pl.edu.agh.iobber.core.User;

/**
 * Created by HOUSE on 2014-04-30.
 */
public interface BaseManager {

    void addNewUser(User user) throws CannotAddNewUserToDatebase;

    void deleteUser(User user) throws CannotDeleteUserFromDatabaseException;

    List<User> getAvailableUsers() throws CannotGetUsersFromDatabase;

}

package pl.edu.agh.iobber.core;


import pl.edu.agh.iobber.core.exceptions.YetAnotherException;
import pl.edu.agh.iobber.core.exceptions.KurwaZapomnialemZaimplementowac;
import pl.edu.agh.iobber.core.exceptions.ServerNotFoundException;
import pl.edu.agh.iobber.core.exceptions.UserNotExistsException;
import pl.edu.agh.iobber.core.exceptions.WrongPasswordException;

public class User {

    public static User login(Credentials credentials) throws UserNotExistsException, ServerNotFoundException, YetAnotherException, WrongPasswordException {
        // TODO implementacja
        if (credentials.nick.equals("u")) {
            throw new UserNotExistsException();
        } else if (credentials.nick.equals("s")) {
            throw new ServerNotFoundException();
        } else if (credentials.nick.equals("a")) {
            throw new YetAnotherException();
        } else if (credentials.nick.equals("w")) {
            throw new WrongPasswordException();
        } else if (credentials.nick.equals("q")) {
            return new User();
        }
        // TODO lista wyjątków oczywiście może się rozwinąć jak będzie potrzeba
        throw new KurwaZapomnialemZaimplementowac();
    }

    void logout() {
        throw new KurwaZapomnialemZaimplementowac();
    }

    boolean isLogged() {
        throw new KurwaZapomnialemZaimplementowac();
    }

    // TODO tu logika chyba do operacji, bo zawsze robi to zalogowany użytkownik
}

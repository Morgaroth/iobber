package pl.edu.agh.iobber.core;

import java.util.logging.Logger;

import pl.edu.agh.iobber.core.exceptions.KurwaZapomnialemZaimplementowac;

// TODO wskazówki

/**
 * tu myślę, że powinna być taka fasada to zarządzania logiką połączenia z serwerem, ale ponad połączeniem użytkownika
 * umiejąca zalogoważ użytkonika, ewentualanie sprawdzać jakieś parametry serwera
 */
public class XMPPManager {
    private Logger logger = Logger.getLogger(XMPPManager.class.getSimpleName());

    public static LoggedUser login(String login, String password, String... otherArguments) {
        throw new KurwaZapomnialemZaimplementowac();
    }
}

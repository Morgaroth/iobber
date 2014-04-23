package pl.edu.agh.iobber.core;

public class Credentials {
    String nick;
    String password;
    String serverAddress;
    Object everyAnotherNeedCredential;

    public Credentials(String nick, String password) {

        this.nick = nick;
        this.password = password;
    }
}

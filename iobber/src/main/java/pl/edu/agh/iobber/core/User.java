package pl.edu.agh.iobber.core;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {

    public static final String LOGIN = "LOGIN";
    public static final String PASSWORD = "PASSWORD";
    public static final String PORT = "PORT";
    public static final String SERVER = "SERVER";
    public static final String SASLAUTH = "SASLAUTH";
    Map<String, String> mapa;

    public User() {
        this.mapa = new HashMap<String, String>();
    }

    public void setValue(String key, String value) {
        mapa.put(key, value);
    }

    public String getValue(String key) {
        return mapa.get(key);
    }


    public User login(String login) {
        mapa.put(LOGIN, login);
        return this;
    }

    public User password(String password) {
        mapa.put(PASSWORD, password);
        return this;
    }

    public User port(String port) {
        mapa.put(PORT, port);
        return this;
    }

    public User serverAddress(String address) {
        mapa.put(SERVER, address);
        return this;
    }

    public User sslEnable(boolean enabled) {
        mapa.put(SASLAUTH, enabled ? "YES" : "NO");
        return this;
    }

    public String getLogin() {
        return mapa.get(LOGIN);
    }

    public String getPassword() {
        return mapa.get(PASSWORD);
    }

    public String getPort() {
        return mapa.get(PORT);
    }

    public String getServerAddress() {
        return mapa.get(SERVER);
    }

    public boolean isSslEnabled() {
        String ssAuth = mapa.get(SASLAUTH);
        return ssAuth != null && ssAuth.equals("YES");
    }
}

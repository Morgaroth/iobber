package pl.edu.agh.iobber.core;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@DatabaseTable(tableName = "users")
public class User implements Serializable {

    public static final String LOGIN_FIELD_NAME = "login";
    public static final String PASSWORD_FIELD_NAME = "password";
    public static final String PORT_FIELD_NAME = "port";
    public static final String SERVERADDRESS_FIELD_NAME = "serverAddress";
    public static final String SSLENABLE_FIELD_NAME = "sslEnable";

    @DatabaseField(id = true, useGetSet = true, columnName = LOGIN_FIELD_NAME)
    private String login;

    private String password;

    @DatabaseField(useGetSet = true, columnName = PORT_FIELD_NAME)
    private String port;

    @DatabaseField(useGetSet = true, columnName = SERVERADDRESS_FIELD_NAME)
    private String serverAddress;

    @DatabaseField(useGetSet = true, columnName = SSLENABLE_FIELD_NAME)
    private String sslEnable;

    public User() {
        password = "";
    }

    public User login(String login) {
        this.login = login;
        return this;
    }

    public User password(String password) {
        this.password = password;
        return this;
    }

    public User port(String port) {
        this.port = port;
        return this;
    }

    public User serverAddress(String address) {
        this.serverAddress = address;
        return this;
    }

    public User sslEnable(boolean enabled) {
        this.sslEnable = enabled ? "YES" : "NO";
        return this;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getSslEnable() {
        return sslEnable;
    }

    public void setSslEnable(String sslEnable) {
        this.sslEnable = sslEnable;
    }

    public boolean isSslEnabled() {
        return sslEnable.equals("YES");
    }

    @Override
    public int hashCode() {
        return login.hashCode() * password.hashCode() * port.hashCode()
                * serverAddress.hashCode() * sslEnable.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        User user = (User) obj;
        return user.getLogin().equals(login) && user.getPassword().equals(password)
                && user.getPort().equals(port) && user.getServerAddress().equals(serverAddress) && user.isSslEnabled() == isSslEnabled();
    }
}

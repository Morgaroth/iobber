package pl.edu.agh.iobber.core;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.iobber.core.exceptions.YetAnotherException;
import pl.edu.agh.iobber.core.exceptions.KurwaZapomnialemZaimplementowac;
import pl.edu.agh.iobber.core.exceptions.ServerNotFoundException;
import pl.edu.agh.iobber.core.exceptions.UserNotExistsException;
import pl.edu.agh.iobber.core.exceptions.WrongPasswordException;

public class User implements Serializable {

    Map<String, String> mapa;

    public User(){
        this.mapa = new HashMap<String, String>();
    }

    public void setValue(String key, String value){
        mapa.put(key, value);
    }

    public String getValue(String key){
        return mapa.get(key);
    }
}

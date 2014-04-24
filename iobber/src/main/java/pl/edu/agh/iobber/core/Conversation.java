package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.Chat;

import java.util.logging.Logger;

// TODO wskazówki

/**
 * to w sumie zrobiłem jako dekorator do Chat, nie wiem czy jest potrzeba, ale jakoś tak
 * może kiedyś się przydać do dodefiniowania jakichś metod
 */
public class Conversation {
    private Logger logger = Logger.getLogger(Conversation.class.getSimpleName());
    private String name;
    private Chat chat;

    public String getName() {
        return name;
    }

    public Chat getChat() {
        return chat;
    }
    // TODO dekorator na Chat z smackapi, może coś więcej będziemy dokładać, jakieś metody chociaż, nie wiem, zobaczy sie
}

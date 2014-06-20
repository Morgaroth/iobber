package pl.edu.agh.iobber.android.ChatManagerListener;

import org.jivesoftware.smack.Chat;

import java.util.logging.Logger;

import pl.edu.agh.iobber.android.MainActivity;
import pl.edu.agh.iobber.core.ChatManagerListenerCore;

public class AndroidChatManagerListenerCore implements ChatManagerListenerCore {

    private MainActivity mainActivity;
    private Logger logger = Logger.getLogger(AndroidChatManagerListenerCore.class.getSimpleName());

    public AndroidChatManagerListenerCore(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void chatCreated(Chat chat, boolean b) {
        if(b == false){
            logger.info("New conversation is comming with " + chat.getParticipant().split("/")[0]);
            mainActivity.joinToConversatonWith(chat);
        }
    }
}

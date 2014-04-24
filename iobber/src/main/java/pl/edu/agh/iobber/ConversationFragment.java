package pl.edu.agh.iobber;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

public class ConversationFragment extends Fragment {

    private final Chat chatDelegate;
    private Logger logger = Logger.getLogger(ConversationFragment.class.getSimpleName());

    public ConversationFragment(Chat chat) {
        this.chatDelegate = chat;
    }

    public static ConversationFragment newInstance(Chat chat) {
        ConversationFragment fragment = new ConversationFragment(chat);
        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private static boolean isActionSend(int actionId, KeyEvent event) {
        return actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_SEND ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        logger.info("creating view for chat " + chatDelegate);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        EditText messageEditor = (EditText) rootView.findViewById(R.id.chatLine);
        messageEditor.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        logger.info("used event=" + event);
                        if (isActionSend(actionId, event)) {
                            sendMessage(v.getText());
                            clearEditText(v);
                            return true;
                        }
                        return false;
                    }
                }
        );
        return rootView;
    }

    private void clearEditText(TextView v) {
        v.clearComposingText();
    }

    private void sendMessage(CharSequence text) {
        try {
            logger.info(format("sending message \"%s\"", text));
            chatDelegate.sendMessage(text.toString());
        } catch (XMPPException e) {
            logger.log(Level.SEVERE, "error sending message", e);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        ((MainActivity) activity).onSectionAttached(
//                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}

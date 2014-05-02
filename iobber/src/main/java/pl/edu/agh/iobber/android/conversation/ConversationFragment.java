package pl.edu.agh.iobber.android.conversation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.agh.iobber.R;
import pl.edu.agh.iobber.core.Conversation;
import pl.edu.agh.iobber.core.Msg;
import pl.edu.agh.iobber.core.MsgListener;
import pl.edu.agh.iobber.core.exceptions.IObberException;

import static java.lang.String.format;

public class ConversationFragment extends ListFragment implements MsgListener {

    private final Conversation delegate;
    private Logger logger = Logger.getLogger(ConversationFragment.class.getSimpleName());
    private List<Msg> messages = new LinkedList<Msg>();
    private ConversationListAdapter adapter;

    public ConversationFragment(Conversation conversation) {
        this.delegate = conversation;
    }


    public static ConversationFragment newInstance(Conversation chat) {
        return new ConversationFragment(chat);
    }

    private static boolean isActionSend(int actionId, KeyEvent event) {
        return actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_SEND ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
    }

    @Override
    public String toString() {
        return "ConversationFragment{" +
                "delegate=" + delegate +
                '}';
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        logger.info("creating view for chat " + delegate);
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
        delegate.addMessageListener(this);
        return rootView;
    }

    private void clearEditText(TextView v) {
        v.clearComposingText();
    }

    private void sendMessage(CharSequence text) {
        try {
            logger.info(format("sending message \"%s\"", text));
            delegate.sendMessage(text.toString());

        } catch (IObberException e) {
            logger.log(Level.SEVERE, "error sending message", e);
        }
    }

    /**
     * @deprecated for test use only
     */
    @Deprecated
    private void addMsgToListDirectly(String text){
        onMessage(new Msg(text));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        ((MainActivity) activity).onSectionAttached(
//                getArguments().getInt(ARG_SECTION_NUMBER));
    }


    @Override
    public void onMessage(Msg message) {
        addNewMessageToList(message);
    }

    private void addNewMessageToList(Msg message) {
        if (adapter == null) {
            adapter = new ConversationListAdapter(getActivity(), R.layout.conversation_list_item_layout);
            setListAdapter(adapter);
        }
        messages.add(message);
        adapter.updateContent(messages);
    }
}

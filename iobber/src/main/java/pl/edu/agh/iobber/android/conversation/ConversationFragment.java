package pl.edu.agh.iobber.android.conversation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.jivesoftware.smack.XMPPException;

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
        View rootView = inflater.inflate(R.layout.conversation_fragment_layout, container, false);

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
            //addMsgToListDirectly(text.toString());
        } catch (IObberException e) {
            logger.log(Level.SEVERE, "error sending message", e);
        } catch (XMPPException e) {
            logger.log(Level.SEVERE, "Cannnot send a message", e);
        }
    }

    /**
     * @deprecated for test use only
     */
    @Deprecated
    private void addMsgToListDirectly(String text) {
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
        logger.info(message + " received");
    }

    private void addNewMessageToList(Msg message) {
        messages.add(message);
        updateAdapter();
    }

    private void updateAdapter() {
        final ListView view = getListView();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (adapter == null) {
                    adapter = new ConversationListAdapter(getActivity(), R.layout.conversation_fragment_list_item_layout);
                    view.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    ConversationFragment.this.setListAdapter(adapter);
                    logger.info("adapter for conversation created");
                }
                adapter.updateContent(messages);
                logger.info("content updated " + messages);
            }
        });
    }
//
//    class PrivAdapter extends ArrayAdapter<Msg> {
//        private Logger logger = Logger.getLogger(PrivAdapter.class.getSimpleName());
//
//        public PrivAdapter(Context context, List<Msg> objects) {
//            super(context, android.R.layout.simple_list_item_1, objects);
//            logger.info(format("CYCKI %s", objects));
//        }
//
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View v = convertView;
//            if (v == null) {
//                LayoutInflater vi;
//                vi = LayoutInflater.from(getContext());
//                v = vi.inflate(R.layout.conversation_fragment_list_item_layout, null);
//            }
//
//            logger.info(format("%s generate view for item %s", this, getItem(position)));
//            TextView bodyView = (TextView) v.findViewById(R.id.conversation_list_item_body);
//            bodyView.setText(getItem(position).getText());
//
//            return v;
//        }
//    }

}
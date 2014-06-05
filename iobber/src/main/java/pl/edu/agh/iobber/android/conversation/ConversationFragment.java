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
import android.widget.ListView;
import android.widget.TextView;

import org.jivesoftware.smack.XMPPException;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.agh.iobber.R;
import pl.edu.agh.iobber.core.Conversation;
import pl.edu.agh.iobber.core.MsgListener;
import pl.edu.agh.iobber.core.SimpleMessage;
import pl.edu.agh.iobber.core.XMPPManager;
import pl.edu.agh.iobber.core.exceptions.CannotGetMessagesFromTheDatabaseException;
import pl.edu.agh.iobber.core.exceptions.IObberException;

import static java.lang.String.format;
import static java.lang.Thread.sleep;

public class ConversationFragment extends ListFragment implements MsgListener {

    private Conversation delegate;
    private SimpleMessage messagesToInit = null;
    private Logger logger = Logger.getLogger(ConversationFragment.class.getSimpleName());
    private EndlessAdapter adapter;

    public ConversationFragment() {
    }

    public static ConversationFragment newInstance(Conversation chat) {
        return new ConversationFragment().setUp(chat);
    }

    public static ConversationFragment newInstance(Conversation chat, SimpleMessage messagesToInit) {
        return new ConversationFragment().setUp(chat, messagesToInit);
    }

    private static boolean isActionSend(int actionId, KeyEvent event) {
        return actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_SEND ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
    }

    public ConversationFragment setUp(Conversation conversation) {
        this.delegate = conversation;
        return this;
    }

    public ConversationFragment setUp(Conversation chat, SimpleMessage messagesToInit) {
        this.delegate = chat;
        this.messagesToInit = messagesToInit;
        return this;
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter(messagesToInit);
        messagesToInit = null;
    }

    private void clearEditText(TextView v) {
        v.setText("");
    }

    private void sendMessage(CharSequence text) {
        try {
            logger.info(format("sending message \"%s\"", text));
            delegate.sendMessage(text.toString());
            addMsgToListDirectly(text.toString());
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
        try {
            sleep(80);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scrollAdapterDown();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        ((MainActivity) activity).onSectionAttached(
//                getArguments().getInt(ARG_SECTION_NUMBER));
    }


    @Override
    public void onMessage(SimpleMessage message) {
        addNewMessageToList(message);
        logger.info(message + " received");
    }

    private void addNewMessageToList(SimpleMessage message) {
        scrollAdapterDown();
    }

    private void scrollAdapterDown() {
        if (adapter != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.scrollDown();
                }
            });
        }
    }

    private void setAdapter(final SimpleMessage messageToInit) {
        final ListView view = getListView();
        if (adapter == null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (adapter == null) {
                        try {
                            List<SimpleMessage> messages;
                            if (messageToInit == null) {
                                messages = XMPPManager.instance.getBaseManager().getLastNMessagesForPerson(delegate.getName(), 20);
                            } else {
                                messages = new LinkedList<SimpleMessage>();
                                List<SimpleMessage> earlier = XMPPManager.instance.getBaseManager().getEarlierMessagesForPerson(delegate.getName(), 7, messageToInit);
                                List<SimpleMessage> later = XMPPManager.instance.getBaseManager().getLaterMessagesForPerson(delegate.getName(), 7, messageToInit);
                                for (SimpleMessage msg : earlier) {
                                    messages.add(msg);
                                }
                                messages.add(messageToInit);
                                for (SimpleMessage msg : later) {
                                    messages.add(msg);
                                }
                            }
                            ListView listView = ConversationFragment.this.getListView();
                            adapter = new EndlessAdapter<ConversationListAdapter>(getActivity(), new ConversationListAdapter(getActivity(), messages), listView, true, messageToInit != null).setContactID(delegate.getChat().getParticipant());
                            ConversationFragment.this.setListAdapter(adapter);
                            if (messageToInit != null) {
                                listView.setSelection(6);
                            } else {
                                listView.setSelection(messages.size() - 4);
                            }
                            logger.info("adapter for conversation created");
                        } catch (CannotGetMessagesFromTheDatabaseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }


    public void scrollToPosition(int pos) {
        getListView().setSelection(pos);
    }


}

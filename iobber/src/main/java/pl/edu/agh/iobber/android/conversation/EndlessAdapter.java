package pl.edu.agh.iobber.android.conversation;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.commonsware.cwac.endless.EndslessAdapter;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import pl.edu.agh.iobber.R;
import pl.edu.agh.iobber.core.Contact;
import pl.edu.agh.iobber.core.Msg;
import pl.edu.agh.iobber.core.SimpleMessage;
import pl.edu.agh.iobber.core.XMPPManager;
import pl.edu.agh.iobber.core.XMPPManagerInstance;
import pl.edu.agh.iobber.core.exceptions.CannotGetMessagesFromTheDatabaseException;

public class EndlessAdapter<T extends ListAdapter> extends com.commonsware.cwac.endless.EndslessAdapter {

    private static Logger logger = Logger.getLogger(EndlessAdapter.class.getSimpleName());
    private ListAdapter wrapped;
    private RotateAnimation rotate;
    private View pendingView;
    private String xmppUserID;
    private List<SimpleMessage> earlierMessagesForPerson;

    public EndlessAdapter(Context context, T wrapped, boolean keepOnAppendingAtStart, boolean keepOnAppendingAtEnd) {
        super(wrapped, keepOnAppendingAtStart, keepOnAppendingAtEnd);
        logger.info("endless adapter initialized with adapter with " + wrapped.getCount() + " items");
        rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setDuration(600);
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setRepeatCount(Animation.INFINITE);
    }

    public EndlessAdapter setContactID(String xmppUserID) {
        this.xmppUserID = xmppUserID;
        return this;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        logger.info("endless adapter returns view at position " + position);
        return super.getView(position, convertView, parent);
    }

    @Override
    protected View getPendingView(ViewGroup parent) {
        logger.info("getPendingView " + parent);
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_item_layout, null);

        pendingView = row.findViewById(android.R.id.text1);
        pendingView.setVisibility(View.GONE);
        pendingView = row.findViewById(R.id.pending_item_spinner);
        pendingView.setVisibility(View.VISIBLE);
        startProgressAnimation();

        return (row);
    }

    @Override
    protected boolean cacheStartInBackground() throws Exception {
        logger.info("cacheStartInBackground");
        SystemClock.sleep(1000 * 3);
        try {
            if (getWrappedAdapter().getCount() > 0) {
                SimpleMessage item = (SimpleMessage) getWrappedAdapter().getItem(0);
                earlierMessagesForPerson = XMPPManager.instance.getBaseManager().getEarlierMessagesForPerson(xmppUserID, 20, item);
                logger.info("query on more msgs returned " + earlierMessagesForPerson);
            } else {
                earlierMessagesForPerson = XMPPManager.instance.getBaseManager().getLastNMessagesForPerson(xmppUserID, 20);
                logger.info("query on last msgs returned " + earlierMessagesForPerson);
            }
            return earlierMessagesForPerson.size() != 0;
        } catch (CannotGetMessagesFromTheDatabaseException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean cacheEndInBackground() throws Exception {
        logger.info("cacheEndtInBackground");
        return false;//true - jeśli jest co dodać i jest sens wywoływać appendAtEnd
    }

    @Override
    protected void appendCachedDataAtStart() {
        logger.info("appendCachedDataAtStart");
        if (earlierMessagesForPerson != null) {
            Collections.reverse(earlierMessagesForPerson);
            ArrayAdapter adapter = (ArrayAdapter) getWrappedAdapter();
            for (SimpleMessage simpleMessage : earlierMessagesForPerson) {
                adapter.insert(simpleMessage, 0);
            }
            earlierMessagesForPerson = null;
        }
    }

    @Override
    protected void appendCachedDataAtEnd() {
        logger.info("appendCachedDataAtEnd");
    }

    void startProgressAnimation() {
        logger.info("progressAnimation");
        if (pendingView != null) {
            pendingView.startAnimation(rotate);
        }
    }
}

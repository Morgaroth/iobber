package pl.edu.agh.iobber.android.conversation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.logging.Logger;

import pl.edu.agh.iobber.R;
import pl.edu.agh.iobber.core.Msg;
import pl.morgaroth.utils.CustomListAdapter;

public class ConversationListAdapter extends CustomListAdapter<Msg> {
    private Logger logger = Logger.getLogger(ConversationListAdapter.class.getSimpleName());

    public ConversationListAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    public ConversationListAdapter(Context context, int itemLayoutId, List<Msg> objects) {
        super(context, itemLayoutId, objects);
    }

    @Override
    public View fillItem(int position, View view, ViewGroup parent, Msg item) {
        //logger.info(format("%s generate view for item %s", this, item));
        TextView bodyView = (TextView) view.findViewById(R.id.conversation_list_item_body);
        bodyView.setText(item.getText());

        return view;
    }

    @Override
    public String toString() {
        return ConversationListAdapter.class.getSimpleName() + "@" + hashCode();
    }
}

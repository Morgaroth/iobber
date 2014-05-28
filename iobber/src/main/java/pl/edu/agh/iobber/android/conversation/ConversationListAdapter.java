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

    public ConversationListAdapter(Context context) {
        super(context, R.layout.conversation_fragment_list_item_layout);
    }

    public ConversationListAdapter(Context context, List<Msg> objects) {
        super(context, R.layout.conversation_fragment_list_item_layout, objects);
    }

    @Override
    public View fillItem(int position, View view, ViewGroup parent, Msg item) {
        //logger.info(format("%s generate view for item %s", this, item));
        TextView bodyView = (TextView) view.findViewById(R.id.conversation_fragment_list_item_body);
        bodyView.setText(item.getBody());

        TextView author = (TextView) view.findViewById(R.id.conversation_fragment_list_item_author);
        author.setText(item.getAuthor().getName());

        TextView date = (TextView) view.findViewById(R.id.conversation_fragment_list_item_date);
        date.setText(item.getDate());


        return ConversationItemFormatter.format(getContext(), view);
    }

    @Override
    public String toString() {
        return ConversationListAdapter.class.getSimpleName() + "@" + hashCode();
    }
}

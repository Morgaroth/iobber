package pl.edu.agh.iobber.android.conversation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import pl.edu.agh.iobber.R;
import pl.edu.agh.iobber.core.Msg;
import pl.edu.agh.iobber.core.SimpleMessage;
import pl.morgaroth.utils.CustomListAdapter;

import static java.lang.String.format;

public class ConversationListAdapter extends CustomListAdapter<SimpleMessage> {
    public static final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.US);
    private Logger logger = Logger.getLogger(ConversationListAdapter.class.getSimpleName());

    public ConversationListAdapter(Context context) {
        super(context, R.layout.conversation_fragment_list_item_layout);
    }

    public ConversationListAdapter(Context context, List<SimpleMessage> objects) {
        super(context, R.layout.conversation_fragment_list_item_layout, objects);
    }

    @Override
    public View fillItem(int position, View view, ViewGroup parent, SimpleMessage item) {
//        logger.info(format("%s generate view at index %d for item %s", this, position, item));
        TextView bodyView = (TextView) view.findViewById(R.id.conversation_fragment_list_item_body);

        bodyView.setText(item.getBody());

        TextView author = (TextView) view.findViewById(R.id.conversation_fragment_list_item_author);
        author.setText(item.getFrom());

        TextView date = (TextView) view.findViewById(R.id.conversation_fragment_list_item_date);
        date.setText(formatter.format(Long.valueOf(item.getDate())));


        return ConversationItemFormatter.format(getContext(), view);
    }

    @Override
    public String toString() {
        return ConversationListAdapter.class.getSimpleName() + "@" + hashCode();
    }
}

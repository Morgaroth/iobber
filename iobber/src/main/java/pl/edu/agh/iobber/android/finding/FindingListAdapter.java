package pl.edu.agh.iobber.android.finding;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.logging.Logger;

import pl.edu.agh.iobber.R;
import pl.edu.agh.iobber.core.SimpleMessage;
import pl.morgaroth.utils.CustomListAdapter;

public class FindingListAdapter extends CustomListAdapter<SimpleMessage> {
    private Logger logger = Logger.getLogger(FindingListAdapter.class.getSimpleName());

    public FindingListAdapter(Context context) {
        super(context, R.layout.conversation_fragment_list_item_layout);
    }

    public FindingListAdapter(Context context, List<SimpleMessage> objects) {
        super(context, R.layout.conversation_fragment_list_item_layout, objects);
    }

    @Override
    public View fillItem(int position, View view, ViewGroup parent, SimpleMessage item) {
        //logger.info(format("%s generate view for item %s", this, item));
        TextView bodyView = (TextView) view.findViewById(R.id.conversation_fragment_list_item_body);
        bodyView.setText(item.getBody());

        TextView author = (TextView) view.findViewById(R.id.conversation_fragment_list_item_author);
        author.setText(item.getFrom());

        TextView date = (TextView) view.findViewById(R.id.conversation_fragment_list_item_date);
        date.setText(item.getDate());


        return view;
    }

    @Override
    public String toString() {
        return FindingListAdapter.class.getSimpleName() + "@" + hashCode();
    }
}

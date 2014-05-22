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

public class FindingListAdapter extends CustomListAdapter<Tuple> {
    private Logger logger = Logger.getLogger(FindingListAdapter.class.getSimpleName());

    public FindingListAdapter(Context context) {
        super(context, R.layout.tuple_layout);
    }

    public FindingListAdapter(Context context, List<Tuple> objects) {
        super(context, R.layout.tuple_layout, objects);
    }

    @Override
    public View fillItem(int position, View view, ViewGroup parent, Tuple item) {
        View view1 = view.findViewById(R.id.tuple_before);
        View view2 = view.findViewById(R.id.tuple_exact);
        View view3 = view.findViewById(R.id.tuple_after);
        fillItemLayout(view1, item.msgBefore);
        fillItemLayout(view2, item.msgExact);
        fillItemLayout(view3, item.msgAfter);
        return view;
    }

    private void fillItemLayout(View view, SimpleMessage item) {
        TextView bodyView = (TextView) view.findViewById(R.id.conversation_fragment_list_item_body);
        bodyView.setText(item.getBody());

        TextView author = (TextView) view.findViewById(R.id.conversation_fragment_list_item_author);
        author.setText(item.getFrom());

        TextView date = (TextView) view.findViewById(R.id.conversation_fragment_list_item_date);
        date.setText(item.getDate());
    }

    @Override
    public String toString() {
        return FindingListAdapter.class.getSimpleName() + "@" + hashCode();
    }
}

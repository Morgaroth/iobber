package pl.edu.agh.iobber.android.navigation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.logging.Logger;

import pl.edu.agh.iobber.R;
import pl.edu.agh.iobber.core.Conversation;
import pl.edu.agh.iobber.core.Msg;
import pl.morgaroth.utils.CustomListAdapter;

public class NavigationListAdapter extends CustomListAdapter<Conversation> {
    private Logger logger = Logger.getLogger(NavigationListAdapter.class.getSimpleName());

    public NavigationListAdapter(Context context) {
        super(context, R.layout.navigationdrawer_fragment_list_item_layout);
    }

    public NavigationListAdapter(Context context, List<Conversation> objects) {
        super(context, R.layout.navigationdrawer_fragment_list_item_layout, objects);
    }

    @Override
    public View fillItem(int position, View convertView, ViewGroup parent, Conversation item) {

        TextView header = (TextView) convertView.findViewById(R.id.navigationdrawer_fragment_list_item_header);
        TextView more = (TextView) convertView.findViewById(R.id.navigationdrawer_fragment_list_item_more);
        TextView unreaded = (TextView) convertView.findViewById(R.id.navigationdrawer_fragment_list_item_unread);


        header.setText(item.getSimpleName());

        Msg lastMsg = item.getLastMessage();
        int characters = lastMsg.getBody().length() > 30 ? 30 : lastMsg.getBody().length();
        String startOfMessage = lastMsg.getBody().substring(0, characters - 1);
        more.setText(startOfMessage);

        unreaded.setText(String.valueOf(item.unreadedMessages()));

        logger.info("filled view = " + convertView);

        return convertView;
    }
}

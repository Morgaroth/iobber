package pl.edu.agh.iobber.android.navigation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.logging.Logger;

import pl.edu.agh.iobber.R;
import pl.edu.agh.iobber.core.Conversation;
import pl.morgaroth.utils.CustomListAdapter;

public class NavigationListAdapter extends CustomListAdapter<Conversation> {
    private Logger logger = Logger.getLogger(NavigationListAdapter.class.getSimpleName());

    public NavigationListAdapter(Context context) {
        super(context, R.layout.navigation_drawer_list_item);
    }

    public NavigationListAdapter(Context context, List<Conversation> objects) {
        super(context, R.layout.navigation_drawer_list_item, objects);
    }

    @Override
    public View fillItem(int position, View convertView, ViewGroup parent, Conversation item) {

        TextView header = (TextView) convertView.findViewById(R.id.navigation_drawer_item_header);
        TextView more = (TextView) convertView.findViewById(R.id.navigation_drawer_item_more);

        header.setText(item.getName());
        more.setText("dodatkowe info, np ostatnia wiadomość");

        logger.info("filled view = " + convertView);

        return convertView;
    }
}

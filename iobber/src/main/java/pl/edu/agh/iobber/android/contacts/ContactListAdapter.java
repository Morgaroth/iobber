package pl.edu.agh.iobber.android.contacts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pl.edu.agh.iobber.R;
import pl.edu.agh.iobber.core.Contact;
import pl.morgaroth.utils.CustomListAdapter;

public class ContactListAdapter extends CustomListAdapter<Contact> {
    public ContactListAdapter(Context context) {
        super(context, R.layout.contacts_fragment_list_item_layout);
    }

    public ContactListAdapter(Context context, List<Contact> objects) {
        super(context, R.layout.contacts_fragment_list_item_layout, objects);
    }

    @Override
    public View fillItem(int position, View view, ViewGroup parent, Contact item) {
        TextView header = (TextView) view.findViewById(R.id.contact_list_item_header);
        header.setText(item.getName());
        return view;
    }


    @Override
    public String toString() {
        return ContactListAdapter.class.getSimpleName() + "@" + hashCode();
    }
}

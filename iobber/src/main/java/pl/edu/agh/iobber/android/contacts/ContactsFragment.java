package pl.edu.agh.iobber.android.contacts;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import pl.edu.agh.iobber.core.Contact;

import static java.lang.String.format;

public class ContactsFragment extends ListFragment implements AdapterView.OnItemLongClickListener {
    private Logger logger = Logger.getLogger(ContactsFragment.class.getSimpleName());
    private InteractionListener mListener;
    private ContactListAdapter adapter;
    private List<Contact> contactsList = new ArrayList<Contact>();
    private Activity parentActivity;

    public ContactsFragment() {
    }

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnItemLongClickListener(this);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (InteractionListener) activity;
            this.parentActivity = activity;
            logger.info(format("activity %s attached to contactsFragment", activity));
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (null != mListener) {
            mListener.onContactClicked(adapter.getItem(position));
        }
    }


    public void setContactsList(List<Contact> contactsList) {
        this.contactsList = contactsList;
        updateListAdapter();
    }

    private void updateListAdapter() {
        if (adapter == null) {
            logger.info("initializing list adapter with activity " + parentActivity);
            adapter = new ContactListAdapter(parentActivity);
            setListAdapter(adapter);
        }
        adapter.updateContent(contactsList);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mListener != null) {
            mListener.onContactLongClick((Contact) adapterView.getItemAtPosition(i));
        }
        return true;
    }

    public interface InteractionListener {
        public void onContactClicked(Contact item);

        public void onContactLongClick(Contact item);
    }
}
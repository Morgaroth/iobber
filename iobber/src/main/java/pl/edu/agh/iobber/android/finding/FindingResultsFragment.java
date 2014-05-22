package pl.edu.agh.iobber.android.finding;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import pl.edu.agh.iobber.R;
import pl.edu.agh.iobber.core.SimpleMessage;

public class FindingResultsFragment extends ListFragment {

    private Logger logger = Logger.getLogger(FindingResultsFragment.class.getSimpleName());
    private List<SimpleMessage> messages = new LinkedList<SimpleMessage>();
    private FindingListAdapter adapter;

    public FindingResultsFragment() {
    }

    public void setUp(List<SimpleMessage> messages) {
        this.messages = messages;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.finding_results_fragment_layout, container, false);
        if (messages != null) {
            setListAdapter(getOrCreateListAdapter());
        }
        return rootView;
    }

    private FindingListAdapter getOrCreateListAdapter() {
        if (adapter == null) {
            adapter = new FindingListAdapter(getActivity(), messages);
        }
        return adapter;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        ((MainActivity) activity).onSectionAttached(
//                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}

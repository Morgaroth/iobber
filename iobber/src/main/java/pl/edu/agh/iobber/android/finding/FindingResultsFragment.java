package pl.edu.agh.iobber.android.finding;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import pl.edu.agh.iobber.R;
import pl.edu.agh.iobber.core.SimpleMessage;

import static java.lang.String.format;

public class FindingResultsFragment extends ListFragment {

    private Logger logger = Logger.getLogger(FindingResultsFragment.class.getSimpleName());
    private List<Tuple> messages = new LinkedList<Tuple>();
    private String author;

    public FindingResultsFragment() {
    }

    public void setUp(List<SimpleMessage> messages, String author) {
        this.author = author;
        if (messages.size() % 3 != 0) {
            throw new RuntimeException("nie trójkami");
        }
        this.messages.clear();
        int count = messages.size() / 3;
        for (int i = 0; i < count; ++i) {
            this.messages.add(new Tuple(messages.get(i), messages.get(i + 1), messages.get(i + 2)));
        }
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
        return new FindingListAdapter(getActivity(), messages);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        logger.info(format("clicked ListView %s, View %s, position %d, id %d", l, v, position, id));
        ((OnResultLister) getActivity()).onFoundMessageSelected(messages.get(position).msgExact, author);
    }

    public interface OnResultLister {
        void onFoundMessageSelected(SimpleMessage msg, String author);
    }
}

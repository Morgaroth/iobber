package pl.edu.agh.iobber.android.utils;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.String.format;


public class LazyListAdapter<T> extends ArrayAdapter<T> {

    private Logger logger = Logger.getLogger(LazyListAdapter.class.getSimpleName());

    public LazyListAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
    }

    public LazyListAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public LazyListAdapter(Context context, int resource) {
        super(context, resource, new ArrayList<T>());

    }

    public LazyListAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId, new ArrayList<T>());
    }

    public void updateContent(List<T> items) {
        if (items != null) {
            clear();
            setNotifyOnChange(false);
            for (T item : items) {
                add(item);
            }
            logger.info(format("%s updated", this));
            notifyDataSetChanged();
        }
    }
}

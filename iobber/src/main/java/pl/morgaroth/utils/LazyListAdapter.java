package pl.morgaroth.utils;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;


public class LazyListAdapter<T> extends ArrayAdapter<T> {

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
            for (T item : items) {
                add(item);
            }
            notifyDataSetChanged();
        }
    }
}

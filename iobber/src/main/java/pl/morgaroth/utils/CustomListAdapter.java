package pl.morgaroth.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class CustomListAdapter<T> extends LazyListAdapter<T> {
    private int itemResourceID;

    public CustomListAdapter(Context context, int itemLayoutId) {
        super(context, android.R.layout.simple_list_item_1);
        itemResourceID = itemLayoutId;
    }

    public CustomListAdapter(Context context, int itemLayoutId, List<T> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        itemResourceID = itemLayoutId;
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(itemResourceID, null);
        }
        return fillItem(position, v, parent, getItem(position));
    }

    public abstract View fillItem(int position, View view, ViewGroup parent, T item);
}

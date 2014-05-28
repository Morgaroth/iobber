package pl.edu.agh.iobber.android.conversation;

import android.content.Context;
import android.widget.ListAdapter;

public class EndlessAdapter extends com.commonsware.cwac.endless.EndlessAdapter {
    public EndlessAdapter(ListAdapter wrapped) {
        super(wrapped);
    }

    public EndlessAdapter(ListAdapter wrapped, boolean keepOnAppending) {
        super(wrapped, keepOnAppending);
    }

    public EndlessAdapter(Context context, ListAdapter wrapped, int pendingResource) {
        super(context, wrapped, pendingResource);
    }

    public EndlessAdapter(Context context, ListAdapter wrapped, int pendingResource, boolean keepOnAppending) {
        super(context, wrapped, pendingResource, keepOnAppending);
    }

    @Override
    protected boolean cacheInBackground() throws Exception {
        return false;
    }

    @Override
    protected void appendCachedData() {

    }
}

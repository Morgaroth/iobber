/***
 Copyright (c) 2008-2009 CommonsWare, LLC
 Portions (c) 2009 Google, Inc.

 Licensed under the Apache License, Version 2.0 (the "License"); you may
 not use this file except in compliance with the License. You may obtain
 a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.commonsware.cwac.endless;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.commonsware.cwac.adapter.AdapterWrapper;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * Adapter that assists another adapter in appearing
 * endless. For example, this could be used for an adapter
 * being filled by a set of Web service calls, where each
 * call returns a "page" of data.
 * <p/>
 * Subclasses need to be able to return, via
 * getPendingView() a row that can serve as both a
 * placeholder while more data is being appended.
 * <p/>
 * The actual logic for loading new data should be done in
 * appendInBackground(). This method, as the name suggests,
 * is run in a background thread. It should return true if
 * there might be more data, false otherwise.
 * <p/>
 * If your situation is such that you will not know if there
 * is more data until you do some work (e.g., make another
 * Web service call), it is up to you to do something useful
 * with that row returned by getPendingView() to let the
 * user know you are out of data, plus return false from
 * that final call to appendInBackground().
 */
abstract public class EndslessAdapter extends AdapterWrapper {

    private static Logger logger = Logger.getLogger(EndslessAdapter.class.getSimpleName());
    private View pendingView = null;
    private AtomicBoolean keepOnAppendingAtStart = new AtomicBoolean(true);
    private AtomicBoolean keepOnAppendingAtEnd = new AtomicBoolean(true);
    private Context context;
    private int pendingResource = -1;
    private boolean isSerialized = false;
    private boolean runInBackground = true;

    /**
     * Constructor wrapping a supplied ListAdapter
     */
    public EndslessAdapter(ListAdapter wrapped) {
        super(wrapped);
    }

    /**
     * Constructor wrapping a supplied ListAdapter and
     * explicitly set if there is more data that needs to be
     * fetched or not.
     *
     * @param wrapped
     */
    public EndslessAdapter(ListAdapter wrapped, boolean keepOnAppendingAtStart, boolean keepOnAppendingAtEnd) {
        super(wrapped);
        this.setKeepOnAppendingAtEnd(keepOnAppendingAtEnd);
        this.setKeepOnAppendingAtStart(keepOnAppendingAtStart);
    }

    /**
     * Constructor wrapping a supplied ListAdapter and
     * providing a id for a pending view.
     *
     * @param context
     * @param wrapped
     * @param pendingResource
     */
    public EndslessAdapter(Context context, ListAdapter wrapped,
                           int pendingResource) {
        super(wrapped);
        this.context = context;
        this.pendingResource = pendingResource;
    }

    /**
     * Constructor wrapping a supplied ListAdapter, providing
     * a id for a pending view and explicitly set if there is
     * more data that needs to be fetched or not.
     *
     * @param context
     * @param wrapped
     * @param pendingResource
     */
    public EndslessAdapter(Context context, ListAdapter wrapped,
                           int pendingResource, boolean keepOnAppendingAtStart, boolean keepOnAppendingAtEnd) {
        super(wrapped);
        this.context = context;
        this.pendingResource = pendingResource;
        this.setKeepOnAppendingAtEnd(keepOnAppendingAtEnd);
        this.setKeepOnAppendingAtStart(keepOnAppendingAtStart);

    }

    abstract protected boolean cacheStartInBackground() throws Exception;

    abstract protected boolean cacheEndInBackground() throws Exception;

    abstract protected int appendCachedDataAtStart();

    abstract protected void onPostAppend(Direction d, int itemsAppended);

    abstract protected int appendCachedDataAtEnd();

    public boolean isSerialized() {
        return (isSerialized);
    }

    public void setSerialized(boolean isSerialized) {
        this.isSerialized = isSerialized;
    }

    public void stopAppendingAtEnd() {
        setKeepOnAppendingAtEnd(false);
    }

    public void stopAppendingAtStart() {
        setKeepOnAppendingAtStart(false);
    }

    public void restartAppendingAtStart() {
        setKeepOnAppendingAtStart(true);
    }

    public void restartAppendingAtEnd() {
        setKeepOnAppendingAtEnd(true);
    }

    /**
     * When set to false, cacheInBackground is called
     * directly, rather than from an AsyncTask.
     * <p/>
     * This is useful if for example you have code to populate
     * the adapter that already runs in a background thread,
     * and simply don't need the built in background
     * functionality.
     * <p/>
     * When using this you must remember to call onDataReady()
     * once you've appended your data.
     * <p/>
     * Default value is true.
     *
     * @param runInBackground :see #cacheInBackground() :see #onDataReady()
     */
    public void setRunInBackground(boolean runInBackground) {
        this.runInBackground = runInBackground;
    }

    /**
     * Use to manually notify the adapter that it's dataset
     * has changed. Will remove the pendingView and update the
     * display.
     */
    public void onDataReady() {
        pendingView = null;
        notifyDataSetChanged();
    }

    /**
     * How many items are in the data set represented by this
     * Adapter.
     */
    @Override
    public int getCount() {
        int more = 0;
        if (keepOnAppendingAtEnd.get()) {
            ++more;
        }
        if (keepOnAppendingAtStart.get()) {
            ++more;
        }
        return super.getCount() + more;
    }

    /**
     * Masks ViewType so the AdapterView replaces the
     * "Pending" row when new data is loaded.
     */
    public int getItemViewType(int position) {
        logger.info("endsless adapter -> returning view for position " + position);
        if (keepOnAppendingAtStart.get() && position == 0) {
            return IGNORE_ITEM_VIEW_TYPE;
        }
        if (keepOnAppendingAtEnd.get()) {
            if (position == (getWrappedAdapter().getCount() + (keepOnAppendingAtStart.get() ? 1 : 0))) {
                return IGNORE_ITEM_VIEW_TYPE;
            }
        }
        return super.getItemViewType(position);
    }

    /**
     * Masks ViewType so the AdapterView replaces the
     * "Pending" row when new data is loaded.
     *
     * @see #getItemViewType(int)
     */
    public int getViewTypeCount() {
        logger.info("getViewTypeCount");
        // TODO ?-----------------------------------------------------------
        // TODO ?-----------------------------------------------------------
        return (super.getViewTypeCount() + 1);
    }

    @Override
    public Object getItem(int position) {
//        logger.info("getItem at position " + position);
        if (position >= super.getCount()) {
            return (null);
        }

        return (super.getItem(position));
    }

    @Override
    public boolean areAllItemsEnabled() {
        return (false);
    }

    @Override
    public boolean isEnabled(int position) {
        if (position >= super.getCount()) {
            return (false);
        }

        return (super.isEnabled(position));
    }

    /**
     * Get a View that displays the data at the specified
     * position in the data set. In this case, if we are at
     * the end of the list and we are still in append mode, we
     * ask for a pending view and return it, plus kick off the
     * background task to append more data to the wrapped
     * adapter.
     *
     * @param position    Position of the item whose data we want
     * @param convertView View to recycle, if not null
     * @param parent      ViewGroup containing the returned View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0 && keepOnAppendingAtStart.get()) {
            logger.info("returning begining pending view");
            if (pendingView == null) {
                pendingView = getPendingView(parent);
                if (runInBackground) {
                    executeAsyncTask(buildTaskForStart());
                } else {
                    try {
                        setKeepOnAppendingAtStart(cacheStartInBackground());
                    } catch (Exception e) {
                        setKeepOnAppendingAtStart(onException(pendingView, e));
                    }
                }
            }

            return (pendingView);
        }

        if (position == (super.getCount() + (keepOnAppendingAtStart.get() ? 1 : 0)) && keepOnAppendingAtEnd.get()) {
            logger.info("returning ending pending view");
            if (pendingView == null) {
                pendingView = getPendingView(parent);

                if (runInBackground) {
                    executeAsyncTask(buildTaskForEnd());
                } else {
                    try {
                        setKeepOnAppendingAtEnd(cacheEndInBackground());
                    } catch (Exception e) {
                        setKeepOnAppendingAtEnd(onException(pendingView, e));
                    }
                }
            }

            return (pendingView);
        }

        if (keepOnAppendingAtStart.get()) {
            position = position - 1;
        }

        return (super.getView(position, convertView, parent));
    }

    /**
     * Called if cacheInBackground() raises a runtime
     * exception, to allow the UI to deal with the exception
     * on the main application thread.
     *
     * @param pendingView View representing the pending row
     * @param e           Exception that was raised by
     *                    cacheInBackground()
     * @return true if should allow retrying appending new
     * data, false otherwise
     */
    protected boolean onException(View pendingView, Exception e) {
        Log.e("EndlessAdapter", "Exception in cacheInBackground()", e);

        return (false);
    }

    protected AppendTask buildTaskForStart() {
        return (new AppendTask(this, true));
    }

    protected AppendTask buildTaskForEnd() {
        return (new AppendTask(this, false));
    }

    @TargetApi(11)
    private <T> void executeAsyncTask(AsyncTask<T, ?, ?> task,
                                      T... params) {
        if (!isSerialized
                && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }

    private void setKeepOnAppendingAtStart(boolean newValue) {
        boolean same = (newValue == keepOnAppendingAtStart.get());

        keepOnAppendingAtStart.set(newValue);

        if (!same) {
            notifyDataSetChanged();
        }
    }

    private void setKeepOnAppendingAtEnd(boolean newValue) {
        boolean same = (newValue == keepOnAppendingAtEnd.get());

        keepOnAppendingAtEnd.set(newValue);

        if (!same) {
            notifyDataSetChanged();
        }
    }

    /**
     * Inflates pending view using the pendingResource ID
     * passed into the constructor
     *
     * @param parent
     * @return inflated pending view, or null if the context
     * passed into the pending view constructor was
     * null.
     */
    protected View getPendingView(ViewGroup parent) {
        if (context != null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(pendingResource, parent, false);
        }

        throw new RuntimeException(
                "You must either override getPendingView() or supply a pending View resource via the constructor");
    }

    /**
     * Getter method for the Context being held by the adapter
     *
     * @return Context
     */
    protected Context getContext() {
        return (context);
    }

    public enum Direction {Start, End}

    /**
     * A background task that will be run when there is a need
     * to append more data. Mostly, this code delegates to the
     * subclass, to append the data in the background thread
     * and rebind the pending view once that is done.
     */
    protected static class AppendTask extends
            AsyncTask<Void, Void, Exception> {
        private final boolean isAtStart;
        EndslessAdapter adapter = null;
        boolean tempKeep;

        protected AppendTask(EndslessAdapter adapter, boolean isAtStart) {
            this.adapter = adapter;
            this.isAtStart = isAtStart;
        }

        @Override
        protected Exception doInBackground(Void... params) {
            try {
                tempKeep = isAtStart ? adapter.cacheStartInBackground() : adapter.cacheEndInBackground();
                return null;
            } catch (Exception e) {
                return e;
            }
        }

        @Override
        protected void onPostExecute(Exception e) {
            if (isAtStart) {
                adapter.setKeepOnAppendingAtStart(tempKeep);
            } else {
                adapter.setKeepOnAppendingAtEnd(tempKeep);
            }
            int position = 0;
            if (e == null) {
                if (isAtStart) {
                    position = adapter.appendCachedDataAtStart();
                } else {
                    position = adapter.appendCachedDataAtEnd();
                }
            } else {
                if (isAtStart) {
                    adapter.setKeepOnAppendingAtStart(adapter.onException(adapter.pendingView,
                            e));
                } else {
                    adapter.setKeepOnAppendingAtEnd(adapter.onException(adapter.pendingView,
                            e));
                }
            }
            adapter.onDataReady();
            adapter.onPostAppend(isAtStart ? Direction.Start : Direction.End, position);
        }
    }
}
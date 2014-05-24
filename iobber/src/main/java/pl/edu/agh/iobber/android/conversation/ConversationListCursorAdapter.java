package pl.edu.agh.iobber.android.conversation;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.logging.Logger;

import pl.edu.agh.iobber.R;

public class ConversationListCursorAdapter extends CursorAdapter {
    private static Logger logger = Logger.getLogger(ConversationListCursorAdapter.class.getSimpleName());
    private final Context context;

    public ConversationListCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View inflatedRow = inflater.inflate(R.layout.conversation_fragment_list_item_layout, viewGroup);
        return inflatedRow;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int position = cursor.getPosition();
        logger.info("bindView at position " + position);

        context.getApplicationContext().getSharedPreferences()

        String body = null;
        String authorName = null;
        String msgDate = null;

        TextView bodyView = (TextView) view.findViewById(R.id.conversation_fragment_list_item_body);
        bodyView.setText(body);

        TextView author = (TextView) view.findViewById(R.id.conversation_fragment_list_item_author);
        author.setText(authorName);

        TextView date = (TextView) view.findViewById(R.id.conversation_fragment_list_item_date);
        date.setText(msgDate);
    }
}

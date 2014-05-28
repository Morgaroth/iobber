package pl.edu.agh.iobber.android.conversation;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import java.util.Map;
import java.util.logging.Logger;

import pl.edu.agh.iobber.R;

public class ConversationItemFormatter {

    private static Logger logger = Logger.getLogger(ConversationItemFormatter.class.getSimpleName());


    public static <T extends View> T format(Context ctx, T view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);

        float author_font_size = Float.parseFloat(prefs.getString(ctx.getString(R.string.PREF_AUTHOR_FONT_SIZE), "15"));
        float date_font_size = Float.parseFloat(prefs.getString(ctx.getString(R.string.PREF_DATE_FONT_SIZE), "15"));
        float body_font_size = Float.parseFloat(prefs.getString(ctx.getString(R.string.PREF_BODY_FONT_SIZE), "22"));

        ((TextView) view.findViewById(R.id.conversation_fragment_list_item_date)).setTextSize(date_font_size);
        ((TextView) view.findViewById(R.id.conversation_fragment_list_item_author)).setTextSize(author_font_size);
        ((TextView) view.findViewById(R.id.conversation_fragment_list_item_body)).setTextSize(body_font_size);

        return view;
    }
}

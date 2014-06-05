package pl.edu.agh.iobber.android.finding;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import pl.edu.agh.iobber.R;
import pl.edu.agh.iobber.android.DateTimeDialog;
import pl.edu.agh.iobber.core.Contact;
import pl.edu.agh.iobber.core.SimpleMessage;
import pl.edu.agh.iobber.core.XMPPManager;
import pl.edu.agh.iobber.core.exceptions.CannotFindMessagesInTheDatabaseException;

import static java.lang.String.format;

public class FindingFragment extends Fragment implements View.OnClickListener {
    private static Logger logger = Logger.getLogger(FindingFragment.class.getSimpleName());
    Calendar fromCalendar, toCalendar;
    private Button fromButton;
    private Button toButton;
    private EditText phaseEdit;
    private EditText authorEdit;

    private String authorData = null;

    public FindingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflated = inflater.inflate(R.layout.finding_fragment_layout, container, false);
        fromButton = (Button) inflated.findViewById(R.id.finding_fragment_from_edit);
        toButton = (Button) inflated.findViewById(R.id.finding_fragment_to_edit);
        fromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new DateTimeDialog(getActivity())
                        .setOnClickListener(new DateTimeDialog.OnClickListener() {
                            @Override
                            public void onClick(Calendar calendar) {
                                ((Button) view).setText(new SimpleDateFormat().format(calendar.getTime()));
                                FindingFragment.this.fromCalendar = calendar;
                            }
                        }).setDate(FindingFragment.this.fromCalendar)
                        .show();
            }
        });
        toButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new DateTimeDialog(getActivity())
                        .setOnClickListener(new DateTimeDialog.OnClickListener() {
                            @Override
                            public void onClick(Calendar calendar) {
                                ((Button) view).setText(new SimpleDateFormat().format(calendar.getTime()));
                                FindingFragment.this.toCalendar = calendar;
                            }
                        })
                        .setDate(FindingFragment.this.toCalendar)
                        .show();
            }
        });
        Button findButton = (Button) inflated.findViewById(R.id.finding_fragment_find_button);
        findButton.setOnClickListener(this);
        phaseEdit = (EditText) inflated.findViewById(R.id.finding_fragment_layout_phase_edit);
        authorEdit = (EditText) inflated.findViewById(R.id.finding_fragment_layout_author_edit);
        if (authorData != null) {
            authorEdit.setText(authorData);
        }

        return inflated;
    }


    @Override
    public void onClick(View view) {
        Editable authorEditable = authorEdit.getText();
        Editable phraseEditable = phaseEdit.getText();
        String dateFrom = null;
        if (fromCalendar != null) {
            dateFrom = new SimpleDateFormat("dd-MM-yy hh:mm").format(fromCalendar.getTime());
        }
        String dateTo = null;
        if (toCalendar != null) {
            dateTo = new SimpleDateFormat("dd-MM-yy hh:mm").format(toCalendar.getTime());
        }
        String author = null;
        if (authorEditable != null) {
            author = authorEditable.toString().trim();
            if (author.equals("")) {
                author = null;
            }
        }
        String phrase = null;
        if (phraseEditable != null) {
            phrase = phraseEditable.toString();
        }
        try {
            logger.info(format("finding author %s, phrase %s, date from %s, date to %s", author, phrase, dateFrom, dateTo));
            List<SimpleMessage> messages = XMPPManager.instance.getBaseManager().findMessages(author, dateFrom, dateTo, phrase);
            logger.info("msgs: " + messages);
            ((OnResultListener) getActivity()).onResult(author, messages);
        } catch (CannotFindMessagesInTheDatabaseException e) {
            e.printStackTrace();
        }
    }

    public void setContact(Contact item) {
        authorData = item.getXMPPIdentifier();
    }

    public interface OnResultListener {
        void onResult(String author, List<SimpleMessage> messages);
    }
}

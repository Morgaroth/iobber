package pl.edu.agh.iobber.android;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.logging.Logger;

import pl.edu.agh.iobber.R;

import static android.view.View.OnClickListener;

public class DateTimeDialog extends Dialog implements OnClickListener {
    private static Logger logger = Logger.getLogger(DateTimeDialog.class.getSimpleName());
    private Calendar dateToShow;
    private OnClickListener listener;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button okButton;

    public DateTimeDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_time_dialog_layout);
        datePicker = (DatePicker) findViewById(R.id.date_time_dialog_layout_date);
        timePicker = (TimePicker) findViewById(R.id.date_time_dialog_layout_time);
        okButton = (Button) findViewById(R.id.date_time_dialog_layout_OK);
        timePicker.setIs24HourView(true);
        if (dateToShow != null) {
            datePicker.updateDate(dateToShow.get(Calendar.YEAR), dateToShow.get(Calendar.MONTH), dateToShow.get(Calendar.DAY_OF_MONTH));
            timePicker.setCurrentHour(dateToShow.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(dateToShow.get(Calendar.MINUTE));
        }
        datePicker.setOnClickListener(this);
        timePicker.setOnClickListener(this);
        okButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date_time_dialog_layout_OK:
                if (listener != null) {
                    listener.onClick(getTimeFromDialogs());
                }
        }
        logger.severe(view + " clicked");
        dismiss();
    }

    private Calendar getTimeFromDialogs() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
        return calendar;
    }

    public DateTimeDialog setDate(Calendar date) {
        dateToShow = date;
        return this;
    }

    public DateTimeDialog setOnClickListener(OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnClickListener {
        public void onClick(Calendar calendar);
    }

}

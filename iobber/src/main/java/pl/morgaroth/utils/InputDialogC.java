package pl.morgaroth.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;

public class InputDialogC {

    public static AlertDialog.Builder InputDialog(Context context, int dialogTitle, OnClickListener onPositiveButton, OnClickListener onNegativeButton) {
        return InputDialog(context, dialogTitle, android.R.string.yes, onPositiveButton, android.R.string.no, onNegativeButton);
    }

    public static AlertDialog.Builder InputDialog(Context context, int dialogTitle, OnClickListener onPositiveButton) {
        return InputDialog(context, dialogTitle, android.R.string.yes, onPositiveButton, android.R.string.no, null);
    }

    public static AlertDialog.Builder InputDialog(Context context, String dialogTitle, OnClickListener onPositiveButton, OnClickListener onNegativeButton) {
        return InputDialog(context, dialogTitle, android.R.string.yes, onPositiveButton, android.R.string.no, onNegativeButton);
    }

    public static AlertDialog.Builder InputDialog(Context context, String dialogTitle, OnClickListener onPositiveButton) {
        return InputDialog(context, dialogTitle, android.R.string.yes, onPositiveButton, android.R.string.no, null);
    }

    public static AlertDialog.Builder InputDialog(Context context, int dialogTitle, int yesText, final OnClickListener onPositiveButton, int noText, final OnClickListener onNegativeButton) {
        final EditText input = new EditText(context);
        setupInputView(input);
        AlertDialog.Builder result = new AlertDialog.Builder(context)
                .setView(input)
                .setTitle(dialogTitle);
        if (onPositiveButton != null) {
            result.setPositiveButton(yesText, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onPositiveButton.onClick(dialogInterface, i, input);
                }
            });
        }
        if (onNegativeButton != null) {
            result.setNegativeButton(noText, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onNegativeButton.onClick(dialogInterface, i, input);
                }
            });
        }
        result.setIcon(android.R.drawable.ic_dialog_alert);
        return result;
    }

    public static AlertDialog.Builder InputDialog(Context context, String dialogTitle, int yesText, final OnClickListener onPositiveButton, int noText, final OnClickListener onNegativeButton) {
        final EditText input = new EditText(context);
        setupInputView(input);
        AlertDialog.Builder result = new AlertDialog.Builder(context);
        result.setView(input)
                .setTitle(dialogTitle);
        if (onPositiveButton != null) {
            result.setPositiveButton(yesText, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onPositiveButton.onClick(dialogInterface, i, input);
                }
            });
        }
        if (onNegativeButton != null) {
            result.setNegativeButton(noText, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onNegativeButton.onClick(dialogInterface, i, input);
                }
            });
        }
        result.setIcon(android.R.drawable.ic_dialog_alert);
        return result;
    }

    private static void setupInputView(EditText input) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
    }

    public static AlertDialog.Builder InputDialog(Context context, String dialogTitle, String yesText, final OnClickListener onPositiveButton, String noText, final OnClickListener onNegativeButton) {
        final EditText input = new EditText(context);
        setupInputView(input);
        AlertDialog.Builder result = new AlertDialog.Builder(context)
                .setView(input)
                .setTitle(dialogTitle);
        if (onPositiveButton != null) {
            result.setPositiveButton(yesText, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onPositiveButton.onClick(dialogInterface, i, input);
                }
            });
        }
        if (onNegativeButton != null) {
            result.setNegativeButton(noText, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onNegativeButton.onClick(dialogInterface, i, input);
                }
            });
        }
        result.setIcon(android.R.drawable.ic_dialog_alert);
        return result;
    }


    public interface OnClickListener {
        void onClick(DialogInterface dialogInterface, int i, EditText input);
    }

}

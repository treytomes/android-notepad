package com.treytomes.notepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by GeorgeTomes on 3/8/2016.
 *
 * Helper methods for creating dialogs.
 */
public class Dialogs {

    public static void showInputBox(Activity activity, String labelText, String inputHint, final IInputTextListener inputTextListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.input_text);

        // Inflate the layout into a view.
        View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_input_text, null);

        TextView label = (TextView)dialogView.findViewById(R.id.label);
        label.setText(labelText);

        final EditText input = (EditText)dialogView.findViewById(R.id.input);
        input.setHint(inputHint);

        // Inflate and set the layout for the dialog.
        // Pass null as the parent view because its going in the dialog layout.
        builder.setView(dialogView);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String inputText = input.getText().toString().trim();
                if (inputText.length() > 0) {
                    inputTextListener.onInputTextReceived(input.getText().toString());
                }
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // Don't do anything.
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showMessageBox(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Menu Item");
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

package com.example.randomizer.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class DosagePickerDialog extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        final EditText dose = new EditText(getActivity());
        final EditText measurement = new EditText(getActivity());
        dose.setHint("number");
        measurement.setHint("g/mg");
        dose.setInputType(InputType.TYPE_CLASS_NUMBER);
        dose.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Dosage");
        builder.setMessage("Set # mg/ui/g");

        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

}
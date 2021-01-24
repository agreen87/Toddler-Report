package com.example.toddlerreport;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class NewStudentDialog extends AppCompatDialogFragment {

    private EditText studentName;
    private EditText phoneOne;
    private EditText phoneTwo;
    private NewStudentDialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("                Add Student")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Add", null);
        studentName = view.findViewById(R.id.editTextName);
        phoneOne = view.findViewById(R.id.editTextPhoneOne);
        phoneTwo = view.findViewById(R.id.editTextPhoneTwo);


        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                alertDialog.getButton((AlertDialog.BUTTON_POSITIVE)).setTextColor(Color.BLACK);
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);

                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name = studentName.getText().toString();
                        String firstPhone = phoneOne.getText().toString();
                        String secondPhone = phoneTwo.getText().toString();

                        if (name.isEmpty() || firstPhone.isEmpty() || secondPhone.isEmpty()) {
                            Toast.makeText(getContext(),
                                    "Nothing to save, one or more fields have been left empty.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            if (firstPhone.length() < 10 || secondPhone.length() < 10) {

                                Toast.makeText(getContext(),
                                        "Phone numbers must have 10 digits.",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                listener.applyTexts(name, firstPhone, secondPhone);

                                dismiss();
                            }
                        }
                    }
                });
            }
        });

        return alertDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (NewStudentDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException((context.toString() +
                    "Must implement NewStudentDialogListener"));
        }
    }

    public interface NewStudentDialogListener {
        void applyTexts(String name, String phoneOne, String phoneTwo);
    }
}
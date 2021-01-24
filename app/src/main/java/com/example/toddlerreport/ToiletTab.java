package com.example.toddlerreport;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToiletTab extends Fragment {

    private EditText editTextComments;
    private RadioGroup radioGroupToilet;
    private RadioButton radioButtonToilet;
    private CheckBox checkBoxAccident;
    private CheckBox checkBoxTried;
    private TextView viewMessageTextView;

    private String type;
    private String accident;
    private String tried;
    private final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;


    String name;

    public ToiletTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_toilet_tab, container, false);

        TextView nameDisplay = view.findViewById(R.id.nameTextView);
        radioGroupToilet = view.findViewById(R.id.radioGroup);
        checkBoxAccident = view.findViewById(R.id.checkboxAccident);
        checkBoxTried = view.findViewById(R.id.checkboxTried);
        viewMessageTextView = view.findViewById(R.id.viewMessageTextView);
        editTextComments = view.findViewById(R.id.commentEditText);

        Bundle bundle = getArguments();
        String studentName = bundle.getString("name");

        nameDisplay.setText("Report for: " + studentName);

        Button previewMessageButton = view.findViewById(R.id.viewMessageButton);
        previewMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int radioId = radioGroupToilet.getCheckedRadioButtonId();
                radioButtonToilet = v.findViewById(radioId);

                if (radioId == R.id.urineRadio) {
                    type = "Urine";
                } else if (radioId == R.id.bowelMovementRadio) {
                    type = "Bowel Movement";
                } else if (radioId == R.id.bothRadio) {
                    type = "Both";
                } else if (radioId == R.id.dryRadio) {
                    type = "Dry";
                }

                if (checkBoxAccident.isChecked()) {
                    accident = "Accident";
                }

                if (checkBoxTried.isChecked()) {
                    tried = "Tried";
                }

                if (radioId == -1) {
                    Toast.makeText(getContext(), "You need to make a selection!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (editTextComments.length() == 0) {
                        if (checkBoxAccident.isChecked() && checkBoxTried.isChecked()) {
                            viewMessageTextView.setText("Toilet Report" + "\nType: " + type +
                                    "\n" + accident + "\n" + tried);
                        } else if (checkBoxAccident.isChecked()) {
                            viewMessageTextView.setText("Toilet Report" + "\nType: " + type +
                                    "\n" + accident);
                        } else if (checkBoxTried.isChecked()) {
                            viewMessageTextView.setText("Toilet Report" + "\nType: " + type +
                                    "\n" + tried);
                        } else {
                            viewMessageTextView.setText("Toilet Report" + "\nType: " + type);
                        }

                    } else {
                        if (checkBoxAccident.isChecked() && checkBoxTried.isChecked()) {
                            viewMessageTextView.setText("Toilet Report" + "\nType: " + type + "\n"
                                    + accident + "\n" + tried + "\nComments: " +
                                    editTextComments.getText().toString());
                        } else if (checkBoxAccident.isChecked()) {
                            viewMessageTextView.setText("Toilet Report" + "\nType: " + type + "\n" +
                                    accident + "\nComments: " + editTextComments.getText().toString());
                        } else if (checkBoxTried.isChecked()) {
                            viewMessageTextView.setText("Toilet Report" + "\nType: " + type + "\n" +
                                    tried + "\nComments: " + editTextComments.getText().toString());
                        } else {
                            viewMessageTextView.setText("Toilet Report" + "\nType: " + type +
                                    "\nComments: " + editTextComments.getText().toString());
                        }
                    }
                }

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                if (null != getActivity().getCurrentFocus())
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                            .getApplicationWindowToken(), 0);

                radioGroupToilet.clearCheck();
                editTextComments.getText().clear();
                checkBoxAccident.setChecked(false);
                checkBoxTried.setChecked(false);
            }
        });

        Button sendMessageButton = view.findViewById(R.id.sendMessageButton);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission(Manifest.permission.SEND_SMS)) {
                    sendMessage();
                } else {
                    ActivityCompat.requestPermissions((Activity) getContext(),
                            new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
                }

                viewMessageTextView.setText("");
            }
        });

        return view;
    }

    private void sendMessage() {

        Bundle bundle = getArguments();
        String phoneOne = bundle.getString("phoneOne");
        String phoneTwo = bundle.getString("phoneTwo");

        String[] numbers = {phoneTwo, phoneOne};

        try {
            SmsManager smsManager = SmsManager.getDefault();
            for (String number : numbers) {
                smsManager.sendTextMessage(number, null,
                        viewMessageTextView.getText().toString(), null, null);
                Toast.makeText(getContext(), "Message sent", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to send message", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(getContext(), permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

}

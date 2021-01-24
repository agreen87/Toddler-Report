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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SleepTab extends Fragment {

    private RadioGroup radioGroupSleep;
    private RadioButton radioButtonSleep;
    private RadioButton radioButtonNap;
    private RadioButton radioButtonNoNap;
    private EditText durationEditText;
    private EditText commentsEditText;
    private TextView viewMessageTextView;

    private String nap;
    private String duration;
    private String comments;
    private final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    public SleepTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sleep_tab, container, false);

        TextView textViewName = view.findViewById(R.id.nameTextViewSleep);
        radioGroupSleep = view.findViewById(R.id.radioGroupSleep);
        durationEditText = view.findViewById(R.id.durationEditText);
        commentsEditText = view.findViewById(R.id.commentEditTextSleep);
        viewMessageTextView = view.findViewById(R.id.previewMessageTextViewSleep);
        radioButtonNap = view.findViewById(R.id.napRadio);
        radioButtonNoNap = view.findViewById(R.id.noNapRadio);

        Bundle bundle = getArguments();
        String studentName = bundle.getString("name");

        textViewName.setText("Report for: " + studentName);

        radioButtonNap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonNap.isChecked()) {
                    durationEditText.setVisibility(View.VISIBLE);
                }
            }
        });

        radioButtonNoNap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonNoNap.isChecked()) {
                    durationEditText.setVisibility(View.INVISIBLE);
                }
            }
        });


        Button previewButton = view.findViewById(R.id.previewMessageButtonSleep);
        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int radioId = radioGroupSleep.getCheckedRadioButtonId();
                radioButtonSleep = v.findViewById(radioId);

                if (radioId == R.id.napRadio) {
                    durationEditText.setVisibility(View.VISIBLE);

                } else if (radioId == R.id.noNapRadio) {
                    durationEditText.setVisibility(View.INVISIBLE);
                }

                if (radioId == R.id.napRadio) {
                    nap = "Yes";
                } else if (radioId == R.id.noNapRadio) {
                    nap = "No";
                }


                duration = durationEditText.getText().toString();
                comments = commentsEditText.getText().toString();

                if (durationEditText.getVisibility() == View.VISIBLE) {
                    if (radioId == -1 || durationEditText.length() == 0) {
                        Toast.makeText(getContext(), "Select a button or nap length..",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if (commentsEditText.length() == 0) {
                            viewMessageTextView.setText("Sleep Report" + "\nNap: " + nap +
                                    "\nDuration: " + duration);
                        } else {
                            viewMessageTextView.setText("Sleep Report" + "\nNap: " + nap +
                                    "\nDuration: " + duration + "\nComments: " + comments);
                        }
                        radioGroupSleep.clearCheck();
                        durationEditText.setText("");
                        commentsEditText.setText("");
                    }
                } else if (durationEditText.getVisibility() == View.INVISIBLE) {
                    if (commentsEditText.length() == 0) {
                        viewMessageTextView.setText("Sleep Report" + "\nNap: " + nap);
                    } else {
                        viewMessageTextView.setText("Sleep Report" + "\nNap: " + nap +
                                "\nComments: " + comments);
                    }
                    radioGroupSleep.clearCheck();
                    durationEditText.setText("");
                    commentsEditText.setText("");
                }

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                if (null != getActivity().getCurrentFocus())
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                            .getApplicationWindowToken(), 0);

            }
        });

        Button sendButton = view.findViewById(R.id.sendMessageSleep);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission(Manifest.permission.SEND_SMS)) {
                    sendMessage();
                } else {
                    ActivityCompat.requestPermissions((Activity) getContext(),
                            new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
                }
                Toast.makeText(getContext(), "Message Sent",
                        Toast.LENGTH_SHORT).show();
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

        System.out.println(phoneOne);

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

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
public class FoodTab extends Fragment {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private EditText commentsEditText;
    private TextView previewMessageTextView;

    private String appetite;
    private String phoneOne;
    private String phoneTwo;
    private final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;


    public FoodTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_tab, container, false);

        TextView nameDisplay = view.findViewById(R.id.nameTextViewFood);
        radioGroup = view.findViewById(R.id.radioGroupFood);
        commentsEditText = view.findViewById(R.id.commentEditTextFood);
        previewMessageTextView = view.findViewById(R.id.previewMessageTextViewFood);

        Bundle bundle = getArguments();
        String studentName = bundle.getString("name");
        phoneOne = bundle.getString("phoneOne");
        phoneTwo = bundle.getString("phoneTwo");

        nameDisplay.setText("Report for: " + studentName);

        Button previewButton = view.findViewById(R.id.previewMessageButtonFood);
        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = v.findViewById(radioId);

                if (radioId == R.id.low) {
                    appetite = "Low";
                } else if (radioId == R.id.moderate) {
                    appetite = "Moderate";
                } else if (radioId == R.id.great) {
                    appetite = "Great";
                }

                if (radioId == -1) {
                    Toast.makeText(getContext(), "You need to make a selection!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (commentsEditText.length() == 0) {
                        previewMessageTextView.setText("Food Report" + "\nAppetite: " + appetite);
                    } else {
                        previewMessageTextView.setText("Food Report" + "\nAppetite: " + appetite
                                + "\nComments: " + commentsEditText.getText().toString());
                    }
                }

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                if (null != getActivity().getCurrentFocus())
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                            .getApplicationWindowToken(), 0);

                radioGroup.clearCheck();
                commentsEditText.setText("");
            }
        });

        Button sendButton = view.findViewById(R.id.sendMessageButtonFood);
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

                previewMessageTextView.setText("");
            }
        });
        return view;
    }

    private void sendMessage() {

        //String numbers[] = {phoneOne, phoneTwo};
        Bundle bundle = getArguments();
        phoneOne = bundle.getString("phoneOne");
        phoneTwo = bundle.getString("phoneTwo");


        String[] numbers = {phoneTwo, phoneOne};

        System.out.println(phoneOne);


        // String number =;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            for (String number : numbers) {
                smsManager.sendTextMessage(number, null,
                        previewMessageTextView.getText().toString(), null, null);
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

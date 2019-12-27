package com.bcil.scanandopenpdf;
import android.app.Activity;
import android.graphics.Color;
import android.telephony.PhoneNumberUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    private static final String PHONE_REGEX = "^[789]\\d{9}$";

    private static final String PHONE_MSG = "invalid number";

    public static final String REQUIRED_MSG =  "cannot be empty";
    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);
        editText.setFocusable(true);
        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }


}

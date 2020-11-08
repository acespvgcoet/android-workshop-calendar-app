package com.example.testcalendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class CustomDatePicker extends DialogFragment {
    // This class shows a DatePicker Dialog Box when instantiated

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Get Java's built-in calendar instance
        Calendar calendar = Calendar.getInstance();

        // Get current day, month and year
        // This will be used to set as the default date in our DatePicker
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // Context is like a base on which the Dialog Box is created
        Context context = getActivity();

        // This is the listener that we implement in our activity
        // This is how the DatePicker knows which function to call, when the user has pressed Ok after selecting the date
        DatePickerDialog.OnDateSetListener onDateSetListener = (DatePickerDialog.OnDateSetListener) getActivity();

        // Instantiate a new DatePickerDialog with all the parameters above, and return it
        return new DatePickerDialog(context, onDateSetListener, year, month, dayOfMonth);
    }
}

package com.example.testcalendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class CustomTimePicker extends DialogFragment {

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Similar to CustomDatePicker, get Java's built-in Calendar
        // to know the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Context is like a base on which the Dialog Box is created
        Context context = getActivity();

        // This is the listener that we implement in our activity
        // This is how the DatePicker knows which function to call, when the user has pressed Ok after selecting the date
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (TimePickerDialog.OnTimeSetListener) getActivity();

        // Instantiate a new TimePickerDialog with all the parameters above, and return it
        return new TimePickerDialog(context, onTimeSetListener, hour, minute, false);
    }

}

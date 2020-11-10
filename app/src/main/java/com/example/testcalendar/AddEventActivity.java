package com.example.testcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final String TAG = "TAG";
    int day, month, year;
    int hour, minute;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Attaches the layout design file to this activity
        setContentView(R.layout.activity_add_event);

        // This name appears on the Action Bar (at the top) when this Activity is launched
        setTitle("New event");

        // Get the ImageButton and add a click listener
        // This lets us know when the button is clicked
        ImageButton showDatePicker = findViewById(R.id.show_datepicker_button);
        showDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch our DatePicker when the button is clicked
                CustomDatePicker customDatePicker = new CustomDatePicker();
                customDatePicker.show(getSupportFragmentManager(), "PICK DATE");
            }
        });

        // Similar to above
        ImageButton showTimePicker = findViewById(R.id.show_timepicker_button);
        showTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Time Picker
                CustomTimePicker customTimePicker = new CustomTimePicker();
                customTimePicker.show(getSupportFragmentManager(), "PICK TIME");
            }
        });

        Button saveButton = findViewById(R.id.new_event_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save our entered info in the local SQLite Database
                saveEventToFirebase();

                // Close this activity and return to the previous page
                finish();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // This is a callback function, as in this gets called when the date is set

        // Format the date in whatever pattern we want
        String date = (dayOfMonth) + "/" + (month + 1) + "/" + (year);

        // Get the dateView which is an EditText
        EditText dateView = findViewById(R.id.new_event_dateview);
        // And set our formatted date
        dateView.setText(date);

        // Also save the date to global variables so we can use it later
        this.day = dayOfMonth;
        this.month = month + 1;
        this.year = year;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // This is a callback function, similar to the one above

        // Format time as needed, and set this string to our timeView
        String time = (hourOfDay) + ":" + (minute);

        EditText timeView = findViewById(R.id.new_event_timeview);
        timeView.setText(time);

        // Also save the time to global variables so we can use it later
        this.hour = hourOfDay;
        this.minute = minute;
    }

    private void saveEventToFirebase() {
        // This function used the global variables day, month, year etc
        // And created an event entry in the database

        // Get the title and location of our event
        EditText eventTitleBox = findViewById(R.id.new_event_title_edittext);
        String eventTitle = eventTitleBox.getText().toString();

        EditText eventLoactionBox = findViewById(R.id.new_event_location_edittext);
        String eventLocation = eventLoactionBox.getText().toString();

        String Year = ""+this.year;
        String date = this.day+"-"+this.month;
        String time = this.hour+":"+this.minute;

        // Create a connection to our Firebase-Firestore Database
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("Events").document(Year).collection(date).document(time);


        // Insert all event info in a Hashmap
        Map<String,String> user = new HashMap<>();
        user.put("Event_title",eventTitle);
        user.put("Location",eventLocation);
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Event is created");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });

    }

}
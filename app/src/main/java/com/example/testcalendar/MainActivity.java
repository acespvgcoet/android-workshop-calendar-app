package com.example.testcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView.Adapter adapter;
    ArrayList<Event> eventsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Attaches the layout design file to this activity
        setContentView(R.layout.activity_main);

        // Get the FloatingActionButton we created in our UI
        FloatingActionButton addEventButton = findViewById(R.id.add_event_fab);

        // Add an onClickListener, so that we know when the button is clicked
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is the function that gets called when a user clicks the button

                // Create an Intent that contains information about which activity we want to launch
                Intent intent = new Intent(MainActivity.this, AddEventActivity.class);

                // Use the built-in startActivity() function and pass our intent to start the new activity
                startActivity(intent);
            }
        });

        // Create a RecyclerView, this lets us reuse the same design for multiple events
        RecyclerView eventsRecyclerView = findViewById(R.id.event_list_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        // Instantiate the EventsAdapter with an ArrayList of the events
        adapter = new EventsAdapter(eventsList);

        eventsRecyclerView.setAdapter(adapter);
        eventsRecyclerView.setLayoutManager(layoutManager);

        // Get our CalendarView and attach a DateChangeListener
        // So that we get to know when a user clicks on some date
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // This function will execute when the user selects a date

                // Fetch events from the database
                // This will update the ArrayList "eventsList"
                getEventsFromSQLiteDatabase(dayOfMonth, month + 1, year);

                // Tell the adapter that the ArrayList is updated
                // This will make it refresh the UI
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getEventsFromSQLiteDatabase(int day, int month, int year) {
        // Create a connection to our SQLite Database
        SQLiteDatabase database = openOrCreateDatabase("Main", MODE_PRIVATE,null);

        // Execute an SQL query to create the table if it does not exist
        String createTableQuery = "CREATE TABLE IF NOT EXISTS events(title VARCHAR, day INT, month INT, year INT, hour INT, minute INT);";
        database.execSQL(createTableQuery);

        // Execute a query to fetch all events and store them in a Cursor object
        String fetchDataQuery = "SELECT * from events WHERE day=" + day + " AND month=" + month + " AND year=" + year + ";";
        Cursor cursor = database.rawQuery(fetchDataQuery, null);
        // Make sure the cursor points to the first row
        cursor.moveToFirst();

        // Clear our ArrayList
        eventsList.clear();

        // Check if there are any rows present
        if (cursor.getCount() == 0) {
            return;
        }

        // Iterate over the rows
        do {
            Event event = new Event(cursor.getString(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5));

            // Add the event object to our dataset list
            eventsList.add(event);

        } while(cursor.moveToNext());

    }

}
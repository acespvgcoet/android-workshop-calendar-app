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
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    final String EVENT_NAME = "Event_title", EVENT_LOCATION = "Location";
    public static final String TAG = "TAG";

    RecyclerView.Adapter adapter;
    ArrayList<Event> eventsList = new ArrayList<>();

    Button logout;
    FloatingActionButton addEventButton;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Attaches the layout design file to this activity
        setContentView(R.layout.activity_main);

        // Get the FloatingActionButton we created in our UI
        addEventButton = findViewById(R.id.add_event_fab);

        // Get the Logout Button we created in our UI
        logout = findViewById(R.id.Logout_button);

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

                getEventsFromFirebaseDatabase(dayOfMonth, month + 1, year);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });
    }

    private void getEventsFromFirebaseDatabase(int day, int month, int year) {

        // Clear our ArrayList
        eventsList.clear();

        String Year = ""+year;
        String date = day+"-"+month;
        // Create a connection to our Firebase-Firestore Database
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Execute a query to fetch data
        firebaseFirestore.collection("Events").document(Year).collection(date)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String[] split = document.getId().split(":");
                        // Log.d(TAG, split[0] + " => " + split[1] + " => " + document.getData());

                        // Check if there are any rows present
                        if(document.getData().isEmpty())   {
                            return;
                        }

                        Event event = new Event(document.getData().get(EVENT_NAME).toString(),
                                document.getData().get(EVENT_LOCATION).toString(),
                                day,
                                month,
                                year,
                                parseInt(split[0]),
                                parseInt(split[1]));

                        eventsList.add(event);
                    }
                    // Tell the adapter that the ArrayList is updated
                    // This will make it refresh the UI
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });




    }

}
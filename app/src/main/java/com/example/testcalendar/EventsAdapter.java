package com.example.testcalendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {

    private ArrayList<Event> eventsList;

    public EventsAdapter(ArrayList eventsList) {
        this.eventsList = eventsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view from the layout file we created
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_event_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Get the event at index "position" from our dataset i.e. the ArrayList "eventsList"
        Event event = eventsList.get(position);

        // Set all event info in the UI layout
        holder.titleView.setText(event.getTitle() + "\t-\t" + event.getLocation());
        holder.dateView.setText(event.getDay() + "/" + event.getMonth() + "/" + event.getYear());

        String period = "AM";
        if(event.getHour() > 12) {
            event.hour = event.getHour() - 12;
            period = "PM";
        }

        holder.timeView.setText(event.getHour() + ":" + event.getMinute() + " " + period);
    }

    @Override
    public int getItemCount() {
        // Return size of our dataset i.e. the ArrayList "eventsList"
        return eventsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleView, dateView, timeView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Assign views from the UI layout we created
            titleView = itemView.findViewById(R.id.event_list_title);
            dateView = itemView.findViewById(R.id.event_list_date);
            timeView = itemView.findViewById(R.id.event_list_time);
        }
    }
}

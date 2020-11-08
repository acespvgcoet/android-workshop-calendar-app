package com.example.testcalendar;

public class Event {
    String title;
    int day, month, year;
    int hour;
    String minute;

    public Event(String title, int day, int month, int year, int hour, int minute) {
        this.title = title;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        if(minute < 10) {
            this.minute = "0" + minute;
        } else {
            this.minute = String.valueOf(minute);
        }
    }

    public String getTitle() {
        return title;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

}

package com.example.fyp;

public class Appointment {
    public String name, phoneNo, email, date, time;

    public Appointment(){
    }

    public Appointment(String name, String phoneNo, String email, String date, String time){
        this.name = name;
        this.phoneNo = phoneNo;
        this.email = email;
        this.date = date;
        this.time = time;
    }
}

package com.example.fyp;

public class User {
    public String username, email, gender, phoneNo, dadNo, momNo;

    public User(){
    }

    public User(String username, String email, String gender, String phoneNo, String dadNo, String momNo){
        this.email = email;
        this.username = username;
        this.gender = gender;
        this.phoneNo = phoneNo;
        this.dadNo = dadNo;
        this.momNo = momNo;
    }
}

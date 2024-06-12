package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Counselor extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    EditText editName, editPhoneNo, editEmail, editDate, editTime;
    Button btn;
    private DatePickerDialog picker;
    DatePickerDialog.OnDateSetListener dateSetListener;
    TimePickerDialog.OnTimeSetListener timeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselor);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        editName = findViewById(R.id.editTextNameCounselor);
        editPhoneNo= findViewById(R.id.editTextPhoneNoCouns);
        editEmail = findViewById(R.id.editTextTextEmailAddress);
        editDate = findViewById(R.id.editTextDate);
        editTime = findViewById(R.id.editTextTime);
        btn = findViewById(R.id.buttonSubmitCouns);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseAuth authCouns;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String phoneNo = editPhoneNo.getText().toString();
                String email = editEmail.getText().toString();
                String date = editDate.getText().toString();
                String time = editTime.getText().toString();

                if (name.length() == 0 || email.length() == 0 || phoneNo.length() == 0
                        || date.length() == 0 || time.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please fill in details", Toast.LENGTH_SHORT).show();
                    return;
                }

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String appointmentId = FirebaseDatabase.getInstance().getReference("Registered User")
                        .child(userId)
                        .child("Appointments")
                        .push()
                        .getKey();
                Appointment appointment = new Appointment(name, phoneNo, email, date, time);
                FirebaseDatabase.getInstance().getReference("Registered User")
                        .child(userId)
                        .child("Appointments")
                        .child(appointmentId)
                        .setValue(appointment).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            editName.setText("");
                            editPhoneNo.setText("");
                            editEmail.setText("");
                            editDate.setText("");
                            editTime.setText("");

                            Toast.makeText(getApplicationContext(), "Appointment booked", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "Failed to book appointment", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                picker = new DatePickerDialog(Counselor.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        editDate .setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int min = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(
                        Counselor.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, hour);
                        cal.set(Calendar.MINUTE, minute);
                        cal.setTimeZone(TimeZone.getDefault());
                        SimpleDateFormat format = new SimpleDateFormat("k:mm a");
                        String time = format.format(cal.getTime());
                        editTime.setText(time);
                    }
                }, hour, min, false);
                dialog.show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePass extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FirebaseAuth authProfile;
    EditText editCurrPass, editNewPass, editConfNewPass;
    Button btnAuthenticate, btnChangePass;
    String userPassCurr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editCurrPass = findViewById(R.id.editTextCurrPass);
        editNewPass = findViewById(R.id.editTextNewPass);
        editConfNewPass = findViewById(R.id.editTextNewConfPass);
        btnAuthenticate = findViewById(R.id.buttonAuthenticate);
        btnChangePass = findViewById(R.id.buttonChangePass);

        //disable enter new password until curr pass authenticated
        editNewPass.setEnabled(false);
        editConfNewPass.setEnabled(false);
        btnChangePass.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser.equals("")){
            Toast.makeText(ChangePass.this, "Password incorrect", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChangePass.this, ProfilePage.class);
            startActivity(intent);
            finish();
        }else {
            reAuthenticateUser(firebaseUser);
        }
    }

    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        btnAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPassCurr = editCurrPass.getText().toString();

                if (userPassCurr.length() == 0 ){
                    Toast.makeText(ChangePass.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                }else {
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), userPassCurr);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()){
                                editCurrPass.setEnabled(false);
                                editNewPass.setEnabled(true);
                                editConfNewPass.setEnabled(true);

                                btnAuthenticate.setEnabled(false);
                                btnChangePass.setEnabled(true);

                                Toast.makeText(ChangePass.this, "Password has been verified", Toast.LENGTH_SHORT).show();

                                btnChangePass.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changePass(firebaseUser);
                                    }
                                });
                            }else {
                                try {
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(ChangePass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void changePass(FirebaseUser firebaseUser) {
        String userNewPass = editNewPass.getText().toString();
        String userConfNewPass = editConfNewPass.getText().toString();

        if (userNewPass.length() == 0){
            Toast.makeText(ChangePass.this, "New password needed", Toast.LENGTH_SHORT).show();
        }else if (userConfNewPass.length() == 0){
            Toast.makeText(ChangePass.this, "Please confirm your new password", Toast.LENGTH_SHORT).show();
        }else if (!userNewPass.matches(userConfNewPass)){
            Toast.makeText(ChangePass.this, "Password did not match", Toast.LENGTH_SHORT).show();
        }else if (userPassCurr.matches(userNewPass)){
            Toast.makeText(ChangePass.this, "New password cannot same like old password", Toast.LENGTH_SHORT).show();
        }else {
            firebaseUser.updatePassword(userNewPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ChangePass.this, "Password has been change", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePass.this, ProfilePage.class);
                        startActivity(intent);
                        finish();
                    }else {
                        try {
                            throw task.getException();
                        }catch (Exception e){
                            Toast.makeText(ChangePass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
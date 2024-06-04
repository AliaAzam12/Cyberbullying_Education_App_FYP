package com.example.fyp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.window.SplashScreen;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    EditText editEmail, editPassword;
    Button btn;
    TextView textViewReg, textViewForgotPass;
    FirebaseAuth authLogin;
    private static final String TAG = "Login";
    public static final String Shared_pref = "sharedprefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authLogin = FirebaseAuth.getInstance();
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);

        editEmail = findViewById(R.id.editTextLoginEmail);
        editPassword = findViewById(R.id.editTextLoginPassword);
        btn = findViewById(R.id.buttonLogin);
        textViewReg = findViewById(R.id.textViewRegisterNow);
        textViewForgotPass = findViewById(R.id.textViewForgotPass);


        //show hide password
        ImageView imageShowHidePass = findViewById(R.id.imageViewShowHidePassLogin);
        imageShowHidePass.setImageResource(R.drawable.hide_icon);
        imageShowHidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageShowHidePass.setImageResource(R.drawable.hide_icon);
                }else {
                    editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageShowHidePass.setImageResource(R.drawable.show_icon);
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                if(email.length() == 0){
                    Toast.makeText(Login.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    editEmail.setError("Email is required");
                }else if(password.length() == 0){
                    Toast.makeText(Login.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    editEmail.setError("Password is required");
                }else {
                    loginUser(email, password);
                }
            }
        });

        textViewReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this, Register.class));
            }
        });

        textViewForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Reset your password", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, ForgotPass.class));
            }
        });

    }

    private void loginUser(String email, String password) {
        authLogin.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    SharedPreferences sharedPreferences = getSharedPreferences(Shared_pref, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name", "true");
                    editor.apply();

                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    //get instance of the curr user
                    FirebaseUser firebaseUser = authLogin.getCurrentUser();

                    //check if email are verified
                    if (firebaseUser.isEmailVerified()){
                        //Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    }else {
                        firebaseUser.sendEmailVerification();
                        authLogin.signOut();
                        showAlertDialog();
                    }

                    Intent intent = new Intent(Login.this, Homepage.class);
                    startActivity(intent);
                }else {
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        editEmail.setError("User does not existed.");
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        editEmail.setError("Invalid credentials.");
                    }catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder build = new AlertDialog.Builder(Login.this);
        build.setTitle("Email not verified");
        build.setMessage("Please verify your email");
        build.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = build.create();
        alertDialog.show();
    }

    //check if user already login
    /*@Override
    protected void onStart() {
        super.onStart();
        if (authLogin.getCurrentUser() != null){
            Toast.makeText(Login.this, "Already logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Homepage.class));
            finish();
            //userprofile
        }else {
            Toast.makeText(Login.this, "Log in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Login.class));
            finish();
        }
    }*/
}
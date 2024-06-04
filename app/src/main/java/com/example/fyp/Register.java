package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText editName, editUsername, editEmail, editPhoneNo, editDadNo, editMomNo, editPassword, editConfirmPass;
    RadioGroup radioGroupGender;
    RadioButton radioBtnGenderSelected;
    Button btn;
    TextView textView;
    private static final String TAG = "Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editName = findViewById(R.id.editTextRegFullName);
        editUsername = findViewById(R.id.editTextRegUsername);
        editEmail = findViewById(R.id.editTextRegEmail);
        editPhoneNo = findViewById(R.id.editTextRegPhoneNo);
        editDadNo = findViewById(R.id.editTextRegDadNo);
        editMomNo = findViewById(R.id.editTextRegMomNo);
        editPassword = findViewById(R.id.editTextRegPassword);
        editConfirmPass = findViewById(R.id.editTextRegConfirmPassword);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioGroupGender.clearCheck();
        btn = findViewById(R.id.buttonRegister);
        textView = findViewById(R.id.textViewExistingUser);

        ImageView imageShowHidePass = findViewById(R.id.imageViewShowHidePassReg);
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

        ImageView imageShowHideConfPass = findViewById(R.id.imageViewShowHideConfPassReg);
        imageShowHideConfPass.setImageResource(R.drawable.hide_icon);
        imageShowHideConfPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editConfirmPass.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    editConfirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageShowHideConfPass.setImageResource(R.drawable.hide_icon);
                }else {
                    editConfirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageShowHideConfPass.setImageResource(R.drawable.show_icon);
                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
                radioBtnGenderSelected = findViewById(selectedGenderId);

                String name = editName.getText().toString();
                String username = editUsername.getText().toString();
                String email = editEmail.getText().toString();
                String phoneNo = editPhoneNo.getText().toString();
                String dadNo = editDadNo.getText().toString();
                String momNo = editMomNo.getText().toString();
                String password = editPassword.getText().toString();
                String confirm = editConfirmPass.getText().toString();
                String gender;

                if(name.length() == 0 || username.length() == 0 || email.length() == 0 || radioGroupGender.getCheckedRadioButtonId() == -1
                        || phoneNo.length() == 0 || dadNo.length() == 0 || momNo.length() == 0 || password.length() == 0 || confirm.length() == 0){
                    Toast.makeText(Register.this, "Please fill in details", Toast.LENGTH_SHORT).show();
                }else if(password.length() < 8){
                    Toast.makeText(Register.this, "Password should at least 8 digits", Toast.LENGTH_SHORT).show();
                    //editPassword.setError("Password too weak");
                }else if(!password.equals(confirm)){
                    Toast.makeText(Register.this, "Please enter same password", Toast.LENGTH_SHORT).show();
                }else{
                    gender = radioBtnGenderSelected.getText().toString();
                    registerUser(name, username, email, gender, phoneNo, dadNo, momNo,password);
                }
            }
        });
    }

    private void registerUser(String name, String username, String email, String gender, String phoneNo, String dadNo, String momNo, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        //create user profile
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    //display name of user
                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                    firebaseUser.updateProfile(userProfileChangeRequest);

                    //enter user data into firebase
                    User user = new User(username, email, gender, phoneNo, dadNo, momNo);

                    //extracting user reference from database for registered user
                    DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference("Registered User");
                    dbreference.child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(Register.this, "Registered. Please verify your email.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(Register.this, "Registered failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        editPassword.setError("Your password is too weak. Enter passwords using alphabets, numbers, and special characters");
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        editPassword.setError("Your email is invalid");
                    }catch (FirebaseAuthUserCollisionException e){
                        editEmail.setError("Email already registered.");
                    }catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
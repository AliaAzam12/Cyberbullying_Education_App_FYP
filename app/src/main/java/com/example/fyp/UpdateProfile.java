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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfile extends AppCompatActivity {

    EditText editName, editUsername, editEmail, editPhoneNo, editDadNo, editMomNo;
    RadioGroup radioGroupUpdtGender;
    RadioButton radioBtnUpdtGenderSelected;
    Button btnUpdtProfile;
    String textName, textUsername, textEmail, textGender, textPhoneNo, textDadNo, textMomNo;
    FirebaseAuth authProfile;
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editName = findViewById(R.id.editTextUpdtFullName);
        editUsername = findViewById(R.id.editTextUpdtUsername);
        editEmail = findViewById(R.id.editTextUpdtEmail);
        editPhoneNo = findViewById(R.id.editTextUpdtPhoneNo);
        editDadNo = findViewById(R.id.editTextUpdtDadNo);
        editMomNo = findViewById(R.id.editTextUpdtMomNo);
        radioGroupUpdtGender = findViewById(R.id.radioGroupUpdtGender);
        btnUpdtProfile = findViewById(R.id.buttonSaveUpdtProfile);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        showProfile(firebaseUser);

        btnUpdtProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateProfile(firebaseUser);
            }
        });
    }

    private void updateProfile(FirebaseUser firebaseUser) {
        int selectedGenderID = radioGroupUpdtGender.getCheckedRadioButtonId();
        radioBtnUpdtGenderSelected = findViewById(selectedGenderID);

        if(textName.length() == 0 || textUsername.length() == 0 || textEmail.length() == 0 ||
                radioGroupUpdtGender.getCheckedRadioButtonId() == -1
                || textPhoneNo.length() == 0 || textDadNo.length() == 0 || textMomNo.length() == 0){
            Toast.makeText(UpdateProfile.this, "Please fill in details", Toast.LENGTH_SHORT).show();
        }else{
            textGender = radioBtnUpdtGenderSelected.getText().toString();
            textName = editName.getText().toString();
            textUsername = editUsername.getText().toString();
            textEmail = editEmail.getText().toString();
            textPhoneNo = editPhoneNo.getText().toString();
            textDadNo = editDadNo.getText().toString();
            textMomNo = editMomNo.getText().toString();

            User userDetails = new User(textUsername, textEmail,textGender, textPhoneNo, textDadNo, textMomNo);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered User");
            String userId = firebaseUser.getUid();
            reference.child(userId).setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()){
                        UserProfileChangeRequest profileUpdt = new UserProfileChangeRequest.Builder().
                                setDisplayName(textName).build();
                        firebaseUser.updateProfile(profileUpdt);
                        Toast.makeText(UpdateProfile.this, "Update successful", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(UpdateProfile.this, ProfilePage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {
                        try {
                            throw task.getException();
                        }catch (Exception e){
                            Toast.makeText(UpdateProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    private void showProfile(FirebaseUser firebaseUser) {
        String registeredId = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered User");
        referenceProfile.child(registeredId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null){
                    textName = firebaseUser.getDisplayName();
                    textUsername = user.username;
                    textEmail = firebaseUser.getEmail();
                    textGender = user.gender;
                    textPhoneNo = user.phoneNo;
                    textDadNo = user.dadNo;
                    textMomNo = user.momNo;

                    editName.setText(textName);
                    editUsername.setText(textUsername);
                    editEmail.setText(textEmail);
                    editPhoneNo.setText(textPhoneNo);
                    editDadNo.setText(textDadNo);
                    editMomNo.setText(textMomNo);

                    if (textGender.equals("Male")){
                        radioBtnUpdtGenderSelected = findViewById(R.id.RadioBtnUpdtMale);
                    }else {
                        radioBtnUpdtGenderSelected = findViewById(R.id.RadioBtnUpdtFemale);
                    }
                    radioBtnUpdtGenderSelected.setChecked(true);
                }else {
                    Toast.makeText(UpdateProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfilePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    TextView textwelcome, textname, textusername, textemail, textgender, textphoneNo, textDadNo, textMomNo, textChangePass;
    String name, username, email, gender, phoneNo, dadNo, momNo;
    ImageView imageView;
    FirebaseAuth authProfile;
    Button btnEditProfile;
    SwipeRefreshLayout swipePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.navEditProfile);
        
        swipetoRefresh();

        textwelcome = findViewById(R.id.textViewWelcome);
        textname = findViewById(R.id.editTextProfileName);
        textusername = findViewById(R.id.editTextProfileUsername);
        textemail = findViewById(R.id.editTextProfileEmail);
        textgender = findViewById(R.id.editTextProfileGender);
        textphoneNo = findViewById(R.id.editTextProfilePhoneNo);
        textDadNo = findViewById(R.id.editTextProfileDadNo);
        textMomNo = findViewById(R.id.editTextProfileMomNo);
        btnEditProfile = findViewById(R.id.buttonEditProfile);
        imageView = findViewById(R.id.imageViewProfilePic);
        textChangePass = findViewById(R.id.textViewChangePass);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null){
            Toast.makeText(ProfilePage.this, "User's details are not available", Toast.LENGTH_SHORT).show();
        }else {
            checkifEmailVerified(firebaseUser);
            showUserProfile(firebaseUser);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, UploadPic.class);
                startActivity(intent);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, UpdateProfile.class);
                startActivity(intent);
            }
        });

        textChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, ChangePass.class);
                startActivity(intent);
            }
        });
    }

    private void swipetoRefresh() {
        swipePage = findViewById(R.id.swipePage);
        swipePage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(getIntent());
                finish();
                overridePendingTransition(0, 0);
                swipePage.setRefreshing(false);
            }
        });
        //swipePage.setColorSchemeColors(android.R.color.holo_purple, android.R.color.holo_purple, android.R.color.holo_blue_bright,
                //android.R.color.holo_blue_dark;
    }


    private void checkifEmailVerified(FirebaseUser firebaseUser) {
        if (!firebaseUser.isEmailVerified()){
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder build = new AlertDialog.Builder(ProfilePage.this);
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

    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userId = firebaseUser.getUid();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered User");
        referenceProfile.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userDetails = snapshot.getValue(User.class);
                if (userDetails != null){
                    name = firebaseUser.getDisplayName();
                    username = userDetails.username;
                    email = firebaseUser.getEmail();
                    gender = userDetails.gender;
                    phoneNo = userDetails.phoneNo;
                    dadNo = userDetails.dadNo;
                    momNo = userDetails.momNo;

                    textwelcome.setText("Welcome, " + name + "!");
                    textname.setText(name);
                    textusername.setText(username);
                    textemail.setText(email);
                    textgender.setText(gender);
                    textphoneNo.setText(phoneNo);
                    textDadNo.setText(dadNo);
                    textMomNo.setText(momNo);

                    //set user PP
                    Uri uri = firebaseUser.getPhotoUrl();
                    Picasso.with(ProfilePage.this).load(uri).into(imageView);
                }else {
                    Toast.makeText(ProfilePage.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfilePage.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navHome) {
            Intent intent = new Intent(ProfilePage.this, Homepage.class);
            startActivity(intent);
        } else if(itemId == R.id.navAbout){
            Intent intent = new Intent(ProfilePage.this, AboutPage.class);
            startActivity(intent);
        }else if(itemId == R.id.navAwareness){
            Intent intent = new Intent(ProfilePage.this, Awareness.class);
            startActivity(intent);
        }else if (itemId == R.id.navSeekHelp) {
            Intent intent = new Intent(ProfilePage.this, SeekHelpPage.class);
            startActivity(intent);
        } else if(itemId == R.id.navSelfHelp){
            Intent intent = new Intent(ProfilePage.this, SelfHelpPage.class);
            startActivity(intent);
        }else if (itemId == R.id.navContact) {
            Intent intent = new Intent(ProfilePage.this, Contact.class);
            startActivity(intent);
        }else if (itemId == R.id.navEditProfile) {

        } else if(itemId == R.id.navLogout){
            Intent intent = new Intent(ProfilePage.this, Login.class);
            startActivity(intent);
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Contact extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    EditText editSendMsg;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        editSendMsg = findViewById(R.id.editTextSendMsg);
        btn = findViewById(R.id.buttonSendMsg);
        FirebaseAuth authMsg;

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.navContact);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editSendMsg.getText().toString();
                if (msg.length() == 0){
                    Toast.makeText(getApplicationContext(), "Please enter your message.", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    editSendMsg.setText("");
                    Toast.makeText(Contact.this, "Your message send.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navHome) {
            Intent intent = new Intent(Contact.this, Homepage.class);
            startActivity(intent);
        } else if(itemId == R.id.navAbout){
            Intent intent = new Intent(Contact.this, AboutPage.class);
            startActivity(intent);
        }else if(itemId == R.id.navAwareness){
            Intent intent = new Intent(Contact.this, Awareness.class);
            startActivity(intent);
        }else if (itemId == R.id.navSeekHelp) {
            Intent intent = new Intent(Contact.this, SeekHelpPage.class);
            startActivity(intent);
        } else if(itemId == R.id.navSelfHelp){
            Intent intent = new Intent(Contact.this, SelfHelpPage.class);
            startActivity(intent);
        }else if (itemId == R.id.navContact) {

        }else if (itemId == R.id.navEditProfile) {
            Intent intent = new Intent(Contact.this, ProfilePage.class);
            startActivity(intent);
        }else if(itemId == R.id.navLogout){
            Intent intent = new Intent(Contact.this, Login.class);
            startActivity(intent);
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
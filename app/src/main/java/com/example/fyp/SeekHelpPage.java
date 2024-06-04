package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class SeekHelpPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button btn1, btn2, btn3;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_help_page);

        btn1 = findViewById(R.id.buttonCounselor);
        btn2 = findViewById(R.id.buttonParent);
        btn3 = findViewById(R.id.buttonTeacher);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.navSeekHelp);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SeekHelpPage.this, Counselor.class));
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SeekHelpPage.this, Parents.class));
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SeekHelpPage.this, Teachers.class));
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
            Intent intent = new Intent(SeekHelpPage.this, Homepage.class);
            startActivity(intent);
        } else if(itemId == R.id.navAbout){
            Intent intent = new Intent(SeekHelpPage.this, AboutPage.class);
            startActivity(intent);
        }else if(itemId == R.id.navAwareness){
            Intent intent = new Intent(SeekHelpPage.this, Awareness.class);
            startActivity(intent);
        }else if (itemId == R.id.navSeekHelp) {

        } else if(itemId == R.id.navSelfHelp){
            Intent intent = new Intent(SeekHelpPage.this, SelfHelpPage.class);
            startActivity(intent);
        }else if (itemId == R.id.navContact) {
            Intent intent = new Intent(SeekHelpPage.this, Contact.class);
            startActivity(intent);
        }else if (itemId == R.id.navEditProfile) {
            Intent intent = new Intent(SeekHelpPage.this, ProfilePage.class);
            startActivity(intent);
        }else if(itemId == R.id.navLogout){
            Intent intent = new Intent(SeekHelpPage.this, Login.class);
            startActivity(intent);
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
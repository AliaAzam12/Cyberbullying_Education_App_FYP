package com.example.fyp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

//import com.example.fyp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import android.window.SplashScreen;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView about, awareness, support, contactus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        about = findViewById(R.id.textViewAbout);
        awareness = findViewById(R.id.textViewAwareness);
        support = findViewById(R.id.textViewSupport);
        contactus = findViewById(R.id.textViewContact);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.navHome);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, AboutPage.class);
                startActivity(intent);
            }
        });

        awareness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Awareness.class);
                startActivity(intent);
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, SeekHelpPage.class);
                startActivity(intent);
            }
        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Contact.class);
                startActivity(intent);
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


    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navHome) {
            // Handle the navigation to the home screen
        } else if(itemId == R.id.navAbout){
            Intent intent = new Intent(Homepage.this, AboutPage.class);
            startActivity(intent);
        }else if(itemId == R.id.navAwareness){
            Intent intent = new Intent(Homepage.this, Awareness.class);
            startActivity(intent);
        }else if (itemId == R.id.navSeekHelp) {
            Intent intent = new Intent(Homepage.this, SeekHelpPage.class);
            startActivity(intent);
        } else if(itemId == R.id.navSelfHelp){
            Intent intent = new Intent(Homepage.this, SelfHelpPage.class);
            startActivity(intent);
        }else if (itemId == R.id.navContact) {
            Intent intent = new Intent(Homepage.this, Contact.class);
            startActivity(intent);
        }else if (itemId == R.id.navEditProfile) {
            Intent intent = new Intent(Homepage.this, ProfilePage.class);
            startActivity(intent);
        }else if(itemId == R.id.navLogout){
            Intent intent = new Intent(Homepage.this, Login.class);
            startActivity(intent);
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class AboutPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.navAbout);
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
            Intent intent = new Intent(AboutPage.this, Homepage.class);
            startActivity(intent);
        } else if(itemId == R.id.navAbout){

        }else if(itemId == R.id.navAwareness){
            Intent intent = new Intent(AboutPage.this, Awareness.class);
            startActivity(intent);
        }else if (itemId == R.id.navSeekHelp) {
            Intent intent = new Intent(AboutPage.this, SeekHelpPage.class);
            startActivity(intent);
        } else if(itemId == R.id.navSelfHelp){
            Intent intent = new Intent(AboutPage.this, SelfHelpPage.class);
            startActivity(intent);
        }else if (itemId == R.id.navContact) {
            Intent intent = new Intent(AboutPage.this, Contact.class);
            startActivity(intent);
        }else if (itemId == R.id.navEditProfile) {
            Intent intent = new Intent(AboutPage.this, ProfilePage.class);
            startActivity(intent);
        }else if(itemId == R.id.navLogout){
            Intent intent = new Intent(AboutPage.this, Login.class);
            startActivity(intent);
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
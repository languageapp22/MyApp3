package com.example.myapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapp.Cards.CardsM;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
// import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Variables
    static final float END_SCALE = 0.7f; // for the navigation

    ImageView menuIcon;
    LinearLayout contentView;

    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ImageButton settings_button, search_button, logout_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_dashboard);

        //Hooks
        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        //call navigation drawer
        navigationDrawer();

        ImageButton search_button = (ImageButton) findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                startActivity(new Intent(getApplicationContext(),Search.class));
            }
        });

        ImageButton settings_button = (ImageButton) findViewById(R.id.settings_button);
        settings_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                startActivity(new Intent(getApplicationContext(),Settings.class));
            }
        });

        ImageButton logout_button = (ImageButton) findViewById(R.id.logout_button);
        logout_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }

    //Navigation Drawer Functions
    private void navigationDrawer() {

        //Naviagtion Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        drawerLayout.setDrawerElevation(0);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {

        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_flashcards:
                startActivity(new Intent(getApplicationContext(), CardsM.class));
                break;
            case R.id.nav_webview:
                startActivity(new Intent(getApplicationContext(),Webview.class));
                break;
            case R.id.nav_settings:
                startActivity(new Intent(getApplicationContext(),Settings.class));
                break;
            case R.id.nav_share:
                startActivity(new Intent(getApplicationContext(),Share.class));
                break;
            case R.id.nav_dictionary:
                startActivity(new Intent(getApplicationContext(), Dictionary.class));
                break;
            case R.id.nav_logout:{
                FirebaseAuth.getInstance().signOut();    //logout
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
                break;
            }
        }
        return true;
    }
}
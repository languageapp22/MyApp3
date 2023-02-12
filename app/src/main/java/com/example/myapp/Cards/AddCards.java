package com.example.myapp.Cards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.Dashboard;
import com.example.myapp.Login;
import com.example.myapp.R;
import com.example.myapp.Search;
import com.example.myapp.Settings;
import com.example.myapp.Share;
import com.example.myapp.Webview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCards extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private List<List<String>> cards = new ArrayList<>();
    private Deck deck = new Deck();
    private EditText edtTitle, edtFront, edtBack;
    private ProgressBar progressBar;
    String deckId, username;


    //Variables
    static final float END_SCALE = 0.7f; // for the navigation

    ImageView menuIcon;
    LinearLayout contentView;

    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cards);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //Hooks
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        //call navigation drawer
        navigationDrawer();

        assert user != null;
        username = user.getDisplayName();


        edtTitle = (EditText) findViewById(R.id.edtAddTitle);
        edtFront = (EditText) findViewById(R.id.edtFront);
        edtBack = (EditText) findViewById(R.id.edtBack);
        progressBar = (ProgressBar) findViewById(R.id.pgBar);
        progressBar.setVisibility(View.INVISIBLE);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            //set title name, deck contents
        }
        else{
            Date date = new Date();
            deckId = String.valueOf(date.getTime());
            deckId = deckId.substring(deckId.length()-9);
        }

        Button go_back = (Button) findViewById(R.id.go_back);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PublicDecks.class));
            }
        });

        Button tvAddAnother = (Button) findViewById(R.id.tvAddAnother);
        tvAddAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtFront.getText().toString().equals("") && !edtBack.getText().toString().equals("")){
                    cards.add(Arrays.asList(new String[]{edtFront.getText().toString(), edtBack.getText().toString()}));
                    edtFront.setText("");
                    edtBack.setText("");
                    edtFront.requestFocus();
                }
                else{
                    Toast.makeText(com.example.myapp.Cards.AddCards.this, "Incomplete card ! " , Toast.LENGTH_SHORT).show();

                }
            }
        });


        Button tvDone = (Button) findViewById(R.id.tvDone);
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                //adds the remaining card to the deck:
                if (!edtFront.getText().toString().equals("") && !edtBack.getText().toString().equals("")){
                    cards.add(Arrays.asList(new String[]{edtFront.getText().toString(), edtBack.getText().toString()}));
                }
                deck.cards = cards;
                deck.title = edtTitle.getText().toString();
                deck.author = username;
                deck.Uid = FirebaseAuth.getInstance().getUid();
                deck.deckId = deckId;
                if (cards.size() == 0){

                    Intent i = new Intent(AddCards.this, PublicDecks.class);
                    startActivity(i);

                }

                //add deck to firebase:

                FirebaseDatabase.getInstance().getReference("Decks").child(deckId)
                        .setValue(deck).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    FirebaseDatabase.getInstance().getReference("Users").child(deck.Uid).child("MyDecks").child(deckId).setValue(deckId);

                                    Intent i = new Intent(AddCards.this, CardsM.class);
                                    startActivity(i);
                                }
                                else{
                                    Toast.makeText(AddCards.this, "Failed to upload new deck.", Toast.LENGTH_LONG).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
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
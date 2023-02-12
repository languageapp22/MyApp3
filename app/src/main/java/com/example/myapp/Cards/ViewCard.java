package com.example.myapp.Cards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.Dashboard;
import com.example.myapp.Login;
import com.example.myapp.R;
import com.example.myapp.Search;
import com.example.myapp.Settings;
import com.example.myapp.Share;
import com.example.myapp.Webview;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.awt.font.NumericShaper;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ViewCard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private List<List<String>> cards = new ArrayList<>();
    private TextView tvShuffle, tvOrder, tvInfo, tvTitle, tvCard;
    private ImageView ivShuffle, ivOrder, ivInfo;
    private List currCard;
    private boolean front = true;
    private static int f = 0, b = 1;
    private Deck thisDeck;
    private int inc = 0;


    //for drawer menu
    //Variables
    static final float END_SCALE = 0.7f; // for the navigation
    ImageView menuIcon;
    LinearLayout contentView;
    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_card);

        //for drawer menu
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Hooks
        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        //call navigation drawer
        navigationDrawer();


        tvTitle = (TextView) findViewById(R.id.tvCardTitle);
        tvCard = (TextView) findViewById(R.id.tvCard) ;



        try {
            thisDeck = (Deck) getIntent().getSerializableExtra("Deck");
            cards = thisDeck.getCards();
            currCard = cards.get(inc);
            tvTitle.setText(thisDeck.title);
            tvCard.setText(currCard.get(f).toString());
            tvCard.setTypeface(Typeface.DEFAULT_BOLD);
        }
        catch(Exception e){
            Log.d("debug", "Empty deck");
        }

        tvCard.setOnTouchListener(new OnSwipeTouchListener(this) {
                @Override
                public void onSwipeLeft () {
                    front = true;
                    inc++;
                    tvCard.setBackgroundColor(Color.parseColor("#A3FFFFFF"));
                    tvCard.setTypeface(Typeface.DEFAULT_BOLD);
                    try {
                        currCard = cards.get(inc);
                        tvCard.setText(currCard.get(f).toString());
                    } catch(Exception e) {
                        inc = 0;
                        currCard = cards.get(inc);
                        tvCard.setText(currCard.get(f).toString());
                        Toast.makeText(ViewCard.this,"Done", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSwipeRight () {
                    front = true;
                    inc--;
                    tvCard.setBackgroundColor(Color.parseColor("#A3FFFFFF"));
                    tvCard.setTypeface(Typeface.DEFAULT_BOLD);
                    try {
                        currCard = cards.get(inc);
                        tvCard.setText(currCard.get(f).toString());
                    } catch (Exception e) {
                        inc = cards.size() - 1;
                        currCard = cards.get(inc);
                        tvCard.setText(currCard.get(f).toString());
                    }
                }
                @Override
                public void onClick(){
                    if (front){
                        front = false;
                        tvCard.setText(currCard.get(b).toString());
                        tvCard.setBackgroundColor(Color.parseColor("#d3d3d3"));
                        tvCard.setTypeface(Typeface.DEFAULT);

                        Log.d("dhk", currCard.get(b).toString());
                    }
                    else{
                        front = true;
                        tvCard.setTypeface(Typeface.DEFAULT_BOLD);
                        tvCard.setBackgroundColor(Color.parseColor("#A3FFFFFF"));
                        tvCard.setText(currCard.get(f).toString());
                        Log.d("dhk", currCard.get(f).toString());
                    }
                }
        });

    }

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


    public void onClick(View v){

        switch (v.getId()){
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                break;
            case R.id.nav_flashcards:
                startActivity(new Intent(getApplicationContext(), CardsM.class));
                break;
            case R.id.nav_webview:
                startActivity(new Intent(getApplicationContext(), Webview.class));
                break;
            case R.id.nav_settings:
                startActivity(new Intent(getApplicationContext(), Settings.class));
                break;
            case R.id.nav_share:
                startActivity(new Intent(getApplicationContext(), Share.class));
                break;
            case R.id.nav_logout:{
                FirebaseAuth.getInstance().signOut();    //logout
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                break;
            }
        }
        return true;
    }

    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new GestureListener());
        }
        public void onClick(){

        }

        public void onSwipeLeft() {
        }

        public void onSwipeRight() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                onClick();
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight();
                    else
                        onSwipeLeft();
                    return true;
                }
                return false;
            }
        }
    }
}
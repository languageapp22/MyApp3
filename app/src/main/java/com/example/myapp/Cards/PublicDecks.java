package com.example.myapp.Cards;

        import androidx.annotation.NonNull;
        import androidx.annotation.RequiresApi;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.view.GravityCompat;
        import androidx.drawerlayout.widget.DrawerLayout;

        import android.content.Intent;
        import android.graphics.Color;
        import android.os.Build;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.PopupWindow;
        import android.widget.SearchView;
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
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
// import com.google.firebase.database.Query;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
// import java.util.Date;
        import java.util.HashMap;
// import java.util.List;
        import java.util.Map;
        import java.util.Objects;


public class PublicDecks extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ArrayList<com.example.myapp.Cards.Deck> allDecks = new ArrayList<>();
    ArrayList<String> myDeckKeys = new ArrayList<>();
    Map<String, com.example.myapp.Cards.Deck> keyedDecks = new HashMap<>();
    ArrayList<com.example.myapp.Cards.Deck> personalDecks = new ArrayList<>();
    private ListView lvDecks;

    private boolean isGuest = false;
    private boolean inPublic = true;

    FirebaseDatabase rootRef;
    FirebaseAuth mAuth;

    //for drawer menu
    //Variables
    static final float END_SCALE = 0.7f; // for the navigation
    ImageView menuIcon;
    LinearLayout contentView;
    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;


    private ImageView addDeck;
    private TextView myDecks, publicDecks, logout;

    // it was suggested that the following could be transformed into a local variable:
    // private SearchView svDecks;
    // the next line is an attempt to turn the line above in a local variable
    SearchView svDecks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_decks);

        //for drawer menu
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Hooks
        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        //call navigation drawer
        navigationDrawer();

        rootRef = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //check if current user is a guest:
        isGuest = checkGuest();

        if ((ArrayList<com.example.myapp.Cards.Deck>) getIntent().getSerializableExtra("allDecks") != null){
            allDecks = (ArrayList<com.example.myapp.Cards.Deck>) getIntent().getSerializableExtra("allDecks");
        }

        if (!isGuest) {
            DatabaseReference thisUser = rootRef.getReference("Users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                    .child("MyDecks");
            getUserData(thisUser, new com.example.myapp.Cards.PublicDecks.OnGetDataListener() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onFailure() {
                }
            });
        }


        //get the users decks
        if (!isGuest) {
            Log.d("chk_guest", "not guest");
        }

        svDecks = (SearchView) findViewById(R.id.svSearchPublic);
        lvDecks = (ListView) findViewById(R.id.lvDecksPublic);


        Button logout_button = (Button) findViewById(R.id.buttonLogout);
        logout_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                LayoutInflater inflater1 = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView1 = inflater1.inflate(R.layout.create_deck_popup, null);

                TextView tvPopup1 = popupView1.findViewById(R.id.tvPopup);
                tvPopup1.setText("Logout?");

                // create the popup window
                int width1 = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height1 = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable1 = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow1 = new PopupWindow(popupView1, width1, height1, focusable1);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow1.showAtLocation(v, Gravity.CENTER, 0, 0);

                popupView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout();
                    }
                });
            }
        });


        Button public_button = (Button) findViewById(R.id.tvPublicDecks);
        public_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inPublic = true;
                com.example.myapp.Cards.DeckListAdapter adapter = new com.example.myapp.Cards.DeckListAdapter(isGuest, public_button.getContext(), R.layout.deck_lv_item, allDecks);
                lvDecks.setAdapter(adapter);
                switchToPublic();
            }
        });

        Button personal_button = (Button) findViewById(R.id.tvMyDecks);
        personal_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isGuest){
                    Toast.makeText(com.example.myapp.Cards.PublicDecks.this, "You must be signed in.", Toast.LENGTH_LONG).show();
                }
                else {
                    //switch decks
                    inPublic = false;
                    com.example.myapp.Cards.MyDeckListAdapter adapter = new com.example.myapp.Cards.MyDeckListAdapter(isGuest, personal_button.getContext(), R.layout.deck_lv_item, personalDecks);
                    lvDecks.setAdapter(adapter);
                    switchToMine();
                }
            }
        });

        ImageButton create_button = (ImageButton) findViewById(R.id.abPlusPublic);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGuest){
                    Toast.makeText(com.example.myapp.Cards.PublicDecks.this, "You must be signed in.", Toast.LENGTH_LONG).show();
                }
                else {
                    LayoutInflater inflater = (LayoutInflater)
                            getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.create_deck_popup, null);

                    TextView tvPopup = popupView.findViewById(R.id.tvPopup);
                    tvPopup.setText("Create Deck?");

                    // create the popup window
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                    // show the popup window
                    // which view you pass in doesn't matter, it is only used for the window tolken
                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                    popupView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addCards();
                        }
                    });
                }
            }
        });



        try{
            inPublic = getIntent().getBooleanExtra("isPublic", true);
        }
        catch(Exception e){
        }

        if ((ArrayList<com.example.myapp.Cards.Deck>) getIntent().getSerializableExtra("personalDecks") != null) {
            personalDecks = (ArrayList<com.example.myapp.Cards.Deck>) getIntent().getSerializableExtra("personalDecks");
        }



        if (inPublic){
            com.example.myapp.Cards.DeckListAdapter adapter = new com.example.myapp.Cards.DeckListAdapter(isGuest,this, R.layout.deck_lv_item, allDecks);
            lvDecks.setAdapter(adapter);

        }
        else{
            com.example.myapp.Cards.MyDeckListAdapter adapter = new com.example.myapp.Cards.MyDeckListAdapter(isGuest,this, R.layout.deck_lv_item, personalDecks);
            lvDecks.setAdapter(adapter);

        }

        lvDecks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<com.example.myapp.Cards.Deck> sendDeck = new ArrayList<>();
                if(inPublic){
                    sendDeck = allDecks;
                }
                else{
                    sendDeck = personalDecks;
                }

                Intent i = new Intent(com.example.myapp.Cards.PublicDecks.this, com.example.myapp.Cards.ViewCard.class);
                i.putExtra("Deck", sendDeck.get(position));
                startActivity(i);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    // @Override
    public void onClick(View v){

        switch (v.getId()){

            case R.id.svSearchPublic:
                searchPublic();
                Log.d("search", "Clicked");
                break;
        }
    }

    public void searchPublic(){
        return;
    }

    public boolean checkGuest(){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user==null){
            return true;
        }
        return false;
    }

    public void createDeck(){
        Intent i = new Intent(com.example.myapp.Cards.PublicDecks.this, com.example.myapp.Cards.CreateDeck.class);
        startActivity(i);
    }

    public void addCards(){
        Intent i = new Intent (com.example.myapp.Cards.PublicDecks.this, com.example.myapp.Cards.AddCards.class);
        startActivity(i);
    }

    public void switchToPublic(){
        inPublic = true;
    }

    public void switchToMine(){
        inPublic = false;
    }

    public void getUserData(DatabaseReference thisUser, final com.example.myapp.Cards.PublicDecks.OnGetDataListener listener){


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot datasnapshot){
                for (DataSnapshot snapshot : datasnapshot.getChildren()){
                    String hi = snapshot.getValue(String.class);
                    myDeckKeys.add(hi);
                }

                for (com.example.myapp.Cards.Deck d : allDecks){
                    keyedDecks.put(String.valueOf(d.deckId), d);
                }
                // find user specific decks:
                for (String s : myDeckKeys){
                    personalDecks.add(keyedDecks.get(s));
                }
                listener.onSuccess(datasnapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailure();
            }
        };
        thisUser.addListenerForSingleValueEvent(valueEventListener);
    }

    public interface OnGetDataListener {
        //this is for callbacks
        void onSuccess(DataSnapshot dataSnapshot);
        void onFailure();
    }

    public void logout(){
        mAuth.signOut();
        Intent i = new Intent(com.example.myapp.Cards.PublicDecks.this, com.example.myapp.Cards.LoginActivity.class);
        i.putExtra("allDecks", allDecks);
        startActivity(i);
    }

    // Navigation Drawer Functions
    private void navigationDrawer() {

        //Navigation Drawer
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                break;
            case R.id.nav_flashcards:
                startActivity(new Intent(getApplicationContext(), Search.class));
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
            case R.id.nav_login:
                startActivity(new Intent(getApplicationContext(), com.example.myapp.Cards.LoginActivity.class));
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

    // end of Navigation Drawer Functions


}
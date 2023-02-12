package com.example.myapp.Cards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class CreateDeck extends AppCompatActivity implements View.OnClickListener {

    private Button btnSave;
    private TextView tvAddCards;
    private ListView lvCards;
    private EditText edtTitle;
    private String Uid = FirebaseAuth.getInstance().getUid();
    private Deck thisDeck = new Deck();
    private String deckId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deck);

        btnSave = (Button) findViewById(R.id.btnSaveDeck);
        tvAddCards = (TextView) findViewById(R.id.tvAdd);
        lvCards = (ListView) findViewById(R.id.lvEditCards);
        edtTitle = (EditText) findViewById(R.id.edtDeckTitle);

        btnSave.setOnClickListener(this);
        tvAddCards.setOnClickListener(this);

        //check if we are editing a pre-existing deck
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            thisDeck = (Deck) bundle.getSerializable("Deck");
            deckId = bundle.getString("did");
            edtTitle.setText(thisDeck.title);
        }
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnSaveDeck:

                break;
            case R.id.tvAdd:
                Intent i = new Intent(this, AddCards.class);
                startActivity(i);
                break;
        }

    }
}
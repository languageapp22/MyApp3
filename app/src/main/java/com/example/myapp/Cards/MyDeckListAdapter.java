package com.example.myapp.Cards;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapp.R;

import java.util.ArrayList;
import java.util.List;

public class MyDeckListAdapter extends ArrayAdapter<Deck> {

    Context ctx;
    boolean isGuest;
    List<Deck> deck;

    public MyDeckListAdapter(boolean isGuest, Context ctx, int resource, List<Deck> deck) {
        super( ctx, resource, deck);
        this.ctx = ctx;
        this.deck = deck;
        this.isGuest = isGuest;
    }

    @NonNull
    @Override
    public View getView( int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(ctx);

        View v = inflater.inflate(R.layout.deck_lv_item, null);

        TextView author = (TextView) v.findViewById(R.id.tvDeckAuthor);
        TextView title = (TextView) v.findViewById(R.id.tvDeckName);
        ImageButton btnDelete = (ImageButton) v.findViewById(R.id.btnDelete);


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Delete");
                builder.setMessage("You cannot undo this action.");
                builder.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("chk", "delete");

                                Intent i = new Intent(getContext(), PublicDecks.class);
                                String deckId = deck.get(position).deckId;
                                deck.remove(position);
                                ArrayList<Deck> allDecks = (ArrayList<Deck>) deck;
                                i.putExtra("personalDecks", allDecks);
                                i.putExtra("deckId", deckId);
                                i.putExtra("isPublic", false);
                                ctx.startActivity(i);
                                //1. reload intent with inPublic = false
                                //2. send position to public decks

                                //3. allDecks.remove(position) --> get this deck's id
                                //4. remove deck's id from user

                                //5. update Firebase user and deck

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        });

        Deck myDeck = deck.get(position);
        author.setText(myDeck.getAuthor());
        title.setText(myDeck.getTitle());

        return v;
    }

}

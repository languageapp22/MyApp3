package com.example.myapp.Cards;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapp.R;

import java.util.ArrayList;
import java.util.List;

public class DeckListAdapter extends ArrayAdapter<Deck> {

    Context ctx;
    boolean isGuest;
    List<Deck> deck;

    public DeckListAdapter(boolean isGuest, Context ctx, int resource, List<Deck> deck) {
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

        Deck myDeck = deck.get(position);

        author.setText(myDeck.getAuthor());
        title.setText(myDeck.getTitle());

        return v;
    }

}

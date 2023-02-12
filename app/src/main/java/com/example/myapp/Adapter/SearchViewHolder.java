package com.example.myapp.Adapter;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;

class SearchViewHolder extends RecyclerView.ViewHolder {

    public TextView word, translation;

    public SearchViewHolder(View itemView) {
        super(itemView);
        word = (TextView) itemView.findViewById(R.id.word);
        translation = (TextView) itemView.findViewById(R.id.translation);
    }
}


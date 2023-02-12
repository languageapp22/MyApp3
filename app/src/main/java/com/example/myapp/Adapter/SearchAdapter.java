package com.example.myapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.Model.Word;
import com.example.myapp.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    private Context context;
    private List<Word> words;

    public SearchAdapter(Context context, List<Word> words) {
        this.context = context;
        this.words = words;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.layout_item,parent,false);
        return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.word.setText(words.get(position).getWord());
        holder.translation.setText(words.get(position).getTranslation());
    }

    @Override
    public int getItemCount() {
        return words.size();
    }
}

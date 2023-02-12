package com.example.myapp.Cards;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Deck implements Serializable {
    String title, author, Uid, deckId;
    List<List<String>> cards = new ArrayList<>();

    public Deck(){

    }

    public Deck (String deckId, String Uid, String title, String author, List<List<String>> cards){
        this.deckId = deckId;
        this.title = title;
        this.author = author;
        this.cards = cards;
        this.Uid = Uid;
    }

    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getDeckId() {return deckId;}
    public String getUid(){
        return Uid;
    }
    public List<List<String>> getCards(){
        return cards;
    }
}

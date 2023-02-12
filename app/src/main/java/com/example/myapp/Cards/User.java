package com.example.myapp.Cards;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class User {
    public String username, email;
    public ArrayList<String> myDecks = new ArrayList<>();

    public User(){

    }

    public User(String username, String email, ArrayList<String> myDecks){
        this.username = username;
        this.email = email;
        this.myDecks = myDecks;

    }
}

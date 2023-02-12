package com.example.myapp.Model;

public class Word {
    public int id;
    public String word, translation;

    public Word(int id, String word, String translation) {
        this.id = id;
        this.word = word;
        this.translation = translation;
    }

    // Getters and setters

    public Word() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String name) {
        this.word = name;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

}

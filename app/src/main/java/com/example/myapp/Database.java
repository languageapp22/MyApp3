package com.example.myapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.myapp.Model.Word;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME="WORDS.db";
    private static final int DB_VER=1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    // function get all words
    public List<Word> getAllWords(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"Id", "Word", "Translation"};
        String tableName = "Words";

        qb.setTables(tableName);
        Cursor cursor = qb.query(db,sqlSelect,null, null, null, null, null);
        List<Word> result = new ArrayList<>();
        if(cursor.moveToFirst())
        {
            do {
                Word word = new Word();
                word.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                word.setWord(cursor.getString(cursor.getColumnIndex("Word")));
                word.setTranslation(cursor.getString(cursor.getColumnIndex("Translation")));
                result.add(word);
            } while(cursor.moveToNext());
        }
        return result;
    }

    // function get all words' words
    public List<String> getWords() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"Word"};
        String tableName = "Words";

        qb.setTables(tableName);
        Cursor cursor = qb.query(db,sqlSelect,null, null, null, null, null);
        List<String> result = new ArrayList<>();
        if(cursor.moveToFirst())
        {
            do {
                result.add(cursor.getString(cursor.getColumnIndex("Word")));
            } while(cursor.moveToNext());
        }
        return result;
    }

    //function get word by it's naming
    public List<Word> getWordByWord(String word)
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"Id", "Word", "Translation", };
        String tableName = "Words";

        qb.setTables(tableName);

        Cursor cursor = qb.query(db,sqlSelect,"Word LIKE ?", new String[] {"%" + word + "%"}, null, null, null);
        List<Word> result = new ArrayList<>();
        if(cursor.moveToFirst())
        {
            do {
                Word word1 = new Word();
                word1.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                word1.setWord(cursor.getString(cursor.getColumnIndex("Word")));
                word1.setTranslation(cursor.getString(cursor.getColumnIndex("Translation")));
                result.add(word1);
            } while(cursor.moveToNext());
        }
        return result;
    }
}

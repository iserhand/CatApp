package com.iserhand.inviochallengefinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "favcats";
    private static final String DB_NAME = "database";
    private static final int VERSION = 1;
    private static final String[] columns = {"id", "catID", "name", "description", "image", "origin", "wikipedia_url",
            "isFav", "life_span", "energy_level"
            , "shedding_level", "social_needs", "child_friendly", "dog_friendly", "vocalisation", "health_issues"};
    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE favcats(id INTEGER PRIMARY KEY AUTOINCREMENT,catID TEXT,name TEXT,description TEXT,image TEXT, origin TEXT," +
                "wikipedia_url TEXT,isFav INTEGER,life_span TEXT,energy_level INTEGER,shedding_level INTEGER,social_needs INTEGER,child_friendly INTEGER,dog_friendly INTEGER,vocalisation INTEGER,health_issues INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<Cat> listAll(SQLiteDatabase db) throws SQLException {
        this.db = db;
        ArrayList<Cat> favCatList = new ArrayList<>();
        Cursor cursor = db.query("favcats", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Cat cat = new Cat();
            cat.setId(cursor.getString((cursor.getColumnIndexOrThrow(columns[1]))));
            cat.setName(cursor.getString(cursor.getColumnIndexOrThrow(columns[2])));
            cat.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(columns[3])));
            cat.setImage(cursor.getString(cursor.getColumnIndexOrThrow(columns[4])));
            cat.setOrigin(cursor.getString(cursor.getColumnIndexOrThrow(columns[5])));
            cat.setWiki_url(cursor.getString(cursor.getColumnIndexOrThrow(columns[6])));
            cat.setIsFav(cursor.getInt(cursor.getColumnIndexOrThrow(columns[7])));
            cat.setLife_span(cursor.getString(cursor.getColumnIndexOrThrow(columns[8])));
            cat.setEnergy_level(cursor.getInt(cursor.getColumnIndexOrThrow(columns[9])));
            cat.setShedding_level(cursor.getInt(cursor.getColumnIndexOrThrow(columns[10])));
            cat.setSocial_needs(cursor.getInt(cursor.getColumnIndexOrThrow(columns[11])));
            cat.setChild_friendly(cursor.getInt(cursor.getColumnIndexOrThrow(columns[12])));
            cat.setDog_friendly(cursor.getInt(cursor.getColumnIndexOrThrow(columns[13])));
            cat.setVocalisation(cursor.getInt(cursor.getColumnIndexOrThrow(columns[14])));
            cat.setHealth_issues(cursor.getInt(cursor.getColumnIndexOrThrow(columns[15])));
            favCatList.add(cat);
        }
        return favCatList;
    }

    public ArrayList<String> getFavoritedInfo(SQLiteDatabase db) throws SQLException {
        ArrayList<String> favoritedIdList = new ArrayList<>();
        Cursor cursor = db.query("favcats", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            favoritedIdList.add(cursor.getString(cursor.getColumnIndexOrThrow(columns[2])));
        }
        return favoritedIdList;
    }

    public boolean isFavorited(SQLiteDatabase db, String id) {
        Cursor cursor = db.query("favcats", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndexOrThrow(columns[1])).equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void insert(SQLiteDatabase db, Cat cat) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(columns[1], cat.getId());
        contentValues.put(columns[2], cat.getName());
        contentValues.put(columns[3], cat.getDescription());
        contentValues.put(columns[4], cat.getImage());
        contentValues.put(columns[5], cat.getOrigin());
        contentValues.put(columns[6], cat.getWiki_url());
        contentValues.put(columns[7], cat.getIsFav());
        contentValues.put(columns[8], cat.getLife_span());
        contentValues.put(columns[9], cat.getEnergy_level());
        contentValues.put(columns[10], cat.getShedding_level());
        contentValues.put(columns[11], cat.getSocial_needs());
        contentValues.put(columns[12], cat.getChild_friendly());
        contentValues.put(columns[13], cat.getDog_friendly());
        contentValues.put(columns[14], cat.getVocalisation());
        contentValues.put(columns[15], cat.getHealth_issues());
        db.insertOrThrow("favcats", null, contentValues);
    }

    public void delete(SQLiteDatabase db, String id) throws SQLException {
        db.delete("favcats", "catID='" + id + "'", null);
    }


}


package com.iserhand.inviochallengefinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {
    private  RecyclerView recyclerView ;
    ArrayList<Cat> catArrayList=new ArrayList<>();
    CatAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        assert getSupportActionBar() != null;
        recyclerView=findViewById(R.id.recycler_view_favorites);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Favorites");
        NestedScrollView nestedScrollView = findViewById(R.id.scroll_view_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DBHelper helper=new DBHelper(this);
        SQLiteDatabase db=helper.getWritableDatabase();
        catArrayList=helper.listAll(db);
        adapter= new CatAdapter(FavoritesActivity.this,catArrayList,FavoritesActivity.this,"FAV");
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
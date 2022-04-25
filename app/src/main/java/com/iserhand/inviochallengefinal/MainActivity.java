package com.iserhand.inviochallengefinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    ArrayList<Cat> catArrayList = new ArrayList<>();
    private final Handler resumeHandler=new Handler();
    CatAdapter adapter;
    int page = 0;
    int limit = 25;
    String queryStr = "";
    MenuItem showFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_InvioChallengeFinal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NestedScrollView nestedScrollView = findViewById(R.id.scroll_view);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        adapter = new CatAdapter(MainActivity.this, catArrayList, MainActivity.this, "MAIN");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        getData(page, limit);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //Condition validation
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    //Reached last item
                    //Add more pages
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    //Call getData to fetch more data.
                    getData(page, limit);
                }
            }
        });
        ;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {

        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onResume() {
        UpdateList update =new UpdateList();
        update.start();
        super.onResume();
    }

    class UpdateList extends Thread {
        DBHelper helper = new DBHelper(MainActivity.this);
        SQLiteDatabase database = helper.getReadableDatabase();
        public void run() {
            for (Cat cat : catArrayList) {
                if (helper.isFavorited(database, cat.getId())) {
                    cat.setIsFav(1);
                } else {
                    cat.setIsFav(0);
                }
            }
            resumeHandler.post(new Runnable() {
                @Override
                public void run() {
                    adapter = new CatAdapter(MainActivity.this, catArrayList, MainActivity.this, "MAIN");
                    recyclerView.setAdapter(adapter);
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Siberian");
        showFav = menu.findItem(R.id.favoritesButton);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                page = 0;
                queryStr = query;
                if (query.isEmpty()) {
                    catArrayList.clear();
                    getData(page, limit);
                } else {
                    catArrayList.clear();
                    getData(queryStr);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    catArrayList.clear();
                    getData(page, limit);
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.favoritesButton) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData(int page, int limit) {
        //Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        //Main interface for cat
        CatInterface catInterface = retrofit.create(CatInterface.class);
        Call<String> call = catInterface.STRING_CALL("3da6d958-28f2-46f9-b9a5-b8edacce5a74", page, limit);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //Verifying conditions
                if (response.isSuccessful() && response.body() != null) {
                    //Hide progress bar for successful responses.
                    progressBar.setVisibility(View.GONE);
                    try {
                        //Create jsonarray
                        JSONArray jsonArray = new JSONArray(response.body());
                        parseResult(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });

    }

    //Getting data with search query
    private void getData(String queryStr) {
        //Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        //Main interface for cat
        CatInterface catInterface = retrofit.create(CatInterface.class);
        Call<String> call = catInterface.STRING_CALL_SEARCH("3da6d958-28f2-46f9-b9a5-b8edacce5a74", queryStr);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //Verifying conditions
                if (response.isSuccessful() && response.body() != null) {
                    //Hide progress bar for successful responses.
                    progressBar.setVisibility(View.GONE);
                    try {
                        //Create jsonarray
                        JSONArray jsonArray = new JSONArray(response.body());
                        parseResultSearch(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });

    }

    private void parseResult(JSONArray jsonArray) {
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase database = helper.getReadableDatabase();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                //Create jsonobject from array
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //Get cat data from jsonobject
                Cat data = new Cat();
                //Set cat name
                data.setName(jsonObject.getString("name"));
                //Set cat image
                JSONObject jsimg = jsonObject.getJSONObject("image");
                data.setImage(jsimg.getString("url"));
                //Set cat id
                data.setId(jsonObject.getString("id"));
                //Set description
                data.setDescription(jsonObject.getString("description"));
                //Set cat origin
                data.setOrigin(jsonObject.getString("origin"));
                //Set wikipedia url
                data.setWiki_url(jsonObject.getString("wikipedia_url"));
                if (helper.isFavorited(database, data.getId())) {
                    data.setIsFav(1);
                } else {
                    data.setIsFav(0);
                }
                data.setLife_span(jsonObject.getString("life_span"));
                data.setEnergy_level(jsonObject.getInt("energy_level"));
                data.setShedding_level(jsonObject.getInt("shedding_level"));
                data.setSocial_needs(jsonObject.getInt("social_needs"));
                data.setChild_friendly(jsonObject.getInt("child_friendly"));
                data.setDog_friendly(jsonObject.getInt("dog_friendly"));
                data.setVocalisation(jsonObject.getInt("vocalisation"));
                data.setHealth_issues(jsonObject.getInt("health_issues"));
                catArrayList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adapter initialise
            adapter = new CatAdapter(MainActivity.this, catArrayList, MainActivity.this, "MAIN");
            recyclerView.setAdapter(adapter);
        }
    }

    private void parseResultSearch(JSONArray jsonArray) {
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase database = helper.getReadableDatabase();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                //Create jsonobject from array
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //Get cat data from jsonobject
                Cat data = new Cat();
                //Set cat name
                data.setName(jsonObject.getString("name"));
                //Set cat image
                data.setImage("https://cdn2.thecatapi.com/images/" + jsonObject.getString("reference_image_id") + ".jpg");
                //Set cat id
                data.setId(jsonObject.getString("id"));
                //Set cat details
                data.setDescription(jsonObject.getString("description"));
                //Set cat origin
                data.setOrigin(jsonObject.getString("origin"));
                //Set wikipedia url
                data.setWiki_url(jsonObject.getString("wikipedia_url"));
                if (helper.isFavorited(database, data.getId())) {
                    data.setIsFav(1);
                } else {
                    data.setIsFav(0);
                }
                data.setLife_span(jsonObject.getString("life_span"));
                data.setEnergy_level(jsonObject.getInt("energy_level"));
                data.setShedding_level(jsonObject.getInt("shedding_level"));
                data.setSocial_needs(jsonObject.getInt("social_needs"));
                data.setChild_friendly(jsonObject.getInt("child_friendly"));
                data.setDog_friendly(jsonObject.getInt("dog_friendly"));
                data.setVocalisation(jsonObject.getInt("vocalisation"));
                data.setHealth_issues(jsonObject.getInt("health_issues"));
                catArrayList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adapter initialise
            adapter = new CatAdapter(MainActivity.this, catArrayList, MainActivity.this, "MAIN");
            recyclerView.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }
}
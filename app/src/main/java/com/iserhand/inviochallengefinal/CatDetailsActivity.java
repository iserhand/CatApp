package com.iserhand.inviochallengefinal;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class CatDetailsActivity extends AppCompatActivity {
    TextView catDescription;
    ImageView catImg;
    TextView catOrigin;
    TextView wikiUrl;
    TextView lifeSpan;
    RatingBar energy_level;
    RatingBar shedding_level;
    RatingBar social_needs;
    RatingBar child_friendly;
    RatingBar dog_friendly;
    RatingBar vocalisation;
    RatingBar health_issues;
    MenuItem favItem;
    int isFav;
    String catID;
    Cat cat = new Cat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("catName"));
        setContentView(R.layout.activity_cat_details);
        wikiUrl=findViewById(R.id.textWikiUrl);
        lifeSpan=findViewById(R.id.textLifeSpan);
        energy_level=findViewById(R.id.energy_level);
        shedding_level=findViewById(R.id.shedding_level);
        social_needs=findViewById(R.id.social_needs);
        child_friendly=findViewById(R.id.child_friendly);
        dog_friendly=findViewById(R.id.dog_friendly);
        vocalisation=findViewById(R.id.vocalisation);
        health_issues=findViewById(R.id.health_issues);

        wikiUrl.setText("Wikipedia Url: "+getIntent().getStringExtra("wikiUrl"));
        lifeSpan.setText("Life span: "+getIntent().getStringExtra("life_span"));
        energy_level.setRating(getIntent().getIntExtra("energy_level",0));
        shedding_level.setRating(getIntent().getIntExtra("shedding_level",0));
        social_needs.setRating(getIntent().getIntExtra("social_needs",0));
        child_friendly.setRating(getIntent().getIntExtra("child_friendly",0));
        dog_friendly.setRating(getIntent().getIntExtra("dog_friendly",0));
        vocalisation.setRating(getIntent().getIntExtra("vocalisation",0));
        health_issues.setRating(getIntent().getIntExtra("health_issues",0));

        catID = getIntent().getStringExtra("catID");
        catDescription = findViewById(R.id.textDetails);
        catImg = findViewById(R.id.detailImg);
        catOrigin = findViewById(R.id.textOrigin);
        Glide.with(this).load(getIntent().getStringExtra("catImg")).diskCacheStrategy(DiskCacheStrategy.ALL).into(catImg);
        catDescription.setText(getIntent().getStringExtra("catDetails"));
        catOrigin.setText(getString(R.string.details_origin) + getIntent().getStringExtra("catOrigin"));
        isFav = getIntent().getIntExtra("isFav", 0);

    }

    @Override
    protected void onStop() {
        //Change the favorited state of a cat object here to avoid many database connections.

        super.onStop();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cat_details, menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        favItem = menu.findItem(R.id.isFav);
        if (isFav == 1) {
            favItem.setIcon(R.drawable.ic_baseline_star_24);
        } else {
            favItem.setIcon(R.drawable.ic_baseline_star_border_24);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        if (isFav == 1) {
            isFav = 0;
            favItem.setIcon(R.drawable.ic_baseline_star_border_24);
            helper.delete(db, catID);
        } else {
            isFav = 1;
            cat.setIsFav(isFav);
            cat.setId(catID);
            cat.setOrigin(getIntent().getStringExtra("catOrigin"));
            cat.setName(getIntent().getStringExtra("catName"));
            cat.setImage(getIntent().getStringExtra("catImg"));
            cat.setDescription(getIntent().getStringExtra("catDetails"));
            cat.setOrigin(getIntent().getStringExtra("catOrigin"));
            cat.setWiki_url(getIntent().getStringExtra("wikiUrl"));
            favItem.setIcon(R.drawable.ic_baseline_star_24);
            helper.insert(db, cat);
        }
        return super.onOptionsItemSelected(item);
    }
}
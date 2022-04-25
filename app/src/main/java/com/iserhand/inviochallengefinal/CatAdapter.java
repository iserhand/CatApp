package com.iserhand.inviochallengefinal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.ViewHolder> {
    //Initialize required variables
    private ArrayList<Cat> catArrayList;
    private Activity activity;
    private Context mContext;
    private String whichScreen;
    public View view;
    public Cat currentItem;
    //Adapter constructor.
    public CatAdapter(Activity activity, ArrayList<Cat> catArrayList,Context context,String screenName){
        this.whichScreen=screenName;
        this.activity=activity;
        this.catArrayList=catArrayList;
        this.mContext = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Initialize viewholder.
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cats_main,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Initialize data
        Cat data=catArrayList.get(position);
        //Set image API images are stored as jpg or png, if fails to read jpg,tries png.
        try{
            Glide.with(activity).load(data.getImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
        }
        catch (Exception ex){
            data.setImage(data.getImage().replace(".jpg",".png"));
            Glide.with(activity).load(data.getImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
        }
        //Set text as cat name
        holder.textView.setText(data.getName());
        int favcheck=data.getIsFav();
        if(favcheck==1){
            holder.favButton.setImageResource(R.drawable.ic_mainmenu_favorited);
        }else{
            holder.favButton.setImageResource(R.drawable.ic_mainmenu_notfavorited);
        }
    }
    @Override
    public int getItemCount() {
        return catArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Initializing UI item variables.
        ImageView imageView;
        TextView textView;
        ImageView favButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Assigning variables
            favButton=itemView.findViewById(R.id.favButton);
            imageView=itemView.findViewById(R.id.image_view);
            textView=itemView.findViewById(R.id.text_view);
            itemView.setOnClickListener(this);
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DBHelper helper=new DBHelper(mContext);
                    SQLiteDatabase db=helper.getWritableDatabase();
                    int position =getLayoutPosition();
                    AtomicInteger isFav;
                    isFav = new AtomicInteger(catArrayList.get(position).getIsFav());
                    if(isFav.get() ==1){
                        favButton.setImageResource(R.drawable.ic_mainmenu_notfavorited);
                        catArrayList.get(position).setIsFav(0);
                        helper.delete(db,catArrayList.get(position).getId());
                        if(whichScreen.equals("FAV")){
                            catArrayList.remove(position);
                            notifyItemRemoved(position);
                        }
                    }else{
                        favButton.setImageResource(R.drawable.ic_mainmenu_favorited);
                        catArrayList.get(position).setIsFav(1);
                        helper.insert(db,catArrayList.get(position));
                    }
                }
            });
        }
        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            Intent intent = new Intent(mContext,CatDetailsActivity.class);
            intent.putExtra("catID",catArrayList.get(position).getId());
            intent.putExtra("catName",catArrayList.get(position).getName());
            intent.putExtra("catImg",catArrayList.get(position).getImage());
            intent.putExtra("catDetails",catArrayList.get(position).getDescription());
            intent.putExtra("catOrigin",catArrayList.get(position).getOrigin());
            intent.putExtra("wikiUrl",catArrayList.get(position).getWiki_url());
            intent.putExtra("isFav",catArrayList.get(position).getIsFav());
            mContext.startActivity(intent);
        }
    }


}

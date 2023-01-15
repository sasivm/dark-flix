package com.example.darkflix.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.darkflix.Model.MovieSearchModel;
import com.example.darkflix.MovieDetailActivity;
import com.example.darkflix.R;
import com.example.darkflix.Utility.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class MovsRecyclerViewAdapter extends RecyclerView.Adapter<MovsRecyclerViewAdapter.ViewHolder> {

    Context context;
    List<MovieSearchModel> movsModelArrayList;
    int parentPostion;

   public MovsRecyclerViewAdapter(Context context, int parentPostion, List<MovieSearchModel> movsModelArrayList) {
       this.movsModelArrayList = movsModelArrayList;
       this.context = context;
       this.parentPostion = parentPostion;
   }

   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.poster_layout, parent, false);
       return new MovsRecyclerViewAdapter.ViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       MovieSearchModel movie = movsModelArrayList.get(position);
       holder.rate.setText(movie.getVote_average());
       String type = movie.getType();
       if(type.equals(AppConstants.SHOWS_TYPE)) {
           holder.title.setText(movie.getName());
       } else {
           holder.title.setText(movie.getTitle());
       }
       String poster_url = AppConstants.POSTER_PATH_PREFIX + movie.getPoster_path();
       Glide.with(holder.imageView).load(poster_url)
               .transform(new CenterInside(), new RoundedCorners(100))
               .fitCenter().into(holder.imageView);

       holder.imageView.setOnClickListener(v -> {
           // When you're inside the click listener interface,
           // you can access the position using the ViewHolder.
           // We'll store the position in the member variable in this case.
           Log.i(" Selected Item Here", holder.getAdapterPosition() + "");
           Log.i("parentPostion ", parentPostion + "");
           Log.i("onClick: ", movsModelArrayList.get(holder.getAdapterPosition()).toString());
           Intent intent = new Intent(context, MovieDetailActivity.class);
           intent.putExtra("PARENT_IDX", parentPostion + "");
           intent.putExtra("CHILD_IDX", holder.getAdapterPosition() + "");
           context.startActivity(intent);
       });
   }

   @Override
   public int getItemCount() {
       return movsModelArrayList.size();
   }

    static class ViewHolder extends RecyclerView.ViewHolder {
       TextView rate, title;
       ImageView imageView;

       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           rate = itemView.findViewById(R.id.mov_rate);
           title = itemView.findViewById(R.id.mov_title);
           imageView = itemView.findViewById(R.id.mov_img);
       }
    }
}
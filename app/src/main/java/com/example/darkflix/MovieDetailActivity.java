package com.example.darkflix;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.darkflix.Model.MovieSearchModel;
import com.example.darkflix.Repository.MovieSearchRepo;
import com.example.darkflix.Utility.AppConstants;

public class MovieDetailActivity extends AppCompatActivity {
    TextView title, release_date, duration;
    int parentIdx, childIdx;
    ImageView poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Bundle extras = getIntent().getExtras();
        if (extras.size() > 0) {
            parentIdx = Integer.parseInt(extras.getString("PARENT_IDX"));
            childIdx = Integer.parseInt(extras.getString("CHILD_IDX"));

            Log.i("Movie Activity ", parentIdx + "");
            Log.i("Movie Activity ", childIdx + "");
            //The key argument here must match that used in the other activity
        }

        title = findViewById(R.id.mov_det_title);
        release_date = findViewById(R.id.mov_det_date);
        duration = findViewById(R.id.mov_det_time);
        poster = findViewById(R.id.mov_det_backdrop);

        MovieSearchModel movie = MovieSearchRepo.getList().get(parentIdx).get(childIdx);

        String mov_title;
        if (AppConstants.SHOWS_TYPE.equals(movie.getType())) {
            mov_title = movie.getName();
        } else {
            mov_title = movie.getTitle();
        }
        title.setText(mov_title);
        release_date.setText(movie.getRelease_date());
        duration.setText(movie.getPopularity());

        String poster_full_url = AppConstants.POSTER_PATH_PREFIX + movie.getBackdrop_path();
        Glide.with(this).load(poster_full_url).fitCenter().into(poster);
    }
}
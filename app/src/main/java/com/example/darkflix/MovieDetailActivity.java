package com.example.darkflix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.darkflix.API.MoviesDBAPIInterface;
import com.example.darkflix.API.RetrofitClient;
import com.example.darkflix.Model.MovieModel;
import com.example.darkflix.Model.MovieSearchModel;
import com.example.darkflix.Repository.MovieSearchRepo;
import com.example.darkflix.Utility.AppConstants;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {
    TextView title, release_date, duration, overview;
    ChipGroup pChipGroup;
    ImageView poster;
    MovieModel movie;
    String TYPE;
    int parentIdx, childIdx;

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
        overview = findViewById(R.id.mov_overview);
        pChipGroup = findViewById(R.id.genreGroup);

        MovieSearchModel movie = MovieSearchRepo.getList().get(parentIdx).get(childIdx);
        TYPE = movie.getType();

        getMovieData(movie.getId());
    }

    private void getMovieData(int id) {
        MoviesDBAPIInterface apiInterface = RetrofitClient.getRftInstance().create(MoviesDBAPIInterface.class);
        Call<MovieModel> call = apiInterface.getMovieDetails(id, AppConstants.MOVIESDB_APIKEY);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(@NonNull Call<MovieModel> call, @NonNull Response<MovieModel> response) {
                assert response.body() != null;
                String res = response.body().toString();
                Log.i("getMovieData ", res);
                movie = response.body();
                loadMovieData();
            }

            @Override
            public void onFailure(@NonNull Call<MovieModel> call, @NonNull Throwable t) {
                Log.i("getMovieData Error", t.getMessage());
            }
        });

    }

    private void loadMovieData() {
        String mov_title;
        mov_title = AppConstants.SHOWS_TYPE.equals(TYPE) ? movie.getName() : movie.getTitle();
        title.setText(mov_title);
        release_date.setText(movie.getRelease_date());
        duration.setText(String.valueOf(movie.getRuntime()));
        overview.setText(movie.getOverview());
        String poster_full_url = AppConstants.ORIGINAL_PATH_PREFIX + movie.getBackdrop_path();
        Glide.with(this).load(poster_full_url).fitCenter().into(poster);
        addChip();
    }

    private void addChip() {
        for(int i = 0; i < movie.getGenres().length; i++) {
            String name = movie.getGenres()[i].getName();
            Chip lChip = new Chip(this);
            lChip.setText(name);
    //      lChip.setTextColor(getResources().getColor(R.color.rating_icon_light));
    //      lChip.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_bg));
            pChipGroup.addView(lChip, pChipGroup.getChildCount() - 1);
        }
    }
}
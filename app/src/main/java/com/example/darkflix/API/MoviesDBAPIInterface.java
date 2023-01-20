package com.example.darkflix.API;

import com.example.darkflix.Model.GenreModel;
import com.example.darkflix.Model.MovieModel;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesDBAPIInterface {

    @GET("{type}/{curr_state}")
    Call<ResponseBody> getSearchInfo(@Path("type") String type, @Path("curr_state") String state, @Query("api_key") String name);

    @GET("{type}/{curr_state}")
    Observable<ResponseBody> getSearchList(@Path("type") String type, @Path("curr_state") String state, @Query("api_key") String name);

    @GET("genre/movie/list")
    Observable<ResponseBody> getGenreListInfo(@Query("api_key") String name);

    @GET("genre/{id}/movies")
    Observable<ResponseBody> getMoviesByGenre(@Path("id") int id, @Query("api_key") String name);

    @GET("discover/movie")
    Observable<ResponseBody> getMoviesByReg(@Query("with_original_language") String language, @Query("api_key") String name);

    @GET("movie/{movie_id}")
    Call<MovieModel> getMovieDetails(@Path("movie_id") int id, @Query("api_key") String name);
}

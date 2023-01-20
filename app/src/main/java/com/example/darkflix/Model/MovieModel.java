package com.example.darkflix.Model;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class MovieModel {
    private String title, backdrop_path, overview, name, type, release_date;
    private int id, runtime;
    private float vote_average;
    private GenreModel[] genres;

    public GenreModel[] getGenres() {
        return genres;
    }

    public void setGenres(GenreModel[] genres) {
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    @NonNull
    @Override
    public String toString() {
        return "MovieModel{" +
                "title='" + title + '\'' +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", overview='" + overview + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", release_date='" + release_date + '\'' +
                ", id=" + id +
                ", runtime=" + runtime +
                ", vote_average=" + vote_average +
                ", genres=" + Arrays.toString(genres) +
                '}';
    }
}

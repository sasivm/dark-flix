package com.example.darkflix.Repository;

import android.util.Log;

import com.example.darkflix.Model.GenreModel;
import com.example.darkflix.Model.HomeCatModel;
import com.example.darkflix.Model.MovieSearchModel;
import java.util.ArrayList;

public class MovieSearchRepo {
    private static ArrayList<ArrayList<MovieSearchModel>> list = new ArrayList<>();

    public static ArrayList<ArrayList<MovieSearchModel>> getList() {
        return list;
    }

    public static void setList(ArrayList<ArrayList<MovieSearchModel>> list) {
        MovieSearchRepo.list = list;
        Log.i("setList: ls size on set", MovieSearchRepo.list.size() + "");
    }

    private static ArrayList<HomeCatModel> HOME_REQ_LIST = new ArrayList<>();
    private static ArrayList<GenreModel> GENRE_REQ_LIST = new ArrayList<>();

    public static void setHomeReqList(ArrayList<HomeCatModel> list) {
        MovieSearchRepo.HOME_REQ_LIST = list;
    }
    public static void setGenreReqList(ArrayList<GenreModel> list) {
        MovieSearchRepo.GENRE_REQ_LIST = list;
    }

    public static ArrayList<HomeCatModel> getHomeReqList() {
       return HOME_REQ_LIST;
    }
    public static ArrayList<GenreModel> getGenreReqList() {
       return GENRE_REQ_LIST;
    }
}

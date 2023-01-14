package com.example.darkflix.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.darkflix.Model.HomeCatModel;
import com.example.darkflix.Model.MovieSearchModel;
import com.example.darkflix.R;
import com.example.darkflix.Repository.MovieSearchRepo;

import java.util.ArrayList;
import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder>{
    Context activity;
    ArrayList<HomeCatModel> homeCatList;

    public GroupListAdapter(Context activity, ArrayList<HomeCatModel> homeCatList) {
        this.activity = activity;
        this.homeCatList = homeCatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home, parent, false);
        return new GroupListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupListAdapter.ViewHolder holder, int position) {
        String title = homeCatList.get(position).getListTitle();
        holder.listTitle.setText(title);

//        Here 2nd Recycle view called
        ArrayList<ArrayList<MovieSearchModel>> ls = MovieSearchRepo.getList();
        MovsRecyclerViewAdapter adapter = new MovsRecyclerViewAdapter(activity, ls.get(position));
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return homeCatList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView listTitle;
        LinearLayout header;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.cat_list_group_rv);
            listTitle = itemView.findViewById(R.id.cat_list_title);
            header = itemView.findViewById(R.id.cat_list_header);
            header.setVisibility(View.VISIBLE);
        }
    }
}

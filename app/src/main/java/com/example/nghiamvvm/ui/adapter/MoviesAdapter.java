package com.example.nghiamvvm.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nghiamvvm.AppConstants;
import com.example.nghiamvvm.R;
import com.example.nghiamvvm.data.MovieEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.CustomViewHolder> {

    private Activity activity;
    private List<MovieEntity> movies;
    public MoviesAdapter(Activity activity) {
        this.activity = activity;
        this.movies = new ArrayList<>();
    }

    @Override
    public MoviesAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(rootView);
        return viewHolder;
    }

    public void setItems(List<MovieEntity> movies) {
        int startPosition = this.movies.size();
        this.movies.addAll(movies);
        notifyItemRangeChanged(startPosition, movies.size());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public MovieEntity getItem(int position) {
        return movies.get(position);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.CustomViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    protected class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        public CustomViewHolder(View rootView) {
            super(rootView);
            this.imageView = rootView.findViewById(R.id.imageView);
            this.textView = rootView.findViewById(R.id.textView);
        }

        public void bindTo(MovieEntity movie) {
            String imagePath = String.format(AppConstants.IMAGE_URL, movie.getPosterPath());
            this.textView.setText(imagePath);
            Picasso.get().load(imagePath)
                    .into(this.imageView);
        }
    }
}

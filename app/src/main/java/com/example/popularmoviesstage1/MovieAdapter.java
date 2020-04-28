package com.example.popularmoviesstage1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesstage1.model.Movie;
import com.squareup.picasso.Picasso;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private Movie[] mMovieData;
    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler{
        void onClick(Movie selectedMovie);
    }

    public MovieAdapter(MovieAdapterOnClickHandler handler){
        mClickHandler = handler;
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForGridItem = R.layout.movies_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        final boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForGridItem, parent, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Movie currentMovie = mMovieData[position];

        Picasso.get()
                .load(currentMovie.getImage_URL())
                .error(R.drawable.ic_launcher_background)//lazy solution, can be replaced with proper image
                .fit()
                .into(holder.mMovieImageView);

        holder.mMovieImageView.setTooltipText(currentMovie.getTitle());
        /*  without adding this listener it the onClick does not work for some reason */
        holder.mMovieImageView.setOnClickListener(holder);
    }


    @Override
    public int getItemCount() {
        if(mMovieData == null) return 0;
        return mMovieData.length;
    }

    /**
     * method to set movie data without having to make a new adapter
     * @param movieData list of fetched movie data
     */
    public void setMovieData(Movie[] movieData){
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    /**
     * create custom ViewHolder
     */
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        final ImageView mMovieImageView;

        MovieAdapterViewHolder(View view){
            super(view);
            mMovieImageView = view.findViewById(R.id.iv_movie_thumbnail);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int gridPosition = getAdapterPosition();
            Movie selectedMovie = mMovieData[gridPosition];
            mClickHandler.onClick(selectedMovie);
        }
    }
}

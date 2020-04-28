package com.example.popularmoviesstage1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.popularmoviesstage1.model.Movie;
import com.example.popularmoviesstage1.utils.JsonUtils;
import com.example.popularmoviesstage1.utils.NetworkUtils;
import com.example.popularmoviesstage1.MovieAdapter.MovieAdapterOnClickHandler;


import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler{
    private static final String SEARCH_CRITERIA_POPULAR = "popular";
    private static final String POPULAR_TITLE = "Most Popular Movies";
    private static final String SEARCH_CRITERIA_RATING = "top_rated";
    private static final String HIGHEST_RATING_TITLE = "Highest Rated Movies";

    private static final String MOVIE_PARCELABLE_PARAM = "Movie";

    private static final int GRID_COLUMNS_PORTRAIT = 2;
    private static final int GRID_COLUMNS_LANDSCAPE = 3;

    private ProgressBar mLoadingPB;
    private TextView mErrorLoadingTV;

    private RecyclerView mMovieListRV;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* assign all necessary views */
        getViewIds();

        /* check for phone orientation and show depending grid columns */
        GridLayoutManager gridLayMng;
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridLayMng = new GridLayoutManager(this, GRID_COLUMNS_PORTRAIT);
        }
        else{
            gridLayMng = new GridLayoutManager(this, GRID_COLUMNS_LANDSCAPE);
        }

        mMovieListRV.setLayoutManager(gridLayMng);
        mMovieListRV.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mMovieListRV.setAdapter(mMovieAdapter);

        /* load movie data (popular titles as default search setting) */
        loadMovieData(SEARCH_CRITERIA_POPULAR, POPULAR_TITLE);
    }

    @Override
    public void onClick(Movie selectedMovie) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(MOVIE_PARCELABLE_PARAM, selectedMovie);
        startActivity(intentToStartDetailActivity);
    }

    /**
     * Initiating all views of the activity
     */
    private void getViewIds(){
        mLoadingPB = findViewById(R.id.pb_loading_bar_home);
        mErrorLoadingTV = findViewById(R.id.tv_error_loading_home);
        mMovieListRV = findViewById(R.id.recyclerView);
    }

    /**
     * start the task to look for the movies list
     * @param searchCriteria defines what JSON data (popular or rating) should be looked for
     */
    private void loadMovieData(String searchCriteria, String title){
        showMovieList();
        setTitle(title);
        new FetchMovieDataTask().execute(searchCriteria);
    }

    /**
     *  TV to let user know something went wrong and hide the recyclerview
     */
    private void showErrorMessage(){
        mErrorLoadingTV.setVisibility(View.VISIBLE);
        mMovieListRV.setVisibility(View.INVISIBLE);
    }

    /**
     * hide error message and show recyclerview
     */
    private void showMovieList(){
        mErrorLoadingTV.setVisibility(View.INVISIBLE);
        mMovieListRV.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu_home, menu);
        return true;
    }


    /**
     * determines what sort method was chosen and freshly loads the corresponding main activity
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        mErrorLoadingTV.setVisibility(View.INVISIBLE);

        if(item.getItemId() == R.id.action_sortByPopular){
            mMovieAdapter.setMovieData(null);
            loadMovieData(SEARCH_CRITERIA_POPULAR, POPULAR_TITLE);
            return true;
        }
        if(item.getItemId() == R.id.action_sortByRating){
            mMovieAdapter.setMovieData(null);
            loadMovieData(SEARCH_CRITERIA_RATING, HIGHEST_RATING_TITLE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    class FetchMovieDataTask extends AsyncTask<String, Void, Movie[]>{
        /**
         * show loading bar while movie list is gathered
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingPB.setVisibility(View.VISIBLE);
        }

        /**
         * get a string to determine what json should be looked at and extract
         * a list of movies from it
         * @param sortCriteria should be "popular" or "Rating
         * @return movies that could be fetched
         */
        @Override
        protected Movie[] doInBackground(String... sortCriteria) {

            if (sortCriteria.length == 0) {
                return null;
            }

            Movie[] movies = null;
            URL jSONQueryURL = NetworkUtils.buildJSONURL(sortCriteria[0]);

            try{
                String jSONData = NetworkUtils.getJSONQueryAsString(jSONQueryURL);

                try {
                    movies = JsonUtils.parseMovieData(jSONData);
                } catch (JSONException e){
                    e.printStackTrace();
                    return null;
                }
            }catch (IOException e){
                e.printStackTrace();
                return null;
            }

            return movies;
        }

        /**
         * if movies could be fetched, fill recyclerview otherwise show error message
         * @param movies movies that were fetched from the JSON data
         */
        @Override
        protected void onPostExecute(Movie[] movies) {
            mLoadingPB.setVisibility(View.INVISIBLE);

            if(movies != null){
                showMovieList();
                mMovieAdapter.setMovieData(movies);
            } else {
                showErrorMessage();
            }
        }
    }
}

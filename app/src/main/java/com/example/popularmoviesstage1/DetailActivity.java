package com.example.popularmoviesstage1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmoviesstage1.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    private final String MOVIE_PARCELABLE_PARAM = "Movie";

    private TextView mRelease_dateTV;
    private ImageView mImage_URL_IV;
    private TextView mRatingTV;
    private TextView mPlotTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getViewIds();

        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity != null){
            if(intentThatStartedThisActivity.hasExtra(MOVIE_PARCELABLE_PARAM)){
                Movie selectedMovie = intentThatStartedThisActivity
                                        .getParcelableExtra(MOVIE_PARCELABLE_PARAM);

                setTitle(Objects.requireNonNull(selectedMovie).getTitle());
                mRelease_dateTV.setText(selectedMovie.getRelease_date());
                mRatingTV.setText(String.valueOf(selectedMovie.getRating()));
                mPlotTV.setText(selectedMovie.getPlot());
                Picasso.get()
                        .load(selectedMovie.getImage_URL())
                        .error(R.drawable.ic_launcher_background)//lazy solution, can be replaced with proper image
                        .fit()
                        .into(mImage_URL_IV);
            }
        }
    }

    /**
     * Initiating all views of the activity
     */
    private void getViewIds() {
        mRelease_dateTV = findViewById(R.id.tv_movie_detail_release_date);
        mImage_URL_IV = findViewById(R.id.iv_movie_detail_image);
        mRatingTV = findViewById(R.id.tv_movie_detail_rating);
        mPlotTV = findViewById(R.id.tv_movie_detail_plot);
    }
}

package com.example.popularmoviesstage1.utils;

import com.example.popularmoviesstage1.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class JsonUtils {

    /**
     * gets the naked JSON string and iterates through every result to store
     * title, release date, rating, plot and poster image path into a array of type Movie
     *
     * @param rawJSONData naked JSON string
     * @return array of extracted movies
     * @throws JSONException when things go wrong we want the program to throw an exception
     */
    public static Movie[] parseMovieData(String rawJSONData) throws JSONException {
        final String TMDB_RESULTS = "results";

        final String TMDB_TITLE = "title";
        final String TMDB_RELEASE_DATE = "release_date";
        final String TMDB_IMAGE_PATH = "poster_path";
        final String TMDB_RATING = "vote_average";
        final String TMDB_PLOT = "overview";

        final String JSON_MESSAGE_CODE = "code";

        Movie[] movies = null;

        JSONObject movieQueryJSON = new JSONObject(rawJSONData);

        /* check if JSON was received OK */
        if (movieQueryJSON.has(JSON_MESSAGE_CODE)){
            int errorCode = movieQueryJSON.getInt(JSON_MESSAGE_CODE);

            switch(errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    /* if not OK return empty list*/
                    return null;
            }
        }

        JSONArray movieResultArray = movieQueryJSON.optJSONArray(TMDB_RESULTS);
        if (movieResultArray != null && movieResultArray.length() > 0){
            movies = new Movie[movieResultArray.length()];

            for(int i = 0; i < movieResultArray.length(); i++){
                Movie extractedSingleMovie = new Movie();
                JSONObject currentJSONMovie = movieResultArray.optJSONObject(i);

                extractedSingleMovie.setTitle(currentJSONMovie.optString(TMDB_TITLE));
                extractedSingleMovie.setRelease_date(currentJSONMovie.optString(TMDB_RELEASE_DATE));
                extractedSingleMovie.setImage_URL(NetworkUtils.buildImgURL(
                        currentJSONMovie.optString(TMDB_IMAGE_PATH)).toString());
                extractedSingleMovie.setRating(currentJSONMovie.optInt(TMDB_RATING));
                extractedSingleMovie.setPlot(currentJSONMovie.optString(TMDB_PLOT));

                movies[i] = extractedSingleMovie;
            }
        }

        return movies;
    }
}

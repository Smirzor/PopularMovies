package com.example.popularmoviesstage1.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String THE_MOVIE_DB_IMAGE_BASE_URL = "https://image.tmdb.org/";
    private static final String IMAGE_PATH_QUERY_T = "t";
    private static final String IMAGE_PATH_QUERY_P = "p";
    private static final String IMAGE_QUALITY_PARAM = "w185";

    private static final String THE_MOVIE_DB_JSON_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY_PARAM = "api_key";

    //TODO: Insert API key here
    private static final String API_KEY = "--> Insert API key here <---";

    /**
     * This method creates a url based on a search criteria
     *
     * @param sortCriteria string to decide what JSON request we send
     * @return a finished URL that points to the corresponding JSON
     */
    public static URL buildJSONURL(String sortCriteria){
        Uri tempURI = Uri.parse(THE_MOVIE_DB_JSON_BASE_URL).buildUpon()
                         .appendPath(sortCriteria)
                         .appendQueryParameter(API_KEY_PARAM, API_KEY)
                         .build();

        URL url = null;
        try{
            url = new URL(tempURI.toString());
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }


    /**
     * Builds URL with given base URL and one parameter/query
     * @param param query string
     * @return finished URL
     */
    public static URL buildImgURL(String param){
        Uri tempURI = Uri.parse(THE_MOVIE_DB_IMAGE_BASE_URL).buildUpon()
                         .appendPath(IMAGE_PATH_QUERY_T)
                         .appendPath(IMAGE_PATH_QUERY_P)
                         .appendPath(IMAGE_QUALITY_PARAM)
                         .appendEncodedPath(param)
                         .build();

        URL url = null;
        try{
            url = new URL(tempURI.toString());
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }


    /**
     * scans a website and returns its content as string
     * @param url that should be downloaded
     * @return the downloaded website
     * @throws IOException if things go pete tong!
     */
    public static String getJSONQueryAsString(URL url) throws IOException{
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try{
            InputStream stream = conn.getInputStream();
            Scanner scanner = new Scanner(stream);
            // scan everything
            scanner.useDelimiter("\\A");

            // if the site is empty return null
            boolean notEmpty = scanner.hasNext();
            if (notEmpty){
                return scanner.next();
            } else{
                return null;
            }
        } finally {
            // if we do not do this people will die!
            conn.disconnect();
        }
    }
}

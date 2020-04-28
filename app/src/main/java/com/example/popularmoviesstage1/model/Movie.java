package com.example.popularmoviesstage1.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String title;
    private String release_date;
    private String image_URL;
    private double rating;
    private String plot;

    public Movie(){}

    // never used, just in case
    public Movie (String title, String release_date, String image_URL, double rating, String plot){
        this.title = title;
        this.release_date = release_date;
        this.image_URL = image_URL;
        this.rating = rating;
        this.plot = plot;
    }

    private Movie(Parcel in) {
        title = in.readString();
        release_date = in.readString();
        image_URL = in.readString();
        rating = in.readDouble();
        plot = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle(){ return title; }
    public void setTitle(String title){ this.title = title; }

    public String getRelease_date() { return release_date; }
    public void setRelease_date(String release_date){this.release_date = release_date; }

    public String getImage_URL(){ return image_URL; }
    public void setImage_URL(String image_URL){ this.image_URL = image_URL; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getPlot() { return plot; }
    public void setPlot(String plot) { this.plot = plot; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(release_date);
        dest.writeString(image_URL);
        dest.writeDouble(rating);
        dest.writeString(plot);
    }
}

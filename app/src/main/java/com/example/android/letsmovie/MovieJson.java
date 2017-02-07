package com.example.android.letsmovie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ILIAS on 28/1/2017.
 */

public class MovieJson implements Parcelable {
    private String MovieTitle;
    private String PosterPath;
    private String MovieOverview;
    private String UserRating;
    private String ReleaseDate;


    public MovieJson(String originalTitle,String overview,String userRating,String releaseDate,String posterPath){
        this.MovieTitle = originalTitle;
        this.MovieOverview = overview;
        this.UserRating = userRating;
        this.ReleaseDate = releaseDate;
        this.PosterPath = posterPath;
    }

    private MovieJson(Parcel in){
        MovieTitle = in.readString();
        MovieOverview = in.readString();
        UserRating = in.readString();
        ReleaseDate = in.readString();
        PosterPath = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(MovieTitle);
        out.writeString(MovieOverview);
        out.writeString(UserRating);
        out.writeString(ReleaseDate);
        out.writeString(PosterPath);
    }


    public static final Parcelable.Creator<MovieJson> CREATOR = new Parcelable.Creator<MovieJson>() {
        public MovieJson createFromParcel(Parcel in) {
            return new MovieJson(in);
        }

        public MovieJson[] newArray(int size) {

            return new MovieJson[size];
        }
    };

    public String getMovieTitle() {

        return MovieTitle;
    }

    public String getMovieOverview() {

        return MovieOverview;
    }

    public String getUserRating() {

        return UserRating;
    }

    public String getReleaseDate() {

        return ReleaseDate;
    }

    public String getPosterPath() {
        final String BASE_URL = "http://image.tmdb.org/t/p/";
        final String SIZE = "w500";
        return BASE_URL+SIZE+"/"+PosterPath ;

    }
}

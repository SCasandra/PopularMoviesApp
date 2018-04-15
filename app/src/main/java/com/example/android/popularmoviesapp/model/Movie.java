package com.example.android.popularmoviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Casi on 4/15/2018.
 */

public class Movie implements Parcelable {

    private String title;
    private String releaseDate;
    private String poster;
    private double voteAverage;
    private String overview;
    private int id;
    private String video_key;
    private List<Review> review;

    protected Movie(Parcel in) {
        title = in.readString();
        releaseDate = in.readString();
        poster = in.readString();
        voteAverage = in.readDouble();
        overview = in.readString();
        id = in.readInt();
        video_key = in.readString();
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

    public String getVideo_key() {
        return video_key;
    }

    public void setVideo_key(String video_key) {
        this.video_key = video_key;
    }

    public List<Review> getReview() {
        return review;
    }

    public void setReview(List<Review> review) {
        this.review = review;
    }

    public Movie(String title, String releaseDate, String poster, double voteAverage, String overview, int id) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(poster);
        parcel.writeDouble(voteAverage);
        parcel.writeString(overview);
        parcel.writeInt(id);
        parcel.writeString(video_key);

    }
}
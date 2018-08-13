package tz.co.neelansoft.cinegallery.library;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{

    private int id;
    private String title;
    private String release_date;
    private String poster_url;
    private String synopsis;
    private String original_title;


    private String original_language;
    private int vote_count;
    private float vote_average;
    private float popularity;
    private boolean isAdult;

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    private String backdrop;

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }



    //no argument constructor
    public Movie(){

    }
    //public constructor
    public Movie(int id, String title, String release_date, String poster_url,int votes,float vote_average,float popularity, String synopsis){
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.poster_url = poster_url;
        this.vote_count = votes;
        this.vote_average = vote_average;
        this.popularity = popularity;
        this.synopsis = synopsis;

    }

    public String getOriginalTitle() {
        return original_title;
    }

    public void setOriginalTitle(String original_title) {
        this.original_title = original_title;
    }

    public String getOriginalLanguage() {
        return original_language;
    }

    public void setOriginalLanguage(String original_language) {
        this.original_language = original_language;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public int getVoteCount() {
        return vote_count;
    }

    public void setVoteCount(int vote_count) {
        this.vote_count = vote_count;
    }

    public float getVoteAverage() {
        return vote_average;
    }

    public void setVoteAverage(float vote_average) {
        this.vote_average = vote_average;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }


    public String getPosterUrl() {
        return poster_url;
    }

    public void setPosterUrl(String poster_url) {
        this.poster_url = poster_url;
    }

    public int getId() {
        return id;

    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public void setReleaseDate(String release_date) {
        this.release_date = release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(vote_count);
        dest.writeFloat(vote_average);
        dest.writeFloat(popularity);
        dest.writeString(title);
        dest.writeString(original_title);
        dest.writeString(original_language);
        dest.writeString(release_date);
        dest.writeString(synopsis);
        dest.writeString(poster_url);
        dest.writeString(backdrop);
        dest.writeByte((byte) (isAdult ? 1: 0));

    }
    //private constructor with parcel argument
    private Movie(Parcel parcel){
        this.id = parcel.readInt();
        this.vote_count = parcel.readInt();
        this.vote_average = parcel.readFloat();
        this.popularity = parcel.readFloat();
        this.title = parcel.readString();
        this.original_title = parcel.readString();
        this.original_language = parcel.readString();
        this.release_date = parcel.readString();
        this.synopsis = parcel.readString();
        this.poster_url = parcel.readString();
        this.backdrop = parcel.readString();
        this.isAdult = parcel.readByte() != 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

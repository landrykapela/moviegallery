package tz.co.neelansoft.cinegallery.library;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
@Entity(tableName = "movie")
public class Movie implements Parcelable{

    @PrimaryKey
    private int id;
    private String title;
    private String releaseDate;
    private String posterUrl;
    private String synopsis;
    private String originalTitle;


    private String originalLanguage;
    private int voteCount;
    private float voteAverage;
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
    public Movie(int id, String title, String releaseDate, String posterUrl,int votes,float voteAverage,float popularity, String synopsis){
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
        this.voteCount = votes;
        this.voteAverage = voteAverage;
        this.popularity = popularity;
        this.synopsis = synopsis;

    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }


    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
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
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(voteCount);
        dest.writeFloat(voteAverage);
        dest.writeFloat(popularity);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(releaseDate);
        dest.writeString(synopsis);
        dest.writeString(posterUrl);
        dest.writeString(backdrop);
        dest.writeByte((byte) (isAdult ? 1: 0));

    }
    //private constructor with parcel argument
    private Movie(Parcel parcel){
        this.id = parcel.readInt();
        this.voteCount = parcel.readInt();
        this.voteAverage = parcel.readFloat();
        this.popularity = parcel.readFloat();
        this.title = parcel.readString();
        this.originalTitle = parcel.readString();
        this.originalLanguage = parcel.readString();
        this.releaseDate = parcel.readString();
        this.synopsis = parcel.readString();
        this.posterUrl = parcel.readString();
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

package tz.co.neelansoft.cinegallery.library;

public class Movie {

    private int id;
    private String title;
    private String release_date;
    private String poster_url;
    private String synopsis;
    private int vote_count;
    private float vote_average;
    private float popularity;


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

}

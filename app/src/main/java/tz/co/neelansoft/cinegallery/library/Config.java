package tz.co.neelansoft.cinegallery.library;

public class Config {

    private static final String API_KEY = "";
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String DEFAULT_SORT = "popularity.desc";
    private static final String DEFAULT_QUERY = "/discover/movie?page=1&include_video=false&include_adult=false&language=en-US";
    public static final String DEFAULT_REQUEST_URL = BASE_URL + DEFAULT_QUERY + "&sort_by=" + DEFAULT_SORT + "&api_key=" + API_KEY;
    public static final String STANDARD_REQUEST_URL = BASE_URL + DEFAULT_QUERY + "&api_key=" + API_KEY;
    public static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String POSTER_SIZE_DEFAULT = "w185/";
    public static final String POSTER_SIZE_WIDE = "w342/";

}

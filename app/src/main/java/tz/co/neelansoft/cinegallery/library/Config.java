package tz.co.neelansoft.cinegallery.library;

import android.content.Context;

import tz.co.neelansoft.cinegallery.R;

public class Config {

    private static final String API_KEY = "";
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String DEFAULT_SORT = "popularity.desc";
    private static final String DEFAULT_QUERY = "/discover/movie?page=1&include_video=false&include_adult=true&language=en-US";
    public static final String MOVIE_URL= BASE_URL+"/movie/";
    public static final String API_QUERY = "?api_key=" + API_KEY;
    public static final String DEFAULT_REQUEST_URL = BASE_URL + DEFAULT_QUERY + "&sort_by=" + DEFAULT_SORT + "&api_key=" + API_KEY;
    public static final String STANDARD_REQUEST_URL = BASE_URL + DEFAULT_QUERY + "&api_key=" + API_KEY;
    public static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String POSTER_SIZE_DEFAULT = "w185/";
    public static final String POSTER_SIZE_WIDE = "w342/";
    public static final String YOUTUBE_BASE_URL = "https://youtube.com/watch?v=";

    public static String getLanguage(Context context, String code){
        String language = "Unknwon";
        String[] codes = context.getResources().getStringArray(R.array.lang_codes);
        String[] lang = context.getResources().getStringArray(R.array.language);

        for(int i = 0; i < codes.length; i++){
            if(codes[i].equalsIgnoreCase(code)){
                language = lang[i];
                break;
            }
        }
        return language;
    }
}

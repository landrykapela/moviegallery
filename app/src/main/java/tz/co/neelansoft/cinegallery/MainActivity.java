package tz.co.neelansoft.cinegallery;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import tz.co.neelansoft.cinegallery.library.CustomGridAdapter;
import tz.co.neelansoft.cinegallery.library.JSONParser;
import tz.co.neelansoft.cinegallery.library.Movie;
import tz.co.neelansoft.cinegallery.library.MovieDatabase;
import tz.co.neelansoft.cinegallery.library.MovieExecutors;

import static tz.co.neelansoft.cinegallery.library.Config.DEFAULT_REQUEST_URL;
import static tz.co.neelansoft.cinegallery.library.Config.POSTER_BASE_URL;
import static tz.co.neelansoft.cinegallery.library.Config.POSTER_SIZE_DEFAULT;
import static tz.co.neelansoft.cinegallery.library.Config.POSTER_SIZE_WIDE;
import static tz.co.neelansoft.cinegallery.library.Config.STANDARD_REQUEST_URL;

public class MainActivity extends AppCompatActivity implements CustomGridAdapter.OnImageClickListener{
    private static final String TAG = "MainActivity";

    private static CustomGridAdapter mGridAdapter;
    private static GridView mGridView;
    private static ProgressBar mProgressBar;
    private TextView mGridHeading;
    private static ImageView mImageReload;
    private static int mColumnCount = 2;
    private static List<Movie> mMovies = new ArrayList<>();
    private static boolean success = true;
    private static String mUrlToFetch;

    private MovieDatabase mMovieDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mGridView = findViewById(R.id.gridview);

        mProgressBar = findViewById(R.id.progressBar);
        mGridHeading = findViewById(R.id.tv_grid_heading);
        mImageReload = findViewById(R.id.iv_reload);

        mMovieDatabase = MovieDatabase.getDatabaseInstance(this);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) mColumnCount = 3;
        else mColumnCount = 2;

        mGridAdapter = new CustomGridAdapter(this);
        if(savedInstanceState != null){
            mMovies = savedInstanceState.getParcelableArrayList("movies");
            mGridHeading.setText(savedInstanceState.getString("heading"));
            mGridAdapter.setMovieList(mMovies);
            mGridView.setNumColumns(mColumnCount);
            mGridView.setVerticalSpacing(10);
            mGridView.setHorizontalSpacing(10);
            mGridView.setAdapter(mGridAdapter);
            mGridAdapter.notifyDataSetChanged();
        }
        else{

            new MoviesAsyncTask().execute(DEFAULT_REQUEST_URL);
            if(success){
                updateUI(mMovies);
            }

        }
Log.e(TAG,"It loaded: "+mMovies.size());

        mImageReload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.e(TAG,"fetch url: "+mUrlToFetch);
                reload(mUrlToFetch);
            }
        });

    }

    private void reload(String url){
        new MoviesAsyncTask().execute(url);
        mImageReload.setVisibility(View.GONE);
        mGridAdapter.notifyDataSetChanged();
    }
    @Override
    public void onSaveInstanceState(Bundle out_bundle){

        mMovies = mGridAdapter.getMovies();
        out_bundle.putParcelableArrayList("movies",(ArrayList<Movie>)mMovies);
        out_bundle.putString("heading",mGridHeading.getText().toString());
        super.onSaveInstanceState(out_bundle);
    }
    @Override
    public void onImageClick(int itemPosition){
        Movie selectedMovie = mGridAdapter.getItem(itemPosition);
        showDetails(selectedMovie);
    }

    private void showDetails(Movie m){
        Intent details_intent = new Intent(this,DetailsActivity.class);
        details_intent.putExtra("movie",m);
        startActivity(details_intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int selectedItem = menuItem.getItemId();
        StringBuilder stringBuilder = new StringBuilder(STANDARD_REQUEST_URL);
        String heading = "";
        switch (selectedItem){
            case R.id.action_favorite:
                getFavorites();
                return true;
            case R.id.action_popular:
                stringBuilder.append("&sort_by=popularity.desc");
                heading = getResources().getString(R.string.popular_movies);
                break;
            case R.id.action_rate:
                stringBuilder.append("&sort_by=vote_average.desc");
                heading = getResources().getString(R.string.highly_movies);
                break;
            case R.id.action_coming:
                String date_today = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(new Date());
                stringBuilder.append("&primary_release_date.gte=").append(date_today);
                heading = getResources().getString(R.string.coming_soon);
                break;
            case R.id.action_cinema:
                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String y = today.split("-")[0];
                String m = today.split("-")[1];
                String d = today.split("-")[2];
                String last_month = String.valueOf(Integer.parseInt(m)-1);
                String last_month_date = y + "-" + last_month + "-" + d;
                stringBuilder.append("&primary_release_date.gte=").append(last_month_date).append("&primary_release_date.lte=").append(today);
                heading = getResources().getString(R.string.theatre_movies);
                break;
            default:
                stringBuilder.append("&sort_by=popularity.desc");
                heading = getResources().getString(R.string.popular_movies);
                break;
        }
        new MoviesAsyncTask().execute(stringBuilder.toString());
        mGridAdapter.notifyDataSetChanged();
        mGridHeading.setText(heading);
        return super.onOptionsItemSelected(menuItem);
    }

    private void getFavorites(){
        LiveData<List<Movie>> favoriteMovies = mMovieDatabase.movieDao().getFavoriteMovies();
        favoriteMovies.observe(MainActivity.this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mGridAdapter.setMovieList(movies);
                mGridAdapter.notifyDataSetChanged();
                mGridHeading.setText(R.string.favorite);
            }
        });
    }


    private static void showProgress(){
        mProgressBar.setVisibility(View.VISIBLE);

    }
    private static void hideProgress(){
        mProgressBar.setVisibility(View.GONE);

    }

    private static void updateUI(List<Movie> movies){
        mGridAdapter.setMovieList(movies);
        mGridView.setNumColumns(mColumnCount);
        mGridView.setVerticalSpacing(10);
        mGridView.setHorizontalSpacing(10);
        mGridView.setAdapter(mGridAdapter);
        mGridAdapter.notifyDataSetChanged();
        mImageReload.setVisibility(View.GONE);
    }
    static class MoviesAsyncTask extends AsyncTask<String,Void,String> {
        final List<Movie> movies = new ArrayList<>();
        String url_string = "";
    @Override
    protected String doInBackground(String... arg){
        url_string = arg[0];

        mUrlToFetch = url_string;
        try{
            URL url = new URL(url_string);
            try {
                String json = new JSONParser().getResponseFromHttpUrl(url);
                Log.d(TAG,"JSON: "+json);
                try{
                    JSONObject jo = new JSONObject(json);
                    JSONArray results = jo.getJSONArray("results");
                    if(results != null){

                        for(int i=0; i<results.length();i++){

                                JSONObject json_movie = results.getJSONObject(i);
                                int id = Integer.parseInt(json_movie.getString("id"));
                                String title = json_movie.getString("title");
                                String release_date = json_movie.getString("release_date");
                                String poster_url = POSTER_BASE_URL + POSTER_SIZE_DEFAULT + json_movie.getString("poster_path");
                                int votes = Integer.parseInt(json_movie.getString("vote_count"));
                                float vote_average = Float.parseFloat(json_movie.getString("vote_average"));
                                float popularity = Float.parseFloat(json_movie.getString("popularity"));
                                String overview = json_movie.getString("overview");
                                String original_title = json_movie.getString("original_title");
                                String original_lang = json_movie.getString("original_language");


                                Movie movie = new Movie(id, title, release_date, poster_url,votes,vote_average,popularity, overview);
                                movie.setOriginalLanguage(original_lang);
                                movie.setOriginalTitle(original_title);

                                boolean isAdult = json_movie.getBoolean("adult");

                                if(json_movie.getString("backdrop_path")!= null) {
                                    movie.setBackdrop(POSTER_BASE_URL+POSTER_SIZE_WIDE+json_movie.getString("backdrop_path"));
                                }
                                else {
                                    movie.setBackdrop(POSTER_BASE_URL+POSTER_SIZE_WIDE+json_movie.getString("poster_path"));
                                }
                                movie.setAdult(isAdult);
                                movies.add(movie);

                        }
                        success = true;
                        mMovies = movies;
                    }
                }
                catch (JSONException je){
                    je.printStackTrace();
                    Log.e(TAG,"Bad JSON",je);
                    success = false;
                    return null;
                }


            }
            catch (IOException e){
                Log.e(TAG,"Error in handling request url:"+url_string,e);
                success = false;
                return null;
            }
        }
        catch (MalformedURLException e){
            Log.e(TAG,"Error in url "+url_string,e);
            success = false;
            return null;
        }
        return null;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        showProgress();
    }

    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);
        hideProgress();

        if(success) {

            updateUI(movies);
        }
        else{
            mImageReload.setVisibility(View.VISIBLE);
        }

    }

}
}

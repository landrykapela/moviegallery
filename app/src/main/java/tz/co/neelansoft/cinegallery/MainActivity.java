package tz.co.neelansoft.cinegallery;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
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
import java.util.Scanner;

import tz.co.neelansoft.cinegallery.library.CustomGridAdapter;
import tz.co.neelansoft.cinegallery.library.Movie;

import static tz.co.neelansoft.cinegallery.library.Config.DEFAULT_REQUEST_URL;
import static tz.co.neelansoft.cinegallery.library.Config.POSTER_BASE_URL;
import static tz.co.neelansoft.cinegallery.library.Config.POSTER_SIZE_DEFAULT;
import static tz.co.neelansoft.cinegallery.library.Config.POSTER_SIZE_WIDE;
import static tz.co.neelansoft.cinegallery.library.Config.STANDARD_REQUEST_URL;

public class MainActivity extends AppCompatActivity implements CustomGridAdapter.OnImageClickListener{
    private static final String TAG = "MainActivity";

    private CustomGridAdapter mGridAdapter;
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private TextView mGridHeading;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mGridView = findViewById(R.id.gridview);

        mProgressBar = findViewById(R.id.progressBar);
        mGridHeading = findViewById(R.id.tv_grid_heading);

        mGridAdapter = new CustomGridAdapter(this,this);

        new DataLoader().execute(DEFAULT_REQUEST_URL);
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
        switch (selectedItem){
            case R.id.action_popular:
                mGridHeading.setText(getResources().getString(R.string.popular_movies));
                break;
            case R.id.action_rate:
                mGridHeading.setText(getResources().getString(R.string.highly_movies));
                break;
            case R.id.action_coming:
                mGridHeading.setText(getResources().getString(R.string.coming_soon));
                break;
            case R.id.action_cinema:
                mGridHeading.setText(getResources().getString(R.string.theatre_movies));
                break;
            default:
                mGridHeading.setText(getResources().getString(R.string.popular_movies));

        }
        loadData(selectedItem);
        return super.onOptionsItemSelected(menuItem);
    }

    private void loadData(int filter){
        StringBuilder stringBuilder = new StringBuilder(STANDARD_REQUEST_URL);
        switch (filter) {
            case R.id.action_popular:
                stringBuilder.append("&sort_by=popularity.desc");
                break;
            case R.id.action_rate:
                stringBuilder.append("&sort_by=vote_average.desc");
                break;
            case R.id.action_cinema:
                String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String y = today.split("-")[0];
                String m = today.split("-")[1];
                String d = today.split("-")[2];
                String last_month = String.valueOf(Integer.parseInt(m)-1);
                String last_month_date = y + "-" + last_month + "-" + d;
                stringBuilder.append("&primary_release_date.gte=").append(last_month_date).append("&primary_release_date.lte=").append(today);
                Log.e(TAG,stringBuilder.toString());
                break;
            case R.id.action_coming:
                String date_today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                stringBuilder.append("&primary_release_date.gte=").append(date_today);
                Log.e(TAG,stringBuilder.toString());
                break;
            default:
                stringBuilder.append("&sort_by=popularity.desc");
                break;
        }
        new DataLoader().execute(stringBuilder.toString());
        synchronized (mGridAdapter) {
            mGridAdapter.notifyAll();
        }
    }
    private static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

    private void showProgress(){
        mProgressBar.setVisibility(View.VISIBLE);

    }
    private void hideProgress(){
        mProgressBar.setVisibility(View.GONE);

    }

    class DataLoader extends AsyncTask<String,Void,String> {
        final List<Movie> movies = new ArrayList<>();
    @Override
    protected String doInBackground(String... arg){
        String url_string = arg[0];
        try{
            URL url = new URL(url_string);
            try {
                String json = getResponseFromHttpUrl(url);
                Log.e(TAG,"JSON: "+json);
                try{
                    JSONObject jo = new JSONObject(json);
                    JSONArray results = jo.getJSONArray("results");
                    if(results != null){
                        Log.e(TAG,"size: "+results.length());
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
                                Movie movie = new Movie(id, title, release_date, poster_url,votes,vote_average,popularity, overview);
                                boolean isAdult = Boolean.getBoolean(json_movie.getString("adult"));
                                if(json_movie.getString("backdrop_path")!= null) {
                                    movie.setBackdrop(POSTER_BASE_URL+POSTER_SIZE_WIDE+json_movie.getString("backdrop_path"));
                                }
                                else {
                                    movie.setBackdrop(POSTER_BASE_URL+POSTER_SIZE_WIDE+json_movie.getString("poster_path"));
                                    Log.e(TAG,"Poster: "+json_movie.getString("poster_path"));
                                }
                                movie.setAdult(isAdult);
                                movies.add(movie);

                        }
                    }
                }
                catch (JSONException je){
                    je.printStackTrace();
                    Log.e(TAG,"Bad JSON",je);
                    return null;
                }


            }
            catch (IOException e){
                Log.e(TAG,"Error in handling request",e);
                return null;
            }
        }
        catch (MalformedURLException e){
            Log.e(TAG,"Error in url",e);
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

        mGridAdapter.setMovieList(movies);
        mGridView.setNumColumns(2);
        mGridView.setVerticalSpacing(10);
        mGridView.setHorizontalSpacing(10);
        mGridView.setAdapter(mGridAdapter);

    }

}
}

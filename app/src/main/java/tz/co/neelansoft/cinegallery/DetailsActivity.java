package tz.co.neelansoft.cinegallery;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tz.co.neelansoft.cinegallery.library.Config;
import tz.co.neelansoft.cinegallery.library.JSONParser;
import tz.co.neelansoft.cinegallery.library.Movie;
import tz.co.neelansoft.cinegallery.library.MovieDatabase;
import tz.co.neelansoft.cinegallery.library.MovieExecutors;
import tz.co.neelansoft.cinegallery.library.Review;
import tz.co.neelansoft.cinegallery.library.ReviewsAdapter;
import tz.co.neelansoft.cinegallery.library.ScreenDimensions;
import tz.co.neelansoft.cinegallery.library.Trailer;
import tz.co.neelansoft.cinegallery.library.TrailersAdapter;

import static tz.co.neelansoft.cinegallery.library.Config.API_QUERY;
import static tz.co.neelansoft.cinegallery.library.Config.MOVIE_URL;
import static tz.co.neelansoft.cinegallery.library.Config.YOUTUBE_BASE_URL;

public class DetailsActivity extends AppCompatActivity implements TrailersAdapter.OnTrailerClickListener{
    private static final String TAG = "DetailsActivity";
    private ImageView mImageFavorite;
    private TextView mTextNoReview;
    private TextView mTextNoTrailer;

    private RecyclerView mRecyclerViewReviews;
    private ReviewsAdapter mReviewsAdapter;
    private RecyclerView mRecyclerViewTrailers;
    private TrailersAdapter mTrailersAdapter;
    private final List<Review> mReviews = new ArrayList<>();
    private final List<Trailer> mTrailers = new ArrayList<>();

    private MovieDatabase mDatabase;
    private boolean isFavorite;
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        ImageView mImageBadge = findViewById(R.id.iv_badge);
        ImageView mImagePoster = findViewById(R.id.iv_backdrop);
        mImageFavorite = findViewById(R.id.iv_favorite);
        TextView mTextOverview = findViewById(R.id.tv_overview);
        TextView mTextTitle = findViewById(R.id.tv_title);
        TextView mTextOriginalLanguage = findViewById(R.id.tv_original_language);
        TextView mTextOriginalTitle = findViewById(R.id.tv_original_title);
        TextView mTextOfficialReleaseDate = findViewById(R.id.tv_release_date);
        mTextNoReview = findViewById(R.id.tv_no_review);
        mTextNoTrailer = findViewById(R.id.tv_no_trailer);
        RatingBar mRatingBar = findViewById(R.id.ratingBar);
        mRecyclerViewReviews = findViewById(R.id.rv_review);
        mRecyclerViewTrailers = findViewById(R.id.rv_trailer);

        mReviewsAdapter = new ReviewsAdapter(this,mReviews);
        mTrailersAdapter = new TrailersAdapter(this,this);
        ScreenDimensions sd = new ScreenDimensions(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        if(sd.isMedium()){

            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
            }
            else{
                layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
            }
        }
        else{
            layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        }
        mRecyclerViewReviews.setHasFixedSize(true);
        mRecyclerViewReviews.setLayoutManager(layoutManager);
        mRecyclerViewTrailers.setLayoutManager(layoutManager2);

        mDatabase = MovieDatabase.getDatabaseInstance(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final Movie myMovie = (extras != null) ? (Movie) extras.getParcelable("movie") : null;
            if( myMovie != null){
                movieId = myMovie.getId();
                String movieBackdrop = myMovie.getBackdrop();
                if( movieBackdrop != null && !movieBackdrop.contains("null")) Picasso.with(this).load(myMovie.getBackdrop()).into(mImagePoster);
                else mImagePoster.setImageResource(R.drawable.tmdb);

                    mRatingBar.setRating(myMovie.getVoteAverage()/2);
                    mTextTitle.setText(myMovie.getTitle());
                    mTextOverview.setText(myMovie.getSynopsis());

                    mTextOfficialReleaseDate.setText(myMovie.getReleaseDate());
                    mTextOriginalLanguage.setText(Config.getLanguage(this,myMovie.getOriginalLanguage()));
                    mTextOriginalTitle.setText(myMovie.getOriginalTitle());


                    int badge;
                    if(myMovie.isAdult()){
                        badge = R.mipmap.age_badge;
                    }
                    else {
                        badge = R.mipmap.age_badge_pg;
                    }
                mImageBadge.setImageResource(badge);
                LiveData<Movie> favMovie = mDatabase.movieDao().getMovieWithId(myMovie.getId());
                favMovie.observe(DetailsActivity.this, new Observer<Movie>() {
                    @Override
                    public void onChanged(@Nullable Movie movie) {
                        if(movie != null){
                            if(myMovie.getId() == movie.getId()){
                            mImageFavorite.setImageResource(R.mipmap.ic_favorite_colored);
                            isFavorite = true;
                            }
                            else{
                                mImageFavorite.setImageResource(R.mipmap.ic_favorite_clear);
                                isFavorite = false;

                            }
                        }
                        else{
                            mImageFavorite.setImageResource(R.mipmap.ic_favorite_clear);
                            isFavorite = false;

                        }
                    }
                });

            }

            mImageFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(isFavorite){
                       makeFavorite(myMovie,false);
                       mImageFavorite.setImageResource(R.mipmap.ic_favorite_clear);
                   }
                   else{
                       makeFavorite(myMovie,true);
                       mImageFavorite.setImageResource(R.mipmap.ic_favorite_colored);
                   }

                }
            });

            getMovieTrailers(movieId);
            getMovieReviews(MOVIE_URL+movieId+"/reviews"+API_QUERY);
    }
    private void makeFavorite(final Movie movie, final boolean flag){
        MovieExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(flag) {
                    mDatabase.movieDao().addToFavorites(movie);
                }
                else{
                    mDatabase.movieDao().removeFromFavorites(movie);
                }
            }
        });
    }

    private void getMovieTrailers(int movie_id){
        String trailer_link = MOVIE_URL+movie_id+"/videos"+API_QUERY;
        new MovieTrailers().execute(trailer_link);
    }
    private void getMovieReviews(String link){
        new MovieReviews().execute(link);
    }

    @Override
    public void onTrailerClick(int positioin) {
        Trailer t = mTrailers.get(positioin);
        String video_link = YOUTUBE_BASE_URL+t.getKey();
        Intent watchVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_link));
        startActivity(Intent.createChooser(watchVideoIntent,getString(R.string.open_with)));
    }

    class MovieReviews extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... args){
            String url_string = args[0];
            try{
                URL url = new URL(url_string);
                try {
                    String json = new JSONParser().getResponseFromHttpUrl(url);
                    Log.e(TAG,"result: "+json);
                    try{
                        JSONObject jo_review = new JSONObject(json);
                            JSONArray ja = jo_review.getJSONArray("results");
                        if(ja != null){
                            for(int i=0;i< ja.length();i++){
                                JSONObject jo = ja.getJSONObject(i);
                                String author = jo.getString("author");
                                String content = jo.getString("content");

                                Review r = new Review(author,content);
                                mReviews.add(r);
                            }
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                        Log.e(TAG,"error in json object",e);
                        return null;
                    }
                    return json;
                }
                catch (IOException e){
                    e.printStackTrace();
                    Log.e(TAG,"Could not retreive json",e);
                    return null;
                }
            }
            catch(MalformedURLException e){
                e.printStackTrace();
                Log.e(TAG,"URL error");
                return null;
            }
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(mReviews.size() == 0){
                mTextNoReview.setVisibility(View.VISIBLE);
            }
            else{

                mTextNoReview.setVisibility(View.GONE);
            }
            mReviewsAdapter.setReviewList(mReviews);
            mRecyclerViewReviews.setAdapter(mReviewsAdapter);
            mReviewsAdapter.notifyDataSetChanged();
        }
    }

    class MovieTrailers extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... args){
            String url_string = args[0];
            try{
                URL url = new URL(url_string);
                try {
                    String json = new JSONParser().getResponseFromHttpUrl(url);
                    Log.e(TAG,"trailer result: "+json);
                   try{
                        JSONObject jo_review = new JSONObject(json);
                        JSONArray ja = jo_review.getJSONArray("results");
                        if(ja != null){
                            for(int i=0;i< ja.length();i++){
                                JSONObject jo = ja.getJSONObject(i);
                                String id = jo.getString("id");
                                String key = jo.getString("key");
                                String name = jo.getString("name");

                                Trailer t = new Trailer(id,key,name);
                                mTrailers.add(t);
                            }
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                        Log.e(TAG,"error in json object",e);
                        return null;
                    }
                    return json;
                }
                catch (IOException e){
                    e.printStackTrace();
                    Log.e(TAG,"Could not retreive json",e);
                    return null;
                }
            }
            catch(MalformedURLException e){
                e.printStackTrace();
                Log.e(TAG,"URL error");
                return null;
            }
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(mTrailers.size() == 0){
                mTextNoTrailer.setVisibility(View.VISIBLE);
            }
            else{

                mTextNoTrailer.setVisibility(View.GONE);
            }
            mTrailersAdapter.setTrailerList(mTrailers);
            mRecyclerViewTrailers.setAdapter(mTrailersAdapter);
            mTrailersAdapter.notifyDataSetChanged();
        }
    }
}

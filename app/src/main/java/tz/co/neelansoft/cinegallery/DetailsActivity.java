package tz.co.neelansoft.cinegallery;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import tz.co.neelansoft.cinegallery.library.Config;
import tz.co.neelansoft.cinegallery.library.Movie;
import tz.co.neelansoft.cinegallery.library.MovieDatabase;
import tz.co.neelansoft.cinegallery.library.MovieExecutors;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = "DetailsActivity";
    private ImageView mImageBadge;
    private ImageView mImagePoster;
    private ImageView mImageFavorite;
    private TextView mTextOverview;
    private TextView mTextTitle;
    private TextView mTextOriginalLanguage;
    private TextView mTextOriginalTitle;
    private TextView mTextOfficialReleaseDate;
    private RatingBar mRatingBar;

    private MovieDatabase mDatabase;
    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        mImageBadge = findViewById(R.id.iv_badge);
        mImagePoster = findViewById(R.id.iv_backdrop);
        mImageFavorite = findViewById(R.id.iv_favorite);
        mTextOverview = findViewById(R.id.tv_overview);
        mTextTitle = findViewById(R.id.tv_title);
        mTextOriginalLanguage = findViewById(R.id.tv_original_language);
        mTextOriginalTitle = findViewById(R.id.tv_original_title);
        mTextOfficialReleaseDate = findViewById(R.id.tv_release_date);
        mRatingBar = findViewById(R.id.ratingBar);

        mDatabase = MovieDatabase.getDatabaseInstance(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final Movie myMovie = (extras != null) ? (Movie) extras.getParcelable("movie") : null;
            if( myMovie != null){

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
                   Log.e(TAG,"Movie id:"+myMovie.getId());
                }
            });
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
}

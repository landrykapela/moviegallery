package tz.co.neelansoft.cinegallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import tz.co.neelansoft.cinegallery.library.Config;
import tz.co.neelansoft.cinegallery.library.Movie;

public class DetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        ImageView mImageBadge = findViewById(R.id.iv_badge);
        ImageView mImagePoster = findViewById(R.id.iv_backdrop);
        TextView mTextOverview = findViewById(R.id.tv_overview);
        TextView mTextTitle = findViewById(R.id.tv_title);
        TextView mTextOriginalLanguage = findViewById(R.id.tv_original_language);
        TextView mTextOriginalTitle = findViewById(R.id.tv_original_title);
        TextView mTextOfficialReleaseDate = findViewById(R.id.tv_release_date);
        RatingBar mRatingBar = findViewById(R.id.ratingBar);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Movie myMovie = (extras != null) ? (Movie) extras.getParcelable("movie") : null;
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
            }

    }
}

package tz.co.neelansoft.cinegallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import tz.co.neelansoft.cinegallery.library.Movie;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DetailsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        ImageView mImageBadge = findViewById(R.id.iv_badge);
        ImageView mImagePoster = findViewById(R.id.iv_backdrop);
        TextView mTextOverview = findViewById(R.id.tv_overview);
        TextView mTextTitle = findViewById(R.id.tv_title);
        RatingBar mRatingBar = findViewById(R.id.ratingBar);

        Intent intent = getIntent();
        if(intent.getExtras().getParcelable("movie") != null){
            Movie movie = intent.getExtras().getParcelable("movie");
            Log.e(TAG,"poster url: "+movie.getBackdrop());
            Picasso.with(this).load(movie.getBackdrop()).into(mImagePoster);
            mRatingBar.setRating(movie.getVoteAverage()/2);
            mTextTitle.setText(movie.getTitle());
            mTextOverview.setText(movie.getSynopsis());

            int badge = R.mipmap.age_badge;
            if(!movie.isAdult()) badge = R.mipmap.age_badge_pg;

            mImageBadge.setImageResource(badge);
        }
    }
}

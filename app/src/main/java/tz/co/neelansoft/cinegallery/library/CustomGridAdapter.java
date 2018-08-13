package tz.co.neelansoft.cinegallery.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tz.co.neelansoft.cinegallery.R;

public class CustomGridAdapter extends BaseAdapter {

    private List<Movie> movieList;
    private final Context context;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

    private final OnImageClickListener mImageClickListener;
    //public constructor
    public CustomGridAdapter(Context context, OnImageClickListener listener){
        this.context = context;
        this.mImageClickListener = listener;
    }

    public interface OnImageClickListener{
        void onImageClick(int itemPosition);
    }

    @Override
    public int getCount() {
        if(movieList != null){
            return movieList.size();
        }
        else return 0;
    }

    @Override
    public Movie getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public List<Movie> getMovies(){
        return movieList;
    }

    public void setMovieList(List<Movie> list){
        this.movieList = list;
    }
    private void loadImage(String url, ImageView imageView){
        Picasso.with(context).load(url).into(imageView);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.grid_item,parent,false);
        }

        Movie movie = getItem(position);
        ImageView poster = convertView.findViewById(R.id.iv_poster);
        TextView title = convertView.findViewById(R.id.tv_title);
        TextView vote = convertView.findViewById(R.id.tv_vote);

        vote.setText(String.valueOf(movie.getVoteAverage()));

        String year = dateFormat.format(new Date());
        String date_string = movie.getReleaseDate();
        if(!date_string.isEmpty()){
            year = date_string.split("-")[0];
        }

        title.setText(year);
//        vote.setText(String.valueOf(movie.getVoteAverage()));
        loadImage(movie.getPosterUrl(),poster);

        GridView gridView = parent.findViewById(R.id.gridview);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mImageClickListener.onImageClick(position);
            }
        });
        /*
        poster.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Movie selectedMovie = movieList.get(position);
                Toast.makeText(context,"movie id: "+selectedMovie.getId(),Toast.LENGTH_LONG).show();
            }
        });*/
        return convertView;
    }

}

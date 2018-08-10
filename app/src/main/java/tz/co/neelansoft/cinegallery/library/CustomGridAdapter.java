package tz.co.neelansoft.cinegallery.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import tz.co.neelansoft.cinegallery.R;

public class CustomGridAdapter extends BaseAdapter {

    private List<Movie> movieList;
    private final Context context;

    //public constructor
    public CustomGridAdapter(Context context){
        this.context = context;
       /// this.mImageClickListener = listener;
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
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.grid_item,parent,false);
        }

        Movie movie = getItem(position);
        ImageView poster = view.findViewById(R.id.iv_poster);
        TextView title = view.findViewById(R.id.tv_title);

        title.setText(movie.getTitle());
//        vote.setText(String.valueOf(movie.getVoteAverage()));
        loadImage(movie.getPosterUrl(),poster);
        poster.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Movie selectedMovie = movieList.get(position);
                Toast.makeText(context,"movie id: "+selectedMovie.getId(),Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

}

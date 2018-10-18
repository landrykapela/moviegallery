package tz.co.neelansoft.cinegallery.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tz.co.neelansoft.cinegallery.R;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

    private Context mContext;
    private List<Trailer> mTrailerList = new ArrayList<>();
    private OnTrailerClickListener mOnTrailerClickListener;

    public interface OnTrailerClickListener{
        void onTrailerClick(int positioin);
    }

    public TrailersAdapter(Context context,OnTrailerClickListener listener){
        this.mContext = context;
        this.mOnTrailerClickListener = listener;
    }

    public void setTrailerList(List<Trailer> trailers){
        this.mTrailerList = trailers;
    }
    public List<Trailer> getTrailerList(){
        return mTrailerList;
    }

    @Override
    public int getItemCount(){
        return mTrailerList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        holder.bind(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_item,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mTextTrailerName;
        ImageView mImagePlay;
        public ViewHolder(View itemView){
            super(itemView);
            mTextTrailerName = itemView.findViewById(R.id.tv_trailer_name);
            mImagePlay = itemView.findViewById(R.id.iv_play);
            mImagePlay.setOnClickListener(this);
        }
        public void bind(final int position){

            mTextTrailerName.setText(mTrailerList.get(position).getName());
        }
        @Override
        public void onClick(View v){
            int position = getAdapterPosition();
            mOnTrailerClickListener.onTrailerClick(position);
        }
    }
}

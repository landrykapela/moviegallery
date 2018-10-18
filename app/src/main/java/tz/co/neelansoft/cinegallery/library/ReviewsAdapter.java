package tz.co.neelansoft.cinegallery.library;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tz.co.neelansoft.cinegallery.R;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private final Context mContext;
    private List<Review> mReviewList;
    public ReviewsAdapter(Context context, List<Review> reviews){
        this.mContext = context;
        this.mReviewList = reviews;
    }
    public void setReviewList(List<Review> list){
        this.mReviewList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView mTextAuthor;
        private final TextView mTextContent;
        ViewHolder(View itemView) {
            super(itemView);

            mTextAuthor = itemView.findViewById(R.id.tv_author);
            mTextContent = itemView.findViewById(R.id.tv_content);
        }

        void bind(int position){
            Review review = mReviewList.get(position);
            mTextContent.setText(review.getContent());
            mTextAuthor.setText("- ".concat(review.getAuthor()));
        }
    }
}

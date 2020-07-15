package com.pertamina.musicoolpromo.view.adapter;

import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.Feed.NewsFeedResult;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
    private OnItemClickCallback onItemClickCallback;
    public static  final int POST_TYPE_0 = 0;
    public static  final int POST_TYPE_1 = 1;
    private List<NewsFeedResult> movies;
    private View emptyView;


    public FeedAdapter(List<NewsFeedResult> movies, View emptyView) {
        this.movies = movies;
        this.emptyView = emptyView;
    }

    public void updateEmptyView() {
        if (movies.size() == 0)
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.GONE);
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }


    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        if(position == POST_TYPE_0){
            View mViewTop = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_top_feed, viewGroup, false);
            return new FeedViewHolder(mViewTop);
        }else {
            View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_feed, viewGroup, false);
            return new FeedViewHolder(mView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder feedViewHolder, int position) {
        feedViewHolder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class FeedViewHolder extends RecyclerView.ViewHolder {

        ImageView feedImage;
        TextView feedTitle;
        TextView feedDescription;
        TextView feedDate;
        CardView cardView;

        FeedViewHolder(@NonNull View itemView) {

            super(itemView);
            feedImage = itemView.findViewById(R.id.image_feed);
            feedTitle = itemView.findViewById(R.id.title_feed);
            feedDescription = itemView.findViewById(R.id.description_feed);
            feedDate = itemView.findViewById(R.id.time_feed);
            cardView = itemView.findViewById(R.id.cardView);

        }

        void bind(NewsFeedResult feed) {

            Glide.with(itemView.getContext())
                    .load(feed.getImages())
                    .into(feedImage);


            Log.d("njeng", "bind: " + feed.getTitle());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                feedTitle.setText(Html.fromHtml(feed.getTitle(), Html.FROM_HTML_MODE_COMPACT));
                feedDescription.setText(Html.fromHtml(feed.getResume(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                feedTitle.setText(Html.fromHtml(feed.getResume()));
                feedDescription.setText(Html.fromHtml(feed.getResume()));
            }

            String string = feed.getCreated_at();
            String[] parts = string.split(" ");
            String part1 = parts[0]; // 004
            String part2 = parts[1]; // 034556

            feedDate.setText(part1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickCallback.onItemClicked(movies.get(getAdapterPosition()));
                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return POST_TYPE_0;
        }else {
            return POST_TYPE_1;
        }

    }


    public interface OnItemClickCallback {
        void onItemClicked(NewsFeedResult data);
    }
}
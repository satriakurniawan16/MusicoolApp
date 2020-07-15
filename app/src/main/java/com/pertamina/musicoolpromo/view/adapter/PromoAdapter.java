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
import com.pertamina.musicoolpromo.model.promo.PromoResult;

import java.util.List;

public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.PromoViewHolder> {
    private OnItemClickCallback onItemClickCallback;
    public static  final int POST_TYPE_0 = 0;
    public static  final int POST_TYPE_1 = 1;
    private List<PromoResult> promoList;
    private View emptyView;

    public PromoAdapter(List<PromoResult> promoList, View emptyView) {
        this.promoList = promoList;
        this.emptyView = emptyView;
    }

    public void updateEmptyView() {
        if (promoList.size() == 0)
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.GONE);
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }


    @NonNull
    @Override
    public PromoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        if(position == POST_TYPE_0){
            View mViewTop = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_top_feed, viewGroup, false);
            return new PromoViewHolder(mViewTop);
        }else {
            View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_feed, viewGroup, false);
            return new PromoViewHolder(mView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PromoViewHolder promoViewHolder, int position) {
        promoViewHolder.bind(promoList.get(position));
    }

    @Override
    public int getItemCount() {
        return promoList.size();
    }

    class PromoViewHolder extends RecyclerView.ViewHolder {

        ImageView feedImage;
        TextView feedTitle;
        TextView feedDescription;
        TextView feedDate;
        CardView cardView;

        PromoViewHolder(@NonNull View itemView) {

            super(itemView);
            feedImage = itemView.findViewById(R.id.image_feed);
            feedTitle = itemView.findViewById(R.id.title_feed);
            feedDescription = itemView.findViewById(R.id.description_feed);
            feedDate = itemView.findViewById(R.id.time_feed);
            cardView = itemView.findViewById(R.id.cardView);

        }

        void bind(PromoResult promoResult) {

            Glide.with(itemView.getContext())
                    .load(promoResult.getImages())
                    .into(feedImage);


            Log.d("njengpromo", "bind: " + promoResult.getTitle());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                feedDescription.setText(Html.fromHtml(promoResult.getDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                feedDescription.setText(Html.fromHtml(promoResult.getDescription()));
            }

            String string = promoResult.getCreated_at();
            String[] parts = string.split(" ");
            String part1 = parts[0]; // 004
            String part2 = parts[1]; // 034556

            feedTitle.setText(promoResult.getTitle());
            feedDate.setText(part1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickCallback.onItemClicked(promoList.get(getAdapterPosition()));
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
        void onItemClicked(PromoResult data);
    }
}
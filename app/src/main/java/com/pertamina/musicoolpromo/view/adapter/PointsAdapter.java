package com.pertamina.musicoolpromo.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.reward.RewardResult;
import com.pertamina.musicoolpromo.view.network.ApiClient;
import com.pertamina.musicoolpromo.view.network.ApiInterface;
import com.pertamina.musicoolpromo.view.utilities.SharePreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.PointsViewHolder> {

    private PointsAdapter.OnItemClickCallback onItemClickCallback;
    private List<RewardResult> rewards;
    private View mEmptyView;

    public PointsAdapter(List<RewardResult> rewards, View mEmptyView) {
        this.rewards = rewards;
        this.mEmptyView = mEmptyView;
    }

    public void updateEmptyView() {
        if (rewards.size() == 0)
            mEmptyView.setVisibility(View.VISIBLE);
        else
            mEmptyView.setVisibility(View.GONE);
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public PointsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View mViewTop = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_points, viewGroup, false);
        return new PointsViewHolder(mViewTop);
    }

    @Override
    public void onBindViewHolder(@NonNull PointsViewHolder pointsViewHolder, int position) {
        pointsViewHolder.bind(rewards.get(position));
    }

    @Override
    public int getItemCount() {
        return rewards.size();
    }

    class PointsViewHolder extends RecyclerView.ViewHolder {

        TextView point;
        TextView pointTitle;
        ImageView imagePoint;
        TextView pointsDescription;
        TextView rewardLeft;
        LinearLayout rootLayout;
        CardView cardView;

        PointsViewHolder(@NonNull View itemView) {
            super(itemView);
            pointTitle = itemView.findViewById(R.id.description_point);
            point = itemView.findViewById(R.id.text_point);
            pointsDescription = itemView.findViewById(R.id.valid_point);
            imagePoint = itemView.findViewById(R.id.image_reward);
            rewardLeft = itemView.findViewById(R.id.available_reward);
            rootLayout = itemView.findViewById(R.id.rootLinear);
            cardView = itemView.findViewById(R.id.rootCard);
        }

        void bind(RewardResult rewardResult) {

            SharePreferenceManager sharePreferenceManager = new SharePreferenceManager(itemView.getContext());
            ApiInterface service = ApiClient.getData().create(ApiInterface.class);
            Call<JsonObject> call = service.getRewardID("Bearer "+sharePreferenceManager.getToken(),rewardResult.getId());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject jsonObject = response.body();
                    Log.d("tampan", "onResponse: " + response.code() + response.message());
                    JsonObject imageObject = jsonObject.get("image_storage").getAsJsonObject();
                    Glide.with(itemView.getContext())
                            .load(imageObject.get("url").getAsString())
                            .into(imagePoint);

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("lol gagal", "onResponse: " + t);
                }
            });

            point.setText(rewardResult.getPoint_cost());
            pointTitle.setText(rewardResult.getName());
            SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                String reformattedStr = myFormat.format(fromUser.parse(rewardResult.getStart_date()));
                String reformattedStr2 = myFormat.format(fromUser.parse(rewardResult.getEnd_date()));
                pointsDescription.setText("Periode "+reformattedStr+" - "+ reformattedStr2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            rewardLeft.setText("Tersisa "+rewardResult.getQuantity());

            if(rewardResult.getQuantity().equals("0")){
                rootLayout.setVisibility(View.GONE);
                itemView.setVisibility(View.GONE);
                cardView.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickCallback.onItemClicked(rewards.get(getAdapterPosition()));
                }
            });

        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(RewardResult data);
    }
}

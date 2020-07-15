package com.pertamina.musicoolpromo.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.HistoryPoints;

import java.util.ArrayList;

public class HistoryPointsAdapter extends RecyclerView.Adapter<HistoryPointsAdapter.HistoryPointsViewHolder> {
    private ArrayList<HistoryPoints> mData = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public void setData(ArrayList<HistoryPoints> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }


    @NonNull
    @Override
    public HistoryPointsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
            View mViewTop = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_history, viewGroup, false);
            return new HistoryPointsViewHolder(mViewTop);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryPointsViewHolder historyPointsViewHolder, int position) {
        historyPointsViewHolder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class HistoryPointsViewHolder extends RecyclerView.ViewHolder {

        TextView historyDate;

        HistoryPointsViewHolder(@NonNull View itemView) {

            super(itemView);
            historyDate = itemView.findViewById(R.id.date_history);
        }

        void bind(HistoryPoints historyPoints) {

            historyDate.setText(historyPoints.getDate().toString());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickCallback.onItemClicked(mData.get(getAdapterPosition()));
                }
            });

        }
    }



    public interface OnItemClickCallback {
        void onItemClicked(HistoryPoints data);
    }
}
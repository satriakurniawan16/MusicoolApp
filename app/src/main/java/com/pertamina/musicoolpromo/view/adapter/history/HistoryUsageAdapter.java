package com.pertamina.musicoolpromo.view.adapter.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.view.utilities.DateUtils;

import java.util.Collections;
import java.util.List;

public class HistoryUsageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView txt_header;

        HeaderViewHolder(View itemView) {
            super(itemView);
            txt_header = (TextView) itemView.findViewById(R.id.date_history);
        }
    }

    private static class HistoryUsageViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title;
        TextView txt_point;
        TextView status_point;

        HistoryUsageViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.name);
            txt_point = (TextView) itemView.findViewById(R.id.point);
            status_point = (TextView) itemView.findViewById(R.id.description);
        }

    }

    private static class BottomViewHolder extends RecyclerView.ViewHolder {

        TextView txt_total;

        BottomViewHolder(View itemView) {
            super(itemView);
            txt_total = (TextView) itemView.findViewById(R.id.total);
        }

    }

    @NonNull
    private List<ListItem> items = Collections.emptyList();

    public HistoryUsageAdapter (@NonNull List<ListItem> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ListItem.TYPE_HEADER: {
                View itemView = inflater.inflate(R.layout.list_header_history, parent, false);
                return new HistoryUsageAdapter.HeaderViewHolder(itemView);
            }
            case ListItem.TYPE_EVENT: {
                View itemView = inflater.inflate(R.layout.list_item_usage, parent, false);
                return new HistoryUsageAdapter.HistoryUsageViewHolder(itemView);
            }case ListItem.TYPE_BOTTOM: {
                View itemView = inflater.inflate(R.layout.list_item_bottom, parent, false);
                return new HistoryUsageAdapter.BottomViewHolder(itemView);
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case ListItem.TYPE_HEADER: {
                HeaderItem header = (HeaderItem) items.get(position);
                HistoryUsageAdapter.HeaderViewHolder holder = (HistoryUsageAdapter.HeaderViewHolder) viewHolder;
                // your logic here
                holder.txt_header.setText(DateUtils.formatDate(header.getDate()));
//                DateUtils.formatDate(header.getDate()
                break;
            }
            case ListItem.TYPE_EVENT: {
                HistoryItem history = (HistoryItem) items.get(position);
                HistoryUsageAdapter.HistoryUsageViewHolder holder = (HistoryUsageAdapter.HistoryUsageViewHolder) viewHolder;
                // your logic here
                holder.txt_title.setText(history.getHistoryPoints().getTitle());
                holder.txt_point.setText(String.valueOf(history.getHistoryPoints().getTotal()));
                holder.status_point.setText(String.valueOf(history.getHistoryPoints().getStatus()));
                break;
            }
            case ListItem.TYPE_BOTTOM: {
                BottomItem bottom = (BottomItem) items.get(position);
                HistoryUsageAdapter.BottomViewHolder holder = (HistoryUsageAdapter.BottomViewHolder) viewHolder;
                // your logic here
                holder.txt_total.setText(bottom.getLol());
                break;
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

}
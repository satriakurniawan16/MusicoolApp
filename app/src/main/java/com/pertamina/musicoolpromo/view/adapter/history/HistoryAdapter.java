package com.pertamina.musicoolpromo.view.adapter.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.view.utilities.DateUtils;

import java.util.Collections;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView txt_header;

        HeaderViewHolder(View itemView) {
            super(itemView);
            txt_header = (TextView) itemView.findViewById(R.id.date_history);
        }
    }

    private static class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title;
        TextView txt_point;
        CardView rootLayout;

        HistoryViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.name);
            txt_point = (TextView) itemView.findViewById(R.id.point);
            rootLayout = (CardView) itemView.findViewById(R.id.cardLayout);
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

    public HistoryAdapter (@NonNull List<ListItem> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ListItem.TYPE_HEADER: {
                View itemView = inflater.inflate(R.layout.list_header_history, parent, false);
                return new HeaderViewHolder(itemView);
            }
            case ListItem.TYPE_EVENT: {
                View itemView = inflater.inflate(R.layout.list_item_history, parent, false);
                return new HistoryViewHolder(itemView);
            }case ListItem.TYPE_BOTTOM: {
                View itemView = inflater.inflate(R.layout.list_item_bottom, parent, false);
                return new BottomViewHolder(itemView);
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
                HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
                // your logic here
                holder.txt_header.setText(DateUtils.formatDate(header.getDate()));
//                DateUtils.formatDate(header.getDate()
                break;
            }
            case ListItem.TYPE_EVENT: {
                HistoryItem history = (HistoryItem) items.get(position);
                HistoryViewHolder holder = (HistoryViewHolder) viewHolder;
                // your logic here
                holder.txt_title.setText(history.getHistoryPoints().getTitle());
                holder.txt_point.setText(String.valueOf(history.getHistoryPoints().getTotal()));
                if(history.getHistoryPoints().getStatus().equals("credit")){
                    holder.rootLayout.setVisibility(View.GONE);
                }
                break;
            }
            case ListItem.TYPE_BOTTOM: {
                BottomItem bottom = (BottomItem) items.get(position);
                BottomViewHolder holder = (BottomViewHolder) viewHolder;
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


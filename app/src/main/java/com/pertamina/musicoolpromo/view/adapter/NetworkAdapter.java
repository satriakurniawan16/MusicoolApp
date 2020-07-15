package com.pertamina.musicoolpromo.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.network.NetworkResult;

import java.util.List;

public class NetworkAdapter extends RecyclerView.Adapter<NetworkAdapter.NetworkViewHolder> {

    private NetworkAdapter.OnItemClickCallback onItemClickCallback;
    private List<NetworkResult> networks;
    private View emptyView;

    public NetworkAdapter(List<NetworkResult> networks, View emptyView) {
        this.networks = networks;
        this.emptyView = emptyView;
    }

    public void updateEmptyView() {
        if (networks.size() == 0)
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.GONE);
    }

    public void setOnItemClickCallback(NetworkAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public NetworkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
            View mViewTop = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_network, viewGroup, false);
            return new NetworkViewHolder(mViewTop);
    }

    @Override
    public void onBindViewHolder(@NonNull NetworkViewHolder networkViewHolder, int position) {
        networkViewHolder.bind(networks.get(position));
    }

    @Override
    public int getItemCount() {
        return networks.size();
    }

    class NetworkViewHolder extends RecyclerView.ViewHolder {

        TextView networkTitle;
        TextView networkDescription;
        TextView networkContact;

        NetworkViewHolder(@NonNull View itemView) {
            super(itemView);
            networkTitle = itemView.findViewById(R.id.title_network);
            networkDescription = itemView.findViewById(R.id.description_network);
            networkContact = itemView.findViewById(R.id.contact_network);
        }

        void bind(NetworkResult networkResult) {

            networkTitle.setText(networkResult.getNama());
            networkDescription.setText(networkResult.getAlamat());
            networkContact.setText(networkResult.getNo_telp());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickCallback.onItemClicked(networks.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(NetworkResult data);
    }
}

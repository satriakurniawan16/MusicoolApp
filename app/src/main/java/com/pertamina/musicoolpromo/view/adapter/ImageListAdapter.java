package com.pertamina.musicoolpromo.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pertamina.musicoolpromo.R;
import com.pertamina.musicoolpromo.model.image.ImageList;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageListViewHolder> {
    private ImageListAdapter.OnItemClickCallback onItemClickCallback;
    private List<ImageList> images;

    public ImageListAdapter(List<ImageList> images) {
        this.images = images;
    }

    public void setOnItemClickCallback(ImageListAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ImageListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_image, viewGroup, false);
        return new ImageListViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageListViewHolder imageListViewHolder, int position) {
        imageListViewHolder.bind(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ImageListViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        ImageListViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_list);
        }

        void bind(ImageList imageList) {

            Glide.with(itemView.getContext())
                    .load(imageList.getImage())
                    .into(imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickCallback.onItemClicked(
                      images.get(getAdapterPosition())
                    );
                }
            });

        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(ImageList data);
    }

}
package com.pertamina.musicoolpromo.model.image;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageData {
    @SerializedName("data")
    @Expose
    private List<ImageList> imageLists = null;

    public List<ImageList> getImageLists() {
        return imageLists;
    }

//    public void setImageLists(List<ImageList> imageLists) {
//        this.imageLists = imageLists ;
//    }

    public void setImageLists(List<ImageList> imageLists) {
        this.imageLists = imageLists;
    }
}

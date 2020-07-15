package com.pertamina.musicoolpromo.model.promo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PromoData {
    @SerializedName("data")
    @Expose
    private List<PromoResult> promoResults = null;

    public List<PromoResult> getPromoResults() {
        return promoResults;
    }

    public void setPromoResults(List<PromoResult> promoResults) {
        this.promoResults = promoResults;
    }
}

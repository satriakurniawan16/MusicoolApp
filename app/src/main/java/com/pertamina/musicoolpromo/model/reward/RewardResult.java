package com.pertamina.musicoolpromo.model.reward;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RewardResult implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("point_cost")
    @Expose
    private String point_cost;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("is_active")
    @Expose
    private String is_active;
    @SerializedName("start_date")
    @Expose
    private String start_date;
    @SerializedName("end_date")
    @Expose
    private String end_date;
    @SerializedName("term_and_conditions")
    @Expose
    private String term_and_conditions;

    @Override
    public String toString() {
        return "RewardResult{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", point_cost='" + point_cost + '\'' +
                ", quantity='" + quantity + '\'' +
                ", is_active='" + is_active + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoint_cost() {
        return point_cost;
    }

    public void setPoint_cost(String point_cost) {
        this.point_cost = point_cost;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getTerm_and_conditions() {
        return term_and_conditions;
    }

    public void setTerm_and_conditions(String term_and_conditions) {
        this.term_and_conditions = term_and_conditions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.description);
        dest.writeString(this.end_date);
        dest.writeString(this.name);
        dest.writeString(this.start_date);
        dest.writeString(this.point_cost);
        dest.writeString(this.quantity);
        dest.writeString(this.is_active);
        dest.writeString(this.term_and_conditions);
    }

    private RewardResult(Parcel in) {
        this.id  = in.readString();
        this.description  = in.readString();
        this.end_date  = in.readString();
        this.name  = in.readString();
        this.start_date = in.readString();
        this.point_cost= in.readString();
        this.quantity = in.readString();
        this.is_active = in.readString();
        this.term_and_conditions = in.readString();
    }

    public static final Creator<RewardResult> CREATOR = new Creator<RewardResult>() {
        @Override
        public RewardResult createFromParcel(Parcel source) {
            return new RewardResult(source);
        }

        @Override
        public RewardResult[] newArray(int size) {
            return new RewardResult[size];
        }
    };

}

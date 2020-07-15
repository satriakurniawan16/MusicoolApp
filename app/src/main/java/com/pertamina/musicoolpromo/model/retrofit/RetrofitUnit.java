package com.pertamina.musicoolpromo.model.retrofit;

import com.google.gson.annotations.SerializedName;

public class RetrofitUnit {
    @SerializedName("id")
    private String id;
    @SerializedName("brand")
    private String brand;
    @SerializedName("model")
    private String model;
    @SerializedName("capacity")
    private String capacity;
    @SerializedName("customer_id")
    private String customer_id;
    @SerializedName("serial_number")
    private String serial_number;
    @SerializedName("unit_picture")
    private String unit_picture;

    @Override
    public String toString() {
        return "RetrofitUnit{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", capacity='" + capacity + '\'' +
                ", customer_id='" + customer_id + '\'' +
                '}';
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getUnit_picture() {
        return unit_picture;
    }

    public void setUnit_picture(String unit_picture) {
        this.unit_picture = unit_picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
}

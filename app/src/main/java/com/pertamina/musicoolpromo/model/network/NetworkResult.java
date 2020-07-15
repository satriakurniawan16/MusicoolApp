package com.pertamina.musicoolpromo.model.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NetworkResult {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("nama")
    @Expose
    String nama;

    @SerializedName("alamat")
    @Expose
    String alamat;

    @SerializedName("no_telp")
    @Expose
    String no_telp;

    @SerializedName("kota_id")
    @Expose
    String kota_id;

    @SerializedName("provinsi_id")
    @Expose
    String provinsi_id;

    @Override
    public String toString() {
        return "NetworkResult{" +
                "id='" + id + '\'' +
                ", nama='" + nama + '\'' +
                ", alamat='" + alamat + '\'' +
                ", no_telp='" + no_telp + '\'' +
                ", kota_id='" + kota_id + '\'' +
                '}';
    }

    public String getKota_id() {
        return kota_id;
    }

    public void setKota_id(String kota_id) {
        this.kota_id = kota_id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public String getProvinsi_id() {
        return provinsi_id;
    }

    public void setProvinsi_id(String provinsi_id) {
        this.provinsi_id = provinsi_id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
    }
}

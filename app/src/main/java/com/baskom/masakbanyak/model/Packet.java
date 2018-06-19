package com.baskom.masakbanyak.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Castor on 4/5/2018.
 */

public class Packet implements Serializable {
    @SerializedName("_id")
    private String packet_id;
    @SerializedName("name")
    private String name;
    @SerializedName("contents")
    private ArrayList<String> contents;
    @SerializedName("minimum_quantity")
    private int minimum_quantity;
    @SerializedName("price")
    private int price;
    @SerializedName("images")
    private ArrayList<String> images;
    @SerializedName("catering_id")
    private String catering_id;

    public Packet(String packet_id, String name, ArrayList<String> contents, int minimum_quantity, int price, ArrayList<String> images, String catering_id) {
        this.packet_id = packet_id;
        this.name = name;
        this.contents = contents;
        this.minimum_quantity = minimum_quantity;
        this.price = price;
        this.images = images;
        this.catering_id = catering_id;
    }

    public String getPacket_id() {
        return packet_id;
    }

    public void setPacket_id(String packet_id) {
        this.packet_id = packet_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getContents() {
        return contents;
    }

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }

    public int getMinimum_quantity() {
        return minimum_quantity;
    }

    public void setMinimum_quantity(int minimum_quantity) {
        this.minimum_quantity = minimum_quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getCatering_id() {
        return catering_id;
    }

    public void setCatering_id(String catering_id) {
        this.catering_id = catering_id;
    }
}
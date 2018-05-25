package com.baskom.masakbanyak;

import com.google.gson.annotations.SerializedName;

public class Rating {
    @SerializedName("rating_value")
    private int rating_value;
    @SerializedName("customer_id")
    private String customer_id;

    public Rating(int rating_value, String customer_id) {
        this.rating_value = rating_value;
        this.customer_id = customer_id;
    }

    public int getRating_value() {
        return rating_value;
    }

    public void setRating_value(int rating_value) {
        this.rating_value = rating_value;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
}

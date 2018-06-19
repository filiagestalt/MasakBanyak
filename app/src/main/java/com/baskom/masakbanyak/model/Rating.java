package com.baskom.masakbanyak.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Rating implements Serializable {
  @SerializedName("rating_value")
  private double rating_value;
  @SerializedName("customer_id")
  private String customer_id;
  
  public Rating(double rating_value, String customer_id) {
    this.rating_value = rating_value;
    this.customer_id = customer_id;
  }
  
  public double getRating_value() {
    return rating_value;
  }
  
  public void setRating_value(double rating_value) {
    this.rating_value = rating_value;
  }
  
  public String getCustomer_id() {
    return customer_id;
  }
  
  public void setCustomer_id(String customer_id) {
    this.customer_id = customer_id;
  }
}

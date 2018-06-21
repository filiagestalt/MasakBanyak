package com.baskom.masakbanyak.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order implements Serializable {
  @SerializedName("_id")
  private String order_id;
  @SerializedName("customer_id")
  private String customer_id;
  @SerializedName("packet_id")
  private String packet_id;
  @SerializedName("quantity")
  private int quantity;
  @SerializedName("total_price")
  private int total_price;
  @SerializedName("order_time")
  private String order_time;
  @SerializedName("event_time")
  private String event_time;
  @SerializedName("event_address")
  private String event_address;
  @SerializedName("virtual_account")
  private VirtualAccount virtual_account;
  @SerializedName("status")
  private String status;
  
  public Order(){
  
  }
  
  public Order(String order_id, String customer_id, String packet_id, int quantity, int total_price, String order_time, String event_time, String event_address, VirtualAccount virtual_account, String status) {
    this.order_id = order_id;
    this.customer_id = customer_id;
    this.packet_id = packet_id;
    this.quantity = quantity;
    this.total_price = total_price;
    this.order_time = order_time;
    this.event_time = event_time;
    this.event_address = event_address;
    this.virtual_account = virtual_account;
    this.status = status;
  }
  
  public String getOrder_id() {
    return order_id;
  }
  
  public void setOrder_id(String order_id) {
    this.order_id = order_id;
  }
  
  public String getCustomer_id() {
    return customer_id;
  }
  
  public void setCustomer_id(String customer_id) {
    this.customer_id = customer_id;
  }
  
  public String getPacket_id() {
    return packet_id;
  }
  
  public void setPacket_id(String packet_id) {
    this.packet_id = packet_id;
  }
  
  public int getQuantity() {
    return quantity;
  }
  
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
  
  public int getTotal_price() {
    return total_price;
  }
  
  public void setTotal_price(int total_price) {
    this.total_price = total_price;
  }
  
  public String getOrder_time() {
    return order_time;
  }
  
  public void setOrder_time(String order_time) {
    this.order_time = order_time;
  }
  
  public String getEvent_time() {
    return event_time;
  }
  
  public void setEvent_time(String event_time) {
    this.event_time = event_time;
  }
  
  public String getEvent_address() {
    return event_address;
  }
  
  public void setEvent_address(String event_address) {
    this.event_address = event_address;
  }
  
  public VirtualAccount getVirtual_account() {
    return virtual_account;
  }
  
  public void setVirtual_account(VirtualAccount virtual_account) {
    this.virtual_account = virtual_account;
  }
  
  public String getStatus() {
    return status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  public String getOrderDateDDMMYYYY(){
    SimpleDateFormat ISOFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    SimpleDateFormat DDMMYYYYFormat = new SimpleDateFormat("dd-MM-yyyy");
    Date date = new Date();
    try {
      date = ISOFormat.parse(order_time);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    
    return DDMMYYYYFormat.format(date);
  }
}

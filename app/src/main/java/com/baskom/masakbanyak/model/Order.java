package com.baskom.masakbanyak.model;

import java.io.Serializable;

public class Order implements Serializable {
  private String order_id;
  private String packet_id;
  private String customer_id;
  private int quantity;
  private int total_amount;
  private String address;
  private String datetime;
  private String location;
  
  public Order(){
  
  }
  
  public Order(String order_id, String packet_id, String customer_id, int quantity, int total_amount, String address, String datetime, String location) {
    this.order_id = order_id;
    this.packet_id = packet_id;
    this.customer_id = customer_id;
    this.quantity = quantity;
    this.total_amount = total_amount;
    this.address = address;
    this.datetime = datetime;
    this.location = location;
  }
  
  public String getOrder_id() {
    return order_id;
  }
  
  public void setOrder_id(String order_id) {
    this.order_id = order_id;
  }
  
  public String getPacket_id() {
    return packet_id;
  }
  
  public void setPacket_id(String packet_id) {
    this.packet_id = packet_id;
  }
  
  public String getCustomer_id() {
    return customer_id;
  }
  
  public void setCustomer_id(String customer_id) {
    this.customer_id = customer_id;
  }
  
  public int getQuantity() {
    return quantity;
  }
  
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
  
  public int getTotal_amount() {
    return total_amount;
  }
  
  public void setTotal_amount(int total_amount) {
    this.total_amount = total_amount;
  }
  
  public String getAddress() {
    return address;
  }
  
  public void setAddress(String address) {
    this.address = address;
  }
  
  public String getDatetime() {
    return datetime;
  }
  
  public void setDatetime(String datetime) {
    this.datetime = datetime;
  }
  
  public String getLocation() {
    return location;
  }
  
  public void setLocation(String location) {
    this.location = location;
  }
}

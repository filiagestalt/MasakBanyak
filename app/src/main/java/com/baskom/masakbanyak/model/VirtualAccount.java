package com.baskom.masakbanyak.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VirtualAccount implements Serializable {
  @SerializedName("va_number")
  private String number;
  @SerializedName("bank")
  private String bank;
  
  public VirtualAccount(String number, String bank) {
    this.number = number;
    this.bank = bank;
  }
  
  public String getNumber() {
    return number;
  }
  
  public void setNumber(String number) {
    this.number = number;
  }
  
  public String getBank() {
    return bank;
  }
  
  public void setBank(String bank) {
    this.bank = bank;
  }
}

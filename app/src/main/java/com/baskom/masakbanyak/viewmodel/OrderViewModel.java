package com.baskom.masakbanyak.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.baskom.masakbanyak.model.Order;
import com.baskom.masakbanyak.repository.OrderRepository;

import java.util.ArrayList;

import javax.inject.Inject;

public class OrderViewModel extends ViewModel {
  private OrderRepository repository;
  
  private LiveData<ArrayList<Order>> ordersLiveData;
  
  @Inject
  public OrderViewModel(OrderRepository repository) {
    this.repository = repository;
    this.ordersLiveData = repository.getOrdersLiveData();
  }
  
  public LiveData<ArrayList<Order>> getOrdersLiveData() {
    return ordersLiveData;
  }
  
  public void refreshOrders(){
    repository.refreshOrders();
  }
}
package com.baskom.masakbanyak.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.baskom.masakbanyak.model.Order;
import com.baskom.masakbanyak.repository.OrderRepository;
import com.baskom.masakbanyak.util.Util;

import java.util.ArrayList;

import javax.inject.Inject;

public class OrderViewModel extends ViewModel {
  private OrderRepository repository;
  
  private LiveData<ArrayList<Order>> ordersLiveData;
  private LiveData<Util.Event<String>> notificationEventLiveData;
  
  @Inject
  public OrderViewModel(OrderRepository repository) {
    this.repository = repository;
    this.ordersLiveData = repository.getOrdersLiveData();
    this.notificationEventLiveData = repository.getNotificationEventLiveData();
  }
  
  public LiveData<ArrayList<Order>> getOrdersLiveData() {
    return ordersLiveData;
  }
  
  public LiveData<Util.Event<String>> getNotificationEventLiveData() {
    return notificationEventLiveData;
  }
  
  public void refreshOrders(){
    repository.refreshOrders();
  }
  
  public void refundOrder(Order order){
    repository.refundOrder(order);
  }
}
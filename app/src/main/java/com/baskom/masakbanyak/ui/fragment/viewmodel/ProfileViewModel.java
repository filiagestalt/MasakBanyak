package com.baskom.masakbanyak.ui.fragment.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.baskom.masakbanyak.repository.CustomerRepository;
import com.baskom.masakbanyak.model.Customer;
import com.baskom.masakbanyak.util.Util;

import javax.inject.Inject;

public class ProfileViewModel extends ViewModel {
  private CustomerRepository repository;
  
  private LiveData<Customer> customerLiveData;
  private LiveData<Util.Event<String>> notificationEventLiveData;
  
  @Inject public ProfileViewModel(CustomerRepository repository) {
    this.repository = repository;
    this.customerLiveData = repository.getCustomerLiveData();
    this.notificationEventLiveData = repository.getNotificationEventLiveData();
  }
  
  public LiveData<Customer> getCustomerLiveData() {
    return customerLiveData;
  }
  
  public LiveData<Util.Event<String>> getNotificationEventLiveData() {
    return notificationEventLiveData;
  }
  
  public void uploadProfileAvatar(Customer customer, String filename, byte[] file){
    repository.uploadProfileAvatar(customer, filename, file);
  }
  
  public void updateProfile(Customer customer){
    repository.updateProfile(customer);
  }
  
  public void logout(){
    repository.logout();
  }
}
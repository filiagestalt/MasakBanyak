package com.baskom.masakbanyak.di;

import com.baskom.masakbanyak.model.Customer;
import com.baskom.masakbanyak.repository.CustomerRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class CustomerModule {
  @Provides
  public Customer provideCustomer(CustomerRepository repository){
    return repository.getCustomerLiveData().getValue();
  }
}

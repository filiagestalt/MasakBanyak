package com.baskom.masakbanyak.di;

import android.arch.lifecycle.ViewModel;

import com.baskom.masakbanyak.viewmodel.CateringViewModel;
import com.baskom.masakbanyak.viewmodel.CustomerViewModel;
import com.baskom.masakbanyak.viewmodel.OrderViewModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.Binds;
import dagger.MapKey;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
  
  @Binds
  @IntoMap
  @ViewModelKey(CateringViewModel.class)
  public abstract ViewModel cateringViewModel(CateringViewModel cateringViewModel);
  
  @Binds
  @IntoMap
  @ViewModelKey(OrderViewModel.class)
  public abstract ViewModel transactionViewModel(OrderViewModel orderViewModel);
  
  @Binds
  @IntoMap
  @ViewModelKey(CustomerViewModel.class)
  public abstract ViewModel profileViewModel(CustomerViewModel customerViewModel);
  
  //The annotation to define the type of key used in the map.
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @MapKey
  @interface ViewModelKey{
    Class<? extends ViewModel> value();
  }
}
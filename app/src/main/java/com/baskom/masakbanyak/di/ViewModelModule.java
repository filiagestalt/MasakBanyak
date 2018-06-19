package com.baskom.masakbanyak.di;

import android.arch.lifecycle.ViewModel;

import com.baskom.masakbanyak.ui.fragment.viewmodel.CateringViewModel;
import com.baskom.masakbanyak.ui.fragment.viewmodel.CateringsViewModel;
import com.baskom.masakbanyak.ui.fragment.viewmodel.ProfileViewModel;

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
  @ViewModelKey(ProfileViewModel.class)
  public abstract ViewModel profileViewModel(ProfileViewModel profileViewModel);
  
  @Binds
  @IntoMap
  @ViewModelKey(CateringsViewModel.class)
  public abstract ViewModel cateringsViewModel(CateringsViewModel cateringsViewModel);
  
  @Binds
  @IntoMap
  @ViewModelKey(CateringViewModel.class)
  public abstract ViewModel cateringViewModel(CateringViewModel cateringViewModel);
  
  //The annotation to define the type of key used in the map.
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @MapKey
  @interface ViewModelKey{
    Class<? extends ViewModel> value();
  }
}
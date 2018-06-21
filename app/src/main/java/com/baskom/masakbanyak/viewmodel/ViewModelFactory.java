package com.baskom.masakbanyak.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton public class ViewModelFactory implements ViewModelProvider.Factory {
  private Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModelsMap;
  
  @Inject
  public ViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModelsMap) {
    this.viewModelsMap = viewModelsMap;
  }
  
  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    Provider<ViewModel> viewModelProvider = viewModelsMap.get(modelClass);
    
    if (viewModelProvider == null) {
      throw new IllegalArgumentException("Suitable ViewModel not found.");
    }
    
    return (T) viewModelProvider.get();
  }
}

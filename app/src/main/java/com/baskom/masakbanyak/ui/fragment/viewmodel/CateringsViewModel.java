package com.baskom.masakbanyak.ui.fragment.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.baskom.masakbanyak.model.Catering;
import com.baskom.masakbanyak.repository.CateringsRepository;

import java.util.ArrayList;

import javax.inject.Inject;

public class CateringsViewModel extends ViewModel {
  private LiveData<ArrayList<Catering>> cateringsLiveData;
  
  private CateringsRepository repository;
  
  @Inject
  public CateringsViewModel(CateringsRepository repository) {
    this.repository = repository;
    this.cateringsLiveData = repository.getCateringsLiveData();
  }
  
  public LiveData<ArrayList<Catering>> getCateringsLiveData() {
    return cateringsLiveData;
  }
}

package com.baskom.masakbanyak.ui.fragment.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.baskom.masakbanyak.model.Catering;
import com.baskom.masakbanyak.model.Packet;
import com.baskom.masakbanyak.repository.PacketsRepository;

import java.util.ArrayList;

import javax.inject.Inject;

public class CateringViewModel extends ViewModel {
  private LiveData<ArrayList<Packet>> packetsLiveData;
  
  private PacketsRepository repository;
  
  @Inject
  public CateringViewModel(PacketsRepository repository) {
    this.repository = repository;
  }
  
  public LiveData<ArrayList<Packet>> getPacketsLiveData(Catering catering) {
    this.packetsLiveData = repository.getPacketsLiveData(catering);
    return packetsLiveData;
  }
}

package com.baskom.masakbanyak.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.baskom.masakbanyak.model.Catering;
import com.baskom.masakbanyak.model.Packet;
import com.baskom.masakbanyak.repository.CateringRepository;

import java.util.ArrayList;

import javax.inject.Inject;

public class CateringViewModel extends ViewModel {
  private LiveData<ArrayList<Catering>> cateringsLiveData;
  private LiveData<ArrayList<Packet>> packetsLiveData;
  
  private CateringRepository repository;
  
  @Inject
  public CateringViewModel(CateringRepository repository) {
    this.repository = repository;
    this.cateringsLiveData = repository.getCateringsLiveData();
  }
  
  public LiveData<ArrayList<Catering>> getCateringsLiveData() {
    return cateringsLiveData;
  }
  
  public LiveData<ArrayList<Packet>> getPacketsLiveData(Catering catering) {
    this.packetsLiveData = repository.getPacketsLiveData(catering);
    return packetsLiveData;
  }
  
  public void refreshCaterings(){
    repository.refreshCaterings();
  }
  
  public void refreshPackets(Catering catering){
    repository.refreshPackets(catering);
  }
}

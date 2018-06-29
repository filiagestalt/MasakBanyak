package com.baskom.masakbanyak.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.baskom.masakbanyak.model.Catering;
import com.baskom.masakbanyak.model.Order;
import com.baskom.masakbanyak.model.Packet;
import com.baskom.masakbanyak.repository.CateringRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class CateringViewModel extends ViewModel {
  private LiveData<ArrayList<Catering>> cateringsLiveData;
  private LiveData<ArrayList<Packet>> packetsLiveDataByCatering;
  private LiveData<Packet> packetLiveDataByOrder;
  
  private CateringRepository repository;
  
  @Inject
  public CateringViewModel(CateringRepository repository) {
    this.repository = repository;
    this.cateringsLiveData = repository.getCateringsLiveData();
  }
  
  public LiveData<ArrayList<Catering>> getCateringsLiveData() {
    return cateringsLiveData;
  }
  
  public LiveData<ArrayList<Packet>> getPacketsLiveDataByCatering(Catering catering) {
    this.packetsLiveDataByCatering = repository.getPacketsLiveDataByCatering(catering);
    return packetsLiveDataByCatering;
  }
  
  public LiveData<Packet> getPacketLiveDataByOrder(Order order) {
    this.packetLiveDataByOrder = repository.getPacketLiveDataById(order.getPacket_id());
    return packetLiveDataByOrder;
  }
  
  public void refreshCaterings() {
    repository.refreshCaterings();
  }
  
  public void refreshPacketsByCatering(Catering catering) {
    repository.refreshPacketsByCatering(catering);
  }
  
  public void refreshPacketByOrder(Order order) {
    repository.refreshPacketById(order.getPacket_id());
  }
}

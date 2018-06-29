package com.baskom.masakbanyak.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baskom.masakbanyak.R;
import com.baskom.masakbanyak.di.Components;
import com.baskom.masakbanyak.model.Customer;
import com.baskom.masakbanyak.model.Order;
import com.baskom.masakbanyak.model.Packet;
import com.baskom.masakbanyak.viewmodel.CustomerViewModel;
import com.baskom.masakbanyak.viewmodel.ViewModelFactory;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.baskom.masakbanyak.Constants.MASAKBANYAK_URL;

public class MakeOrderActivity extends AppCompatActivity {
  @Inject
  ViewModelFactory mViewModelFactory;
  
  CustomerViewModel mCustomerViewModel;
  
  private Customer mCustomer;
  private Packet mPacket;
  private Order mOrder;
  
  private TransactionRequest mTransactionRequest;
  private MidtransSDK mMidtransSDK;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order);
    
    Components.getSessionComponent().inject(this);
    mCustomerViewModel = ViewModelProviders.of(this, mViewModelFactory).get(CustomerViewModel.class);
    
    mCustomer = mCustomerViewModel.getCustomerLiveData().getValue();
    mPacket = (Packet) getIntent().getSerializableExtra("packet");
    mOrder = (Order) getIntent().getSerializableExtra("order");
    mOrder.setPacket_id(mPacket.getPacket_id());
    mOrder.setCustomer_id(mCustomer.getCustomer_id());
    
    mTransactionRequest = new TransactionRequest("order_id", mOrder.getTotal_price());
    
    UIKitCustomSetting customSetting = new UIKitCustomSetting();
    customSetting.setSkipCustomerDetailsPages(true);
    
    mMidtransSDK = SdkUIFlowBuilder.init()
        .setTransactionFinishedCallback(transactionResult -> finish())
        .setClientKey("SB-Mid-client-aqNjn-jRTgK28AM7")
        .setUIkitCustomSetting(customSetting)
        .setMerchantBaseUrl(MASAKBANYAK_URL)
        .setContext(this)
        .enableLog(true)
        .buildSDK();
  }
  
  @Override
  protected void onStart() {
    super.onStart();
    
    LocalDataHandler.saveObject("user_details", getUserDetail());
    mTransactionRequest.setItemDetails(getItemDetails());
    mTransactionRequest.setCustomField1(mOrder.getEvent_time());
    mTransactionRequest.setCustomField2(mOrder.getEvent_address());
    
    mMidtransSDK.setTransactionRequest(mTransactionRequest);
    mMidtransSDK.startPaymentUiFlow(this);
  }
  
  private UserDetail getUserDetail() {
    UserDetail userDetail = new UserDetail();
    userDetail.setUserId(mCustomer.getCustomer_id());
    userDetail.setUserFullName(mCustomer.getName());
    userDetail.setEmail(mCustomer.getEmail());
    userDetail.setPhoneNumber(mCustomer.getPhone());
    
    return userDetail;
  }
  
  private ArrayList<ItemDetails> getItemDetails() {
    ItemDetails itemDetails = new ItemDetails(mPacket.getPacket_id(), mPacket.getPrice(), mOrder.getQuantity(), mPacket.getName());
    
    ArrayList<ItemDetails> itemDetailsList = new ArrayList<>();
    itemDetailsList.add(itemDetails);
    
    return itemDetailsList;
  }
}
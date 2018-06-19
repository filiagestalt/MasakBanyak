package com.baskom.masakbanyak.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.baskom.masakbanyak.MasakBanyakApplication;
import com.baskom.masakbanyak.R;
import com.baskom.masakbanyak.model.Order;
import com.baskom.masakbanyak.model.Packet;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import static com.baskom.masakbanyak.Constants.MASAKBANYAK_URL;

public class OrderActivity extends AppCompatActivity {
  private Packet mPacket;
  private Order mOrder;
  
  @Inject JWT jwt;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order);
    
    MasakBanyakApplication.getInstance().getApplicationComponent().inject(this);
    
    mPacket = (Packet) getIntent().getSerializableExtra("packet");
    mOrder = (Order) getIntent().getSerializableExtra("order");
    
    SdkUIFlowBuilder.init()
        .setClientKey("SB-Mid-client-aqNjn-jRTgK28AM7")
        .setContext(this)
        .setTransactionFinishedCallback(transactionResult -> {
          if(transactionResult.isTransactionCanceled()){
            //Handle canceled transaction by deleting order from the database
            Toast.makeText(this, "Transaksi telah dibatalkan, maaf ya.", Toast.LENGTH_SHORT).show();
          }else{
            Toast.makeText(this, transactionResult.getStatus()+transactionResult.getResponse().getOrderId(), Toast.LENGTH_SHORT).show();
          }
          
          finish();
        })
        .setMerchantBaseUrl(MASAKBANYAK_URL)
        .enableLog(true)
        .buildSDK();
    
    setUserDetail();
    TransactionRequest transactionRequest = new TransactionRequest("order_id", mOrder.getTotal_amount());
    transactionRequest.setCustomField1(jwt.getClaim("customer_id").asString());
    transactionRequest.setItemDetails(getItemDetails());
    
    MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
    MidtransSDK.getInstance().startPaymentUiFlow(this);
  }
  
  private void setUserDetail() {
    UserDetail userDetail = LocalDataHandler.readObject("user_details", UserDetail.class);
    if (userDetail == null) {
      userDetail = new UserDetail();
      userDetail.setUserFullName("Iqbal Zulfikar");
      userDetail.setEmail("iqbal@gmail.com");
      userDetail.setPhoneNumber("081234567890");
      
      ArrayList<UserAddress> userAddresses = new ArrayList<>();
      
      UserAddress address00 = new UserAddress();
      address00.setAddress("Rumah Iqbal");
      address00.setCity("Jakarta");
      address00.setAddressType(Constants.ADDRESS_TYPE_SHIPPING);
      address00.setZipcode("12345");
      address00.setCountry("IDN");
      userAddresses.add(address00);
      
      UserAddress address01 = new UserAddress();
      address01.setAddress("Rumah Orang");
      address01.setCity("Jakarta");
      address01.setAddressType(Constants.ADDRESS_TYPE_BILLING);
      address01.setZipcode("12345");
      address01.setCountry("IDN");
      userAddresses.add(address01);
      
      userDetail.setUserAddresses(userAddresses);
      LocalDataHandler.saveObject("user_details", userDetail);
    }
  }
  
  private ArrayList<ItemDetails> getItemDetails() {
    ItemDetails itemDetails = new ItemDetails(mPacket.getPacket_id(), mPacket.getPrice(),
        mOrder.getQuantity(), mPacket.getName());
    
    ArrayList<ItemDetails> itemDetailsList = new ArrayList<ItemDetails>();
    itemDetailsList.add(itemDetails);
    
    return itemDetailsList;
  }
}
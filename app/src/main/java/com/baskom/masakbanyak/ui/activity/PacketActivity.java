package com.baskom.masakbanyak.ui.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baskom.masakbanyak.model.Order;
import com.baskom.masakbanyak.model.Packet;
import com.baskom.masakbanyak.R;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import nl.dionsegijn.steppertouch.StepperTouch;

import static com.baskom.masakbanyak.Constants.MASAKBANYAK_URL;

public class PacketActivity extends AppCompatActivity implements
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
  
  private Packet mPacket;
  private Order mOrder;
  private Calendar mCalendar;
  
  private ImageView mImageView;
  private TextView mTextViewPacketName;
  private LinearLayout mPacketContents;
  private TextView mTextViewPacketPrice;
  private StepperTouch mNumberStepper;
  private FloatingActionButton mButtonOrderDate;
  private DatePickerDialog mDatePickerDialog;
  private TimePickerDialog mTimePickerDialog;
  private FloatingActionButton mButtonOrderLocation;
  private AlertDialog mLocationDialog;
  private Button mButtonOrder;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_packet);
    
    mPacket = (Packet) getIntent().getSerializableExtra("packet");
    mOrder = new Order();
    mCalendar = Calendar.getInstance();
    
    mImageView = findViewById(R.id.packet_images);
    mTextViewPacketName = findViewById(R.id.packet_name);
    mPacketContents = findViewById(R.id.packet_contents);
    mTextViewPacketPrice = findViewById(R.id.packet_price);
    mNumberStepper = findViewById(R.id.number_stepper);
    mButtonOrderDate = findViewById(R.id.orderDateButton);
    mButtonOrderLocation = findViewById(R.id.orderLocationButton);
    mButtonOrder = findViewById(R.id.button_order);
    
    mDatePickerDialog = DatePickerDialog.newInstance(this, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
    mTimePickerDialog = TimePickerDialog.newInstance(this, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);
  }
  
  @Override
  protected void onStart() {
    super.onStart();
  
    Picasso.get().load(MASAKBANYAK_URL + mPacket.getImages().get(0))
        .fit()
        .centerCrop()
        .into(mImageView);
    
    mOrder.setQuantity(mPacket.getMinimum_quantity());
    mOrder.setTotal_price(mPacket.getDefaultTotalPrice());
    mTextViewPacketName.setText(mPacket.getName());
    mTextViewPacketPrice.setText("Rp " + NumberFormat.getNumberInstance(Locale.US).format(mPacket.getDefaultTotalPrice()));
  
    for (int i = 0; i < mPacket.getContents().size(); i++) {
      TextView content = (TextView) getLayoutInflater().inflate(R.layout.itemview_packet_content, null);
      content.setText(mPacket.getContents().get(i));
      mPacketContents.addView(content);
    }
  
    mNumberStepper.stepper.setMin(mPacket.getMinimum_quantity());
    mNumberStepper.stepper.setValue(mPacket.getMinimum_quantity());
    mNumberStepper.stepper.addStepCallback((i, b) -> {
      mOrder.setQuantity(i);
      mOrder.setTotal_price(mPacket.getPrice() * i);
      mTextViewPacketPrice.setText("Rp " + NumberFormat.getNumberInstance(Locale.US).format(mPacket.getPrice() * i));
    });
    
    mButtonOrderDate.setOnClickListener(v -> mDatePickerDialog.show(getFragmentManager(), "date_picker"));
    mButtonOrderLocation.setOnClickListener(v -> showLocationDialog());
  
    mButtonOrder.setOnClickListener(v -> {
      Intent orderIntent = new Intent(PacketActivity.this, MakeOrderActivity.class);
      
      orderIntent.putExtra("packet", mPacket);
      orderIntent.putExtra("order", mOrder);
      startActivity(orderIntent);
    });
  }
  
  @Override
  public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
    mCalendar.set(year, monthOfYear, dayOfMonth);
    
    mTimePickerDialog.show(getFragmentManager(), "time_picker");
  }
  
  @Override
  public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
    mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
    mCalendar.set(Calendar.MINUTE, minute);
    mCalendar.set(Calendar.SECOND, second);
    
    SimpleDateFormat ISODate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    Date datetime = mCalendar.getTime();
    mOrder.setEvent_time(ISODate.format(datetime));
  }
  
  private void showLocationDialog() {
    EditText editText = new EditText(this);
    mLocationDialog = new AlertDialog.Builder(this)
        .setTitle("Alamat")
        .setMessage("Masukkan alamat acara.")
        .setView(editText)
        .setPositiveButton("Ok", (dialogObject, which) -> {
          String address = editText.getText().toString();
          mOrder.setEvent_address(address);
        })
        .setNegativeButton("Cancel", null)
        .create();
    
    mLocationDialog.show();
  }
}

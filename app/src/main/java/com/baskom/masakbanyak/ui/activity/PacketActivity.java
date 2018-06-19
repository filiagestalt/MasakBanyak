package com.baskom.masakbanyak.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baskom.masakbanyak.model.Order;
import com.baskom.masakbanyak.model.Packet;
import com.baskom.masakbanyak.R;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import nl.dionsegijn.steppertouch.OnStepCallback;
import nl.dionsegijn.steppertouch.StepperTouch;

import static com.baskom.masakbanyak.Constants.MASAKBANYAK_URL;

public class PacketActivity extends AppCompatActivity implements
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
  
  private Packet mPacket;
  private Calendar mCalendar;
  private Order mOrder;
  
  private ImageView mImageView;
  private StepperTouch mNumberStepper;
  private TextView mTextViewPacketName;
  private LinearLayout mPacketContents;
  private TextView mTextViewPacketPrice;
  private FloatingActionButton mButtonOrderDate;
  private FloatingActionButton mButtonOrderLocation;
  private Button mButtonOrder;
  
  private DatePickerDialog mDatePickerDialog;
  private TimePickerDialog mTimePickerDialog;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_packet);
    
    mPacket = (Packet) getIntent().getSerializableExtra("packet");
    mCalendar = Calendar.getInstance();
    mOrder = new Order();
    
    mImageView = findViewById(R.id.packet_images);
    mNumberStepper = findViewById(R.id.number_stepper);
    mTextViewPacketName = findViewById(R.id.packet_name);
    mPacketContents = findViewById(R.id.packet_contents);
    mTextViewPacketPrice = findViewById(R.id.packet_price);
    mButtonOrderDate = findViewById(R.id.orderDateButton);
    mButtonOrderLocation = findViewById(R.id.orderLocationButton);
    mButtonOrder = findViewById(R.id.button_order);
    
    mTextViewPacketName.setText(mPacket.getName());
    
    int totalPrice = mPacket.getPrice() * mPacket.getMinimum_quantity();
    String formattedTotalPrice = "Rp " + NumberFormat.getNumberInstance(Locale.US).format(totalPrice);
    mTextViewPacketPrice.setText(formattedTotalPrice);
    
    Picasso.get().load(MASAKBANYAK_URL + mPacket.getImages().get(0))
        .fit()
        .centerCrop()
        .into(mImageView);
    
    mDatePickerDialog = DatePickerDialog.newInstance(this, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
    mTimePickerDialog = TimePickerDialog.newInstance(this, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);
    
    mDatePickerDialog.setAccentColor(getResources().getColor(R.color.colorAccentDark));
    mButtonOrderDate.setOnClickListener(v -> mDatePickerDialog.show(getFragmentManager(), "datepickerdialog"));
    
    mButtonOrderLocation.setOnClickListener(v -> showAddressDialog());
    
    mNumberStepper.stepper.setMin(mPacket.getMinimum_quantity());
    mNumberStepper.stepper.setValue(mPacket.getMinimum_quantity());
    mNumberStepper.stepper.addStepCallback((i, b) -> {
      int totalPrice1 = mPacket.getPrice() * i;
      String formattedTotalPrice1 = "Rp " + NumberFormat.getNumberInstance(Locale.US).format(totalPrice1);
      mTextViewPacketPrice.setText(formattedTotalPrice1);
    });
    
    for (int i = 0; i < mPacket.getContents().size(); i++) {
      TextView content = (TextView) getLayoutInflater().inflate(R.layout.itemview_packet_content, null);
      content.setText(mPacket.getContents().get(i));
      mPacketContents.addView(content);
    }
    
    mButtonOrder.setOnClickListener(v -> {
      Intent orderIntent = new Intent(PacketActivity.this, OrderActivity.class);
      String total_amount = "";
      
      try {
        total_amount = NumberFormat.getNumberInstance(Locale.US).parse(mTextViewPacketPrice.getText().toString().substring("Rp ".length())).toString();
      } catch (ParseException e) {
        e.printStackTrace();
      }
      
      mOrder.setQuantity(mNumberStepper.stepper.getValue());
      mOrder.setTotal_amount(Integer.parseInt(total_amount));
      orderIntent.putExtra("packet", mPacket);
      orderIntent.putExtra("order", mOrder);
      startActivity(orderIntent);
    });
    
  }
  
  @Override
  public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
    mCalendar.set(year, monthOfYear, dayOfMonth);
    
    mTimePickerDialog.show(getFragmentManager(), "timepickerdialog");
  }
  
  @Override
  public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
    mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
    mCalendar.set(Calendar.MINUTE, minute);
    mCalendar.set(Calendar.SECOND, second);
  
    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    Date datetime = mCalendar.getTime();
    mOrder.setDatetime(dateTimeFormat.format(datetime));
    Toast.makeText(this, dateTimeFormat.format(datetime), Toast.LENGTH_SHORT).show();
  }
  
  private void showAddressDialog() {
    final EditText dialogEditText = new EditText(this);
    AlertDialog dialog = new AlertDialog.Builder(this)
        .setTitle("Alamat")
        .setMessage("Masukkan alamat acara.")
        .setView(dialogEditText)
        .setPositiveButton("OKE", (dialogInterface, which) -> {
          String address = String.valueOf(dialogEditText.getText());
          mOrder.setAddress(address);
          Toast.makeText(PacketActivity.this, address, Toast.LENGTH_SHORT).show();
        })
        .setNegativeButton("Cancel", null)
        .create();
    
    dialog.show();
  }
}

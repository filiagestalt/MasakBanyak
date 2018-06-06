package com.baskom.masakbanyak;

import android.content.DialogInterface;
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

import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import nl.dionsegijn.steppertouch.OnStepCallback;
import nl.dionsegijn.steppertouch.StepperTouch;

import static com.baskom.masakbanyak.Constants.MASAKBANYAK_URL;

public class PacketActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private Packet mPacket;
    private ImageView mImageView;
    private TextView mTextViewPacketName;
    private LinearLayout mPacketContents;
    private TextView mTextViewPacketPrice;
    private Button mButtonOrderDate;
    private Button mButtonOrderLocation;
    private StepperTouch mNumberStepper;

    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet);

        mPacket = (Packet) getIntent().getSerializableExtra("packet");

        mImageView = findViewById(R.id.packet_images);
        mTextViewPacketName = findViewById(R.id.packet_name);
        mPacketContents = findViewById(R.id.packet_contents);
        mTextViewPacketPrice = findViewById(R.id.packet_price);
        mButtonOrderDate = findViewById(R.id.order_date);
        mButtonOrderLocation = findViewById(R.id.order_location);
        mNumberStepper = findViewById(R.id.number_stepper);

        mTextViewPacketName.setText(mPacket.getName());

        int totalPrice = mPacket.getPrice()*mPacket.getMinimum_quantity();
        String formattedTotalPrice = "Rp "+NumberFormat.getNumberInstance(Locale.US).format(totalPrice);
        mTextViewPacketPrice.setText(formattedTotalPrice);

        Picasso.get().load(MASAKBANYAK_URL+mPacket.getImages().get(0))
                .fit()
                .centerCrop()
                .into(mImageView);


        Calendar calendar = Calendar.getInstance();
        mDatePickerDialog = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        mDatePickerDialog.setAccentColor(getResources().getColor(R.color.colorAccentDark));
        mButtonOrderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show(getFragmentManager(), "datepickerdialog");
            }
        });
        mTimePickerDialog = TimePickerDialog.newInstance(
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );

        mButtonOrderLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressDialog();
            }
        });

        mNumberStepper.stepper.setMin(mPacket.getMinimum_quantity());
        mNumberStepper.stepper.setValue(mPacket.getMinimum_quantity());
        mNumberStepper.stepper.addStepCallback(new OnStepCallback() {
            @Override
            public void onStep(int i, boolean b) {
                final int totalPrice = mPacket.getPrice()*i;
                String formattedTotalPrice = "Rp "+NumberFormat.getNumberInstance(Locale.US).format(totalPrice);
                mTextViewPacketPrice.setText(formattedTotalPrice);
            }
        });

        for(int i = 0; i < mPacket.getContents().size(); i++){
            TextView content = (TextView) getLayoutInflater().inflate(R.layout.itemview_packet_content, null);
            content.setText(mPacket.getContents().get(i));
            mPacketContents.addView(content);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Toast.makeText(this, "The year "+Integer.toString(year), Toast.LENGTH_SHORT).show();

        mTimePickerDialog.show(getFragmentManager(), "timepickerdialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

    }

    private void showAddressDialog() {
        final EditText dialogEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Alamat?")
                .setMessage("Masukkan alamat acara.")
                .setView(dialogEditText)
                .setPositiveButton("OKE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String address = String.valueOf(dialogEditText.getText());
                        Toast.makeText(PacketActivity.this, address, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
}

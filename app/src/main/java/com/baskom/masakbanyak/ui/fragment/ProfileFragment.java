package com.baskom.masakbanyak.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baskom.masakbanyak.MasakBanyakApplication;
import com.baskom.masakbanyak.model.Customer;
import com.baskom.masakbanyak.viewmodel.CustomerViewModel;
import com.baskom.masakbanyak.viewmodel.ViewModelFactory;
import com.baskom.masakbanyak.R;
import com.google.common.io.ByteStreams;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.baskom.masakbanyak.Constants.MASAKBANYAK_URL;
import static com.baskom.masakbanyak.Constants.PICK_IMAGE_REQUEST_CODE;

public class ProfileFragment extends Fragment {
  
  @Inject
  ViewModelFactory mViewModelFactory;
  
  private CustomerViewModel mCustomerViewModel;
  
  private Customer mCustomer;
  
  private CoordinatorLayout mCoordinatorLayout;
  private SwipeRefreshLayout mRefreshLayout;
  private ConstraintLayout mConstraintLayout;
  private CircleImageView mImageView;
  private EditText mEditTextName;
  private EditText mEditTextPhone;
  private EditText mEditTextEmail;
  private Button mButtonUpdateProfile;
  private Button mButtonLogout;
  
  private ProfileFragmentInteractionListener mListener;
  
  public static ProfileFragment newInstance() {
    return new ProfileFragment();
  }
  
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    MasakBanyakApplication.getInstance().getApplicationComponent().inject(this);
    
    mCustomerViewModel = ViewModelProviders.of(this, mViewModelFactory).get(CustomerViewModel.class);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_profile, container, false);
    
    mCoordinatorLayout = getActivity().findViewById(R.id.coordinatorLayout);
    mRefreshLayout = view.findViewById(R.id.refreshLayout);
    mConstraintLayout = view.findViewById(R.id.constraintLayout);
    mImageView = view.findViewById(R.id.image_view);
    mEditTextName = view.findViewById(R.id.edit_text_name);
    mEditTextPhone = view.findViewById(R.id.edit_text_phone);
    mEditTextEmail = view.findViewById(R.id.edit_text_email);
    mButtonUpdateProfile = view.findViewById(R.id.button_update);
    mButtonLogout = view.findViewById(R.id.button_logout);
    
    return view;
  }
  
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    
    mConstraintLayout.setVisibility(View.INVISIBLE);
    
    mRefreshLayout.setRefreshing(true);
    mRefreshLayout.setOnRefreshListener(mCustomerViewModel::refreshCustomer);
    
    mImageView.setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.setType("image/jpeg");
      intent.setAction(Intent.ACTION_GET_CONTENT);
      startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_CODE);
    });
    
    mButtonUpdateProfile.setOnClickListener(v -> {
      mRefreshLayout.setRefreshing(true);
      
      mCustomer.setName(mEditTextName.getText().toString());
      mCustomer.setPhone(mEditTextPhone.getText().toString());
      mCustomer.setEmail(mEditTextEmail.getText().toString());
      
      mCustomerViewModel.updateProfile(mCustomer);
    });
    
    mButtonLogout.setOnClickListener(v -> {
      mRefreshLayout.setRefreshing(true);
      
      mCustomerViewModel.logout();
    });
  }
  
  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    
    mCustomerViewModel.getCustomerLiveData().observe(this, customer -> {
      this.mCustomer = customer;
      Picasso.get().load(MASAKBANYAK_URL + customer.getAvatar()).into(mImageView);
      
      mEditTextName.setText(customer.getName());
      mEditTextPhone.setText(customer.getPhone());
      mEditTextEmail.setText(customer.getEmail());
      
      mRefreshLayout.setRefreshing(false);
      mConstraintLayout.setVisibility(View.VISIBLE);
    });
    
    mCustomerViewModel.getNotificationEventLiveData().observe(this, notificationEvent -> {
      String notification = notificationEvent.getContentIfNotHandled();
      
      if (notification != null) {
        Snackbar.make(mCoordinatorLayout, notification, Snackbar.LENGTH_LONG).show();
      }
    });
  }
  
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    
    mCustomerViewModel.getCustomerLiveData().removeObservers(this);
    mCustomerViewModel.getNotificationEventLiveData().removeObservers(this);
  }
  
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof ProfileFragmentInteractionListener) {
      mListener = (ProfileFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement CateringFragmentInteractionListener");
    }
  }
  
  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }
  
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    
    if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK &&
        data != null && data.getData() != null) {
      
      String filename = "";
      byte[] file = "".getBytes();
      
      Uri uri = data.getData();
      String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
      Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
      
      if (cursor != null && cursor.getCount() > 0) {
        int columnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        filename = cursor.getString(columnIndex);
      }
      
      cursor.close();
      
      try {
        InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
        file = ByteStreams.toByteArray(inputStream);
        inputStream.close();
      } catch (Exception e) {
        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
      }
      
      mCustomerViewModel.uploadProfileAvatar(mCustomer, filename, file);
    }
  }
  
  public interface ProfileFragmentInteractionListener {
    void onProfileFragmentInteraction();
  }
}

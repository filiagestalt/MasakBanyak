package com.baskom.masakbanyak.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.baskom.masakbanyak.MasakBanyakApplication;
import com.baskom.masakbanyak.model.Customer;
import com.baskom.masakbanyak.ui.ViewModelFactory;
import com.baskom.masakbanyak.ui.fragment.viewmodel.ProfileViewModel;
import com.baskom.masakbanyak.webservice.MasakBanyakWebService;
import com.baskom.masakbanyak.R;
import com.baskom.masakbanyak.ui.activity.LoginActivity;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.common.io.ByteStreams;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;
import static com.baskom.masakbanyak.Constants.MASAKBANYAK_URL;
import static com.baskom.masakbanyak.Constants.PICK_IMAGE_REQUEST_CODE;

public class ProfileFragment extends Fragment {
  
  private Customer mCustomer;
  
  @Inject
  ViewModelFactory mViewModelFactory;
  
  private ProfileViewModel mProfileViewModel;
  
  private CoordinatorLayout mCoordinatorLayout;
  private ConstraintLayout mConstraintLayout;
  private CircleImageView mImageView;
  private EditText mEditTextName;
  private EditText mEditTextPhone;
  private EditText mEditTextEmail;
  private Button mButtonUpdateProfile;
  private Button mButtonLogout;
  private ProgressBar mProgressBar;
  
  private ProfileFragmentInteractionListener mListener;
  
  public static ProfileFragment newInstance() {
    return new ProfileFragment();
  }
  
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    MasakBanyakApplication.getInstance().getApplicationComponent().inject(this);
    
    mProfileViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ProfileViewModel.class);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_profile, container, false);
    
    mCoordinatorLayout = getActivity().findViewById(R.id.coordinatorLayout);
    mConstraintLayout = view.findViewById(R.id.constraintLayoutButtons);
    mImageView = view.findViewById(R.id.image_view);
    mEditTextName = view.findViewById(R.id.edit_text_name);
    mEditTextPhone = view.findViewById(R.id.edit_text_phone);
    mEditTextEmail = view.findViewById(R.id.edit_text_email);
    mButtonUpdateProfile = view.findViewById(R.id.button_update);
    mButtonLogout = view.findViewById(R.id.button_logout);
    mProgressBar = view.findViewById(R.id.progress_bar);
    
    return view;
  }
  
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    
    FoldingCube foldingCube = new FoldingCube();
    foldingCube.setColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark));
    
    mProgressBar.setIndeterminateDrawable(foldingCube);
    
    mImageView.setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.setType("image/jpeg");
      intent.setAction(Intent.ACTION_GET_CONTENT);
      startActivityForResult(
          Intent.createChooser(intent, "Select Picture"),
          PICK_IMAGE_REQUEST_CODE
      );
    });
    
    mButtonUpdateProfile.setOnClickListener(v -> {
      mCustomer.setName(mEditTextName.getText().toString());
      mCustomer.setPhone(mEditTextPhone.getText().toString());
      mCustomer.setEmail(mEditTextEmail.getText().toString());
      
      mProfileViewModel.updateProfile(mCustomer);
    });
    
    mButtonLogout.setOnClickListener(v -> mProfileViewModel.logout());
    
    mImageView.setVisibility(View.INVISIBLE);
    mEditTextName.setVisibility(View.INVISIBLE);
    mEditTextPhone.setVisibility(View.INVISIBLE);
    mEditTextEmail.setVisibility(View.INVISIBLE);
    mConstraintLayout.setVisibility(View.INVISIBLE);
  }
  
  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    
    mProfileViewModel.getCustomerLiveData().observe(this, customer -> {
      this.mCustomer = customer;
      Picasso.get().load(MASAKBANYAK_URL + customer.getAvatar()).into(mImageView);
      
      mEditTextName.setText(customer.getName());
      mEditTextPhone.setText(customer.getPhone());
      mEditTextEmail.setText(customer.getEmail());
      
      mProgressBar.setVisibility(View.GONE);
      mImageView.setVisibility(View.VISIBLE);
      mEditTextName.setVisibility(View.VISIBLE);
      mEditTextPhone.setVisibility(View.VISIBLE);
      mEditTextEmail.setVisibility(View.VISIBLE);
      mConstraintLayout.setVisibility(View.VISIBLE);
    });
    
    mProfileViewModel.getNotificationEventLiveData().observe(this, notificationEvent -> {
      String notification = notificationEvent.getContentIfNotHandled();
      
      if (notification != null) {
        Snackbar.make(mCoordinatorLayout, notification, Snackbar.LENGTH_LONG).show();
      }
    });
  }
  
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    
    mProfileViewModel.getCustomerLiveData().removeObservers(this);
    mProfileViewModel.getNotificationEventLiveData().removeObservers(this);
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
      
      mProfileViewModel.uploadProfileAvatar(mCustomer, filename, file);
    }
  }
  
  public interface ProfileFragmentInteractionListener {
    void onProfileFragmentInteraction();
  }
}

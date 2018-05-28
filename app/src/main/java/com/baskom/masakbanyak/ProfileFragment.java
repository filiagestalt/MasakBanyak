package com.baskom.masakbanyak;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
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
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int PICK_IMAGE_REQUEST = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Customer mCustomer;

    private ProfileFragmentInteractionListener mListener;

    private CanMakeCall mCanMakeProfileCall = new CanMakeCall() {
        @Override
        public void makeCall(final Context context, MasakBanyakService service, String access_token) {
            Call<Customer> call = service.profile("Bearer "+access_token);

            call.enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {
                    if(response.isSuccessful()){
                        mCustomer = response.body();

                        Picasso.get().load(mCustomer.getAvatar()).into(mImageView);

                        mEditTextName.setText(mCustomer.getName());
                        mEditTextPhone.setText(mCustomer.getPhone());
                        mEditTextEmail.setText(mCustomer.getEmail());

                        mProgressBar.setVisibility(View.GONE);
                        mConstraintLayout.setVisibility(View.VISIBLE);
                    }else{
                        try {
                            Toast.makeText(
                                    context,
                                    response.errorBody().string(),
                                    Toast.LENGTH_LONG
                            ).show();
                            mProgressBar.setVisibility(View.GONE);
                        } catch (IOException e) {
                            Toast.makeText(
                                    context,
                                    e.toString(),
                                    Toast.LENGTH_LONG
                            ).show();
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Customer> call, Throwable t) {
                    Toast.makeText(
                            context,
                            t.toString(),
                            Toast.LENGTH_LONG
                    ).show();
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        }
    };

    private CircleImageView mImageView;
    private EditText mEditTextName;
    private EditText mEditTextPhone;
    private EditText mEditTextEmail;
    private ConstraintLayout mConstraintLayout;
    private Button mButtonUpdateProfile;
    private Button mButtonLogout;
    private ProgressBar mProgressBar;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mImageView = view.findViewById(R.id.image_view);
        mEditTextName = view.findViewById(R.id.edit_text_name);
        mEditTextPhone = view.findViewById(R.id.edit_text_phone);
        mEditTextEmail = view.findViewById(R.id.edit_text_email);

        mConstraintLayout = view.findViewById(R.id.constraint_layout_button_background);
        mConstraintLayout.setVisibility(View.INVISIBLE);

        mButtonUpdateProfile = view.findViewById(R.id.button_update);
        mButtonLogout = view.findViewById(R.id.button_logout);

        mImageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(
                                Intent.createChooser(intent, "Select Picture"),
                                PICK_IMAGE_REQUEST
                        );
                    }
                }
        );

        mButtonLogout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout(v.getContext());
                    }
                }
        );

        mProgressBar = view.findViewById(R.id.progress_bar);
        FoldingCube foldingCube = new FoldingCube();
        foldingCube.setColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark));
        mProgressBar.setIndeterminateDrawable(foldingCube);

        verifyAndExecuteCall(view.getContext(), mCanMakeProfileCall);

        return view;
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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        getActivity().getContentResolver(),
                        uri
                );

                mImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface ProfileFragmentInteractionListener {
        // TODO: Update argument type and name
        void onProfileFragmentInteraction(Uri uri);
    }

    public void verifyAndExecuteCall(final Context context, final CanMakeCall canMakeCall){
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.app_preference_key),
                Context.MODE_PRIVATE
        );
        final SharedPreferences.Editor editor = sharedPref.edit();

        String access_token_old = sharedPref.getString("access_token", null);
        String refresh_token = sharedPref.getString("refresh_token", null);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.33:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final MasakBanyakService service = retrofit.create(MasakBanyakService.class);

        JWT jwt = new JWT(access_token_old);

        if(jwt.isExpired(9)){
            Call<JsonObject> call = service.refresh(
                    refresh_token,
                    jwt.getClaim("customer_id").asString()
            );

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.isSuccessful()){
                        String access_token = response.body().get("access_token").getAsString();
                        editor.putString("access_token", access_token).apply();

                        canMakeCall.makeCall(context, service, access_token);
                    }else{
                        try {
                            Toast.makeText(
                                    context,
                                    response.errorBody().string(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        } catch (IOException e) {
                            Toast.makeText(
                                    context,
                                    e.toString(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(
                            context,
                            t.toString(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        }else{
            canMakeCall.makeCall(context, service, access_token_old);
        }
    }

    public void logout(final Context context){
        final SharedPreferences sharedPreferences = context.getSharedPreferences(
                getString(R.string.app_preference_key),
                Context.MODE_PRIVATE
        );
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        String accessToken = sharedPreferences.getString("access_token", null);
        String refreshToken = sharedPreferences.getString("refresh_token", null);
        String customer_id = new JWT(accessToken).getClaim("customer_id").asString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.33:3000/")
                .build();

        MasakBanyakService service = retrofit.create(MasakBanyakService.class);

        Call<ResponseBody> call = service.logout(refreshToken, customer_id);

        call.enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(
                            Call<ResponseBody> call,
                            Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            editor.clear();
                            editor.apply();

                            try {
                                Toast.makeText(
                                        context,
                                        response.body().string(),
                                        Toast.LENGTH_SHORT
                                ).show();
                            } catch (IOException e) {
                                Toast.makeText(
                                        context,
                                        e.toString(),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                            Intent intent = new Intent(context, PreLoginRegisterActivity.class);
                            intent.setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            getActivity().finish();
                        }else{
                            try {
                                Toast.makeText(
                                        context,
                                        response.errorBody().string(),
                                        Toast.LENGTH_SHORT
                                ).show();
                            } catch (IOException e) {
                                Toast.makeText(
                                        context,
                                        e.toString(),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<ResponseBody> call,
                            Throwable t) {
                        Toast.makeText(
                                context,
                                t.toString(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

}

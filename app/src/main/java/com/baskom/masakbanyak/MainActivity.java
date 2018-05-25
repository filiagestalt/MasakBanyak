package com.baskom.masakbanyak;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.RotatingCircle;
import com.google.common.collect.Collections2;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements HomeFragment.HomeFragmentInteractionListener,
        TransactionFragment.TransactionFragmentInteractionListener,
        CateringFragment.CateringFragmentInteractionListener,
        NotificationFragment.NotificationFragmentInteractionListener,
        ProfileFragment.ProfileFragmentInteractionListener,
        PacketFragment.PacketFragmentInteractionListener {

    private Toolbar mToolbar;
    private BottomNavigationView mBottomNavigation;
    private SearchView mSearchView;
    private ArrayList<Catering> mCaterings = new ArrayList<>();
    private ProgressBar mProgressBar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            Fragment fragment = manager.findFragmentById(R.id.content);

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (!(fragment instanceof HomeFragment)) {
                        transaction.replace(R.id.content, HomeFragment.newInstance(mCaterings));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    return true;
                case R.id.navigation_transaction:
                    if (!(fragment instanceof TransactionFragment)) {
                        transaction.replace(R.id.content, TransactionFragment.newInstance("01", "02"));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    return true;

                case R.id.navigation_notification:
                    if (!(fragment instanceof NotificationFragment)) {
                        transaction.replace(R.id.content, NotificationFragment.newInstance("01", "02"));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    return true;
                case R.id.navigation_profile:
                    if (!(fragment instanceof ProfileFragment)) {
                        transaction.replace(R.id.content, ProfileFragment.newInstance("01", "02"));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigation = findViewById(R.id.navigation);
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationHelper.removeShiftMode(mBottomNavigation);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mProgressBar = findViewById(R.id.progress_bar);
        RotatingCircle rotatingCircle = new RotatingCircle();
        rotatingCircle.setColor(R.color.colorAccentDark);
        mProgressBar.setIndeterminateDrawable(rotatingCircle);

        getCaterings(this);
    }

    @Override
    public void onBackPressed() {
        if(!mSearchView.isIconified()) {
            mSearchView.onActionViewCollapsed();
        }else {
            super.onBackPressed();

            FragmentManager manager = getSupportFragmentManager();
            Fragment fragment = manager.findFragmentById(R.id.content);
            if (fragment instanceof HomeFragment) {
                mBottomNavigation.setSelectedItemId(R.id.navigation_home);
            } else if (fragment instanceof TransactionFragment) {
                mBottomNavigation.setSelectedItemId(R.id.navigation_transaction);
            } else if (fragment instanceof NotificationFragment) {
                mBottomNavigation.setSelectedItemId(R.id.navigation_notification);
            } else if (fragment instanceof ProfileFragment) {
                mBottomNavigation.setSelectedItemId(R.id.navigation_profile);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                ArrayList<Catering> filteredCateringList = new ArrayList<>(Collections2
                        .filter(mCaterings, new CateringFilter(query)));
                transaction.replace(R.id.content, HomeFragment.newInstance(filteredCateringList));
                transaction.addToBackStack(null);
                transaction.commit();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHomeFragmentInteraction(Catering catering) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, CateringFragment.newInstance(catering));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onCateringFragmentInteraction(Packet packet) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment previousFragment = manager.findFragmentByTag("Packet");
        if(previousFragment != null){
            transaction.remove(previousFragment);
        }
        transaction.addToBackStack(null);
        PacketFragment fragment = PacketFragment.newInstance(packet);
        fragment.show(transaction, "Packet");
    }

    @Override
    public void onPacketFragmentInteraction(Packet packet) {

    }

    @Override
    public void onTransactionFragmentInteraction(Uri uri) {

    }

    @Override
    public void onNotificationFragmentInteraction(Uri uri) {

    }

    @Override
    public void onProfileFragmentInteraction(Uri uri) {

    }

    public void getCaterings(final Context context){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string
                .app_preference_key), Context.MODE_PRIVATE);
        final String accessTokenOld = sharedPref.getString("access_token", null);
        final String refreshToken = sharedPref.getString("refresh_token", null);
        final SharedPreferences.Editor editor = sharedPref.edit();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.33:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final MasakBanyakService service = retrofit.create(MasakBanyakService.class);

        //Access token check, if expired then refresh
        final JWT jwt = new JWT(accessTokenOld);
        if(jwt.isExpired(9)){
            service.refresh(
                    refreshToken,
                    jwt.getClaim("customer_id").asString()
            ).enqueue(
                    new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if(response.code() == 200){
                                String accessToken = response.body().get("access_token").getAsString();
                                editor.putString("access_token", accessToken).apply();

                                service.caterings("Bearer "+accessToken).enqueue(
                                        new Callback<ArrayList<Catering>>() {
                                            @Override
                                            public void onResponse(Call<ArrayList<Catering>> call,
                                                                   Response<ArrayList<Catering>> response) {
                                                if(response.code() == 200){
                                                    mCaterings = response.body();
                                                    FragmentManager manager = getSupportFragmentManager();
                                                    FragmentTransaction transaction = manager.beginTransaction();
                                                    transaction.replace(R.id.content, HomeFragment.newInstance(response.body()));
                                                    transaction.commit();
                                                    mProgressBar.setVisibility(View.GONE);
                                                }else{
                                                    try {
                                                        Toast.makeText(
                                                                context,
                                                                response.errorBody().string(),
                                                                Toast.LENGTH_LONG
                                                        ).show();
                                                    } catch (IOException e) {
                                                        Toast.makeText(
                                                                context,
                                                                e.toString(),
                                                                Toast.LENGTH_LONG
                                                        ).show();
                                                    }
                                                    mProgressBar.setVisibility(View.GONE);
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ArrayList<Catering>> call, Throwable t) {
                                                Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
                                                mProgressBar.setVisibility(View.GONE);
                                            }
                                        });
                            }else{
                                try {
                                    Toast.makeText(
                                            context,
                                            response.errorBody().string(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                } catch (IOException e) {
                                    Toast.makeText(
                                            context,
                                            e.toString(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                                mProgressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Toast.makeText(
                                    context,
                                    t.toString(),
                                    Toast.LENGTH_LONG
                            ).show();
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }
            );
        }else{
            service.caterings("Bearer "+accessTokenOld).enqueue(
                    new Callback<ArrayList<Catering>>() {
                        @Override
                        public void onResponse(
                                Call<ArrayList<Catering>> call,
                                Response<ArrayList<Catering>> response) {
                            if(response.code() == 200){
                                mCaterings = response.body();
                                FragmentManager manager = getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(
                                        R.id.content,
                                        HomeFragment.newInstance(response.body())
                                );
                                transaction.commit();
                                mProgressBar.setVisibility(View.GONE);
                            }else{
                                try {
                                    Toast.makeText(
                                            context,
                                            response.errorBody().string(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                } catch (IOException e) {
                                    Toast.makeText(
                                            context,
                                            e.toString(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                                mProgressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Catering>> call, Throwable t) {
                            Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
                            mProgressBar.setVisibility(View.GONE);
                        }
            });
        }

    }
}
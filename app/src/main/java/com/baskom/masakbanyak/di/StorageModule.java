package com.baskom.masakbanyak.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.baskom.masakbanyak.R;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ApplicationModule.class})
public class StorageModule {
    @Provides
    @Singleton
    public SharedPreferences providePreferences(Context context) {
        return context.getSharedPreferences(
                context.getString(R.string.app_preferences_key),
                Context.MODE_PRIVATE
        );
    }

    @Provides
    @Singleton
    public SharedPreferences.Editor providePreferencesEditor(SharedPreferences preferences) {
        return preferences.edit();
    }

//    @Provides
//    @Singleton
//    public CustomerRepository providesCustomerRepository(
//            MasakBanyakWebService webservice,
//            SharedPreferences preferences,
//            SharedPreferences.Editor preferencesEditor,
//            JWT jwt
//    ){
//        return new CustomerRepository(webservice, preferences, preferencesEditor, jwt);
//    }
}

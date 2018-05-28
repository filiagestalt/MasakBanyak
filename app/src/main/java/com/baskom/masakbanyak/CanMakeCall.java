package com.baskom.masakbanyak;

import android.content.Context;

public interface CanMakeCall {
    void makeCall(Context context, MasakBanyakService service, String access_token);
}

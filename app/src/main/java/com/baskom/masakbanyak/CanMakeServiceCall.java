package com.baskom.masakbanyak;

import android.content.Context;

@FunctionalInterface
public interface CanMakeServiceCall {
    void makeCall(Context context, MasakBanyakService service, String access_token);
}

package com.baskom.masakbanyak;

import android.content.Context;

@FunctionalInterface
public interface CanMakeServiceCall {
    void makeCall(MasakBanyakService service, String access_token);
}

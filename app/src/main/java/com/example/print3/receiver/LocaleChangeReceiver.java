package com.example.print3.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.print3.util.*;

public class LocaleChangeReceiver extends BroadcastReceiver {

    private final static String TAG = "LocaleChangeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        LogUtils.d("", "mReceiver  onReceive  intent.getAction(): " + intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
            LogUtils.e(TAG,"Language change");

        }
    }
}

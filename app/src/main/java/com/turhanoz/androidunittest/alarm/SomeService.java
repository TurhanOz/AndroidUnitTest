package com.turhanoz.androidunittest.alarm;

import android.app.IntentService;
import android.content.Intent;

public class SomeService extends IntentService {

    public SomeService() {
        super("SomeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //doSomeTask
    }
}

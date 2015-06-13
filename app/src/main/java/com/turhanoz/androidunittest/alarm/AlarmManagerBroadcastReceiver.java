package com.turhanoz.androidunittest.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    AlarmController alarmController;

    public AlarmManagerBroadcastReceiver() {
        alarmController = new AlarmController();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            alarmController.setAlarm(context);
        }

        Intent service = new Intent(context, SomeService.class);
        context.startService(service);
    }
}

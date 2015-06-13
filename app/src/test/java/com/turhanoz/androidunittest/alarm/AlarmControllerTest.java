package com.turhanoz.androidunittest.alarm;

import android.app.AlarmManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;

import com.turhanoz.androidunittest.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowPendingIntent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AlarmControllerTest {
    Context context;
    ShadowAlarmManager shadowAlarmManager;
    AlarmController sut;

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application.getApplicationContext();
        AlarmManager alarmManager = (AlarmManager) RuntimeEnvironment.application.getSystemService(Context.ALARM_SERVICE);
        shadowAlarmManager = shadowOf(alarmManager);

        sut = new AlarmController();
    }

    @Test
    public void shouldScheduleAlarmEveryHourStartingInOneHour() throws Exception {
        assertNull(shadowAlarmManager.getNextScheduledAlarm());

        sut.setAlarm(context);

        ShadowAlarmManager.ScheduledAlarm scheduledAlarm = shadowAlarmManager.getNextScheduledAlarm();
        assertEquals(AlarmController.INTERVAL_ONE_HOUR, scheduledAlarm.interval);
        assertEquals(SystemClock.elapsedRealtime() + AlarmController.INTERVAL_ONE_HOUR, scheduledAlarm.triggerAtTime);
        assertEquals(AlarmManager.ELAPSED_REALTIME, scheduledAlarm.type);
    }

    @Test
    public void shouldCancelPreviousAlarmsAndKeepLatest() throws Exception {
        sut.setAlarm(context);
        sut.setAlarm(context);
        sut.setAlarm(context);

        assertEquals(1, shadowAlarmManager.getScheduledAlarms().size());
    }

    @Test
    public void shouldTriggerBroadcastReceiverWhenTimeElapsed() throws Exception {
        Intent expectedIntent = new Intent(context, AlarmManagerBroadcastReceiver.class);

        sut.setAlarm(context);

        ShadowAlarmManager.ScheduledAlarm scheduledAlarm = shadowAlarmManager.getNextScheduledAlarm();
        ShadowPendingIntent shadowPendingIntent = shadowOf(scheduledAlarm.operation);
        //more on pendingIntent :
        //https://github.com/robolectric/robolectric/blob/master/robolectric/src/test/java/org/robolectric/shadows/ShadowPendingIntentTest.java
        assertTrue(shadowPendingIntent.isBroadcastIntent());
        assertEquals(1, shadowPendingIntent.getSavedIntents().length);
        assertEquals(expectedIntent.getComponent(), shadowPendingIntent.getSavedIntents()[0].getComponent());
    }

    @Test
    public void broadcastReceiverShouldBeDisabledInitialy() throws Exception {
        ComponentName receiver = new ComponentName(context, AlarmManagerBroadcastReceiver.class);
        assertNull(RuntimeEnvironment.getRobolectricPackageManager().getComponentState(receiver));
    }

    @Test
    public void broadcastReceiverShouldBeRegisteredWhenSet() throws Exception {
        ComponentName receiver = new ComponentName(context, AlarmManagerBroadcastReceiver.class);

        sut.enableAlarmOnBootComplete(context);

        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                RuntimeEnvironment.getRobolectricPackageManager().getComponentState(receiver).newState);
    }

    @Test
    public void broadcastReceiverShouldBeUnregisteredWhenUnSet() throws Exception {
        ComponentName receiver = new ComponentName(context, AlarmManagerBroadcastReceiver.class);

        sut.disableAlarmBootComplete(context);

        assertEquals(PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                RuntimeEnvironment.getRobolectricPackageManager().getComponentState(receiver).newState);
    }
}
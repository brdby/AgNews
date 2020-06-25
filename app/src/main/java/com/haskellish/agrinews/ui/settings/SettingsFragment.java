package com.haskellish.agrinews.ui.settings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.haskellish.agrinews.NewsApp;
import com.haskellish.agrinews.R;
import com.haskellish.agrinews.notifications.TimeNotification;

import java.util.Calendar;

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        TimePickerDialog.OnTimeSetListener {

    Calendar dateAndTime = Calendar.getInstance();
    SharedPreferences sPref;

    Context context;

    public final static long MILLIS_IN_DAY = 1000*60*60*24;
    public final static String SAVED_MINUTES = "SAVED_MINUTES";
    public final static String SAVED_HOUR = "SAVED_HOUR";

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("switchNotifications")){
            Preference changeTime = findPreference(s);
            assert changeTime != null;
            boolean isChecked = sharedPreferences.getBoolean("switchNotifications", false);
            if (isChecked) startNotify(); else stopNotify();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sPref = getPreferenceManager().getSharedPreferences();
        sPref.registerOnSharedPreferenceChangeListener(this);

        getSavedTime(dateAndTime);

        Preference mngLinks = findPreference("mngRSS");
        assert mngLinks != null;
        mngLinks.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent =
                        new Intent(context, ManageRSSActivity.class);
                startActivity(intent);
                return true;
            }
        });

        Preference mngKeywords = findPreference("mngKeywords");
        assert mngKeywords != null;
        mngKeywords.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent =
                        new Intent(context, ManageKeywordsActivity.class);
                startActivity(intent);
                return true;
            }
        });

        Preference mngTime = findPreference("mngTime");
        assert mngTime != null;
        mngTime.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                TimePickerDialog t =
                        new TimePickerDialog(context,
                                SettingsFragment.this,
                                dateAndTime.get(Calendar.HOUR_OF_DAY),
                                dateAndTime.get(Calendar.MINUTE),
                                true);
                t.show();
                return true;
            }
        });
    }

    public void startNotify() {
        AlarmManager am = (AlarmManager) NewsApp.getInstance().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TimeNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent,0);
        am.cancel(pendingIntent);
        am.set(AlarmManager.RTC_WAKEUP, dateAndTime.getTimeInMillis(), pendingIntent);
    }

    public void stopNotify(){
        AlarmManager am = (AlarmManager) NewsApp.getInstance().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TimeNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent,0);
        am.cancel(pendingIntent);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        setTime(dateAndTime, i, i1);
        saveTime(dateAndTime);
        startNotify();
    }

    private void setTime(Calendar dateAndTime, int minutes, int hours){
        dateAndTime.setTimeInMillis(System.currentTimeMillis());
        dateAndTime.set(Calendar.HOUR_OF_DAY, minutes);
        dateAndTime.set(Calendar.MINUTE, hours);
        if (dateAndTime.getTimeInMillis() < System.currentTimeMillis()) {
            dateAndTime.setTimeInMillis(dateAndTime.getTimeInMillis() + MILLIS_IN_DAY);
        }
    }

    private void saveTime(Calendar dateAndTime){
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt(SAVED_MINUTES, dateAndTime.get(Calendar.MINUTE));
        ed.putInt(SAVED_HOUR, dateAndTime.get(Calendar.HOUR_OF_DAY));
        ed.apply();
    }

    private void getSavedTime(Calendar dateAndTime){
        int minutes = sPref.getInt(SAVED_MINUTES, 0);
        int hours = sPref.getInt(SAVED_HOUR, 0);
        setTime(dateAndTime, minutes, hours);
    }
}
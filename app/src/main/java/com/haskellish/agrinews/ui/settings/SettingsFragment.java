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

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.haskellish.agrinews.R;
import com.haskellish.agrinews.notifications.TimeNotification;

import java.util.Calendar;

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        TimePickerDialog.OnTimeSetListener {

    Calendar dateAndTime = Calendar.getInstance();
    SharedPreferences sPref;

    public final static String SAVED_TIME = "SAVED_TIME";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("switchNotifications")){
            Preference changeTime = findPreference(s);
            assert changeTime != null;
            System.out.println(changeTime.isEnabled());
            if (changeTime.isEnabled()) startNotify();
            else stopNotify();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sPref = getPreferenceManager().getSharedPreferences();
        sPref.registerOnSharedPreferenceChangeListener(this);

        dateAndTime.setTimeInMillis(getTime());

        Preference mngLinks = findPreference("mngRSS");
        assert mngLinks != null;
        mngLinks.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent =
                        new Intent(SettingsFragment.this.getActivity(), ManageRSSActivity.class);
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
                        new Intent(SettingsFragment.this.getActivity(), ManageKeywordsActivity.class);
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
                        new TimePickerDialog(SettingsFragment.this.getActivity(),
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
        AlarmManager am = (AlarmManager) this.getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this.getActivity(), TimeNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getActivity(), 0,
                intent,0);
        am.cancel(pendingIntent);
        am.set(AlarmManager.RTC_WAKEUP, dateAndTime.getTimeInMillis(), pendingIntent);
    }

    public void stopNotify(){
        AlarmManager am = (AlarmManager) this.getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this.getActivity(), TimeNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getActivity(), 0,
                intent,0);
        am.cancel(pendingIntent);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        dateAndTime.set(Calendar.HOUR_OF_DAY, i);
        dateAndTime.set(Calendar.MINUTE, i1);
        saveTime(dateAndTime.getTimeInMillis());
        startNotify();
    }

    private void saveTime(long value){
        SharedPreferences.Editor ed = sPref.edit();
        ed.putLong(SAVED_TIME, value);
        ed.apply();
    }

    private long getTime(){
        return sPref.getLong(SAVED_TIME, System.currentTimeMillis());
    }
}
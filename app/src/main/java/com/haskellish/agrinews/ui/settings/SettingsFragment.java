package com.haskellish.agrinews.ui.settings;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.haskellish.agrinews.R;
import com.haskellish.agrinews.notifications.TimeNotification;

import java.sql.Timestamp;
import java.util.Calendar;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    Calendar dateAndTime = Calendar.getInstance();
    AlarmManager am;
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    Intent intent;
    PendingIntent pendingIntent;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference changeTime = findPreference(s);
        assert changeTime != null;
        if (changeTime.isEnabled()) startNotify();
        else stopNotify();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1001", "name", importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        Preference mngLinks = findPreference("mngRSS");
        mngLinks.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(SettingsFragment.this.getActivity(), ManageRSSActivity.class);
                startActivity(intent);
                return true;
            }
        });

        Preference mngKeywords = findPreference("mngKeywords");
        mngKeywords.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(SettingsFragment.this.getActivity(), ManageKeywordsActivity.class);
                startActivity(intent);
                return true;
            }
        });

        Preference mngTime = findPreference("mngTime");
        mngTime.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new TimePickerDialog(SettingsFragment.this.getActivity(), null,
                        dateAndTime.get(Calendar.HOUR_OF_DAY),
                        dateAndTime.get(Calendar.MINUTE), true)
                        .show();
                return true;
            }
        });

        am = (AlarmManager) this.getContext().getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(this.getContext(), TimeNotification.class);
        pendingIntent = PendingIntent.getBroadcast(this.getContext(), 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT );
    }

    public void startNotify() {
        // На случай, если мы ранее запускали активити, а потом поменяли время,
        // откажемся от уведомления
        am.cancel(pendingIntent);
        // Устанавливаем разовое напоминание
        am.set(AlarmManager.RTC_WAKEUP, timestamp.getTime(), pendingIntent);
    }

    public void stopNotify(){
        am.cancel(pendingIntent);
    }
}
package com.haskellish.agrinews.ui.settings;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.haskellish.agrinews.R;
import com.haskellish.agrinews.ui.MainActivity;

import java.util.Calendar;

public class SettingsFragment extends Fragment {

    Button mngLinks;
    Button mngKeywords;
    Button chgTime;

    Calendar dateAndTime = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mngLinks = view.findViewById(R.id.mngLinks);
        mngLinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsFragment.this.getActivity(), ManageRSSActivity.class);
                startActivity(intent);
            }
        });

        mngKeywords = view.findViewById(R.id.mngKeywords);
        mngKeywords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsFragment.this.getActivity(), ManageKeywordsActivity.class);
                startActivity(intent);
            }
        });

        chgTime = view.findViewById(R.id.changeTime);
        chgTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(SettingsFragment.this.getActivity(), null,
                        dateAndTime.get(Calendar.HOUR_OF_DAY),
                        dateAndTime.get(Calendar.MINUTE), true)
                        .show();
            }
        });
    }
}
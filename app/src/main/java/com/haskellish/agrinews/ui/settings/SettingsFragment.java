package com.haskellish.agrinews.ui.settings;

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

public class SettingsFragment extends Fragment implements View.OnClickListener {
    final private String[] catNames = new String[] {
            "Рыжик", "Барсик", "Мурзик", "sample1", "sample2", "sample3"
    };

    Button mngLinks;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mngLinks = getActivity().findViewById(R.id.mngLinks);
        mngLinks.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this.getContext(), ManageRSSActivity.class);
        startActivity(intent);
    }
}
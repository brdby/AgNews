package com.haskellish.agrinews;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class NewsFragment extends ListFragment implements AdapterView.OnItemClickListener {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        HashMap<String, String> map;

        map = new HashMap<>();
        map.put("Header", "1");
        map.put("Content", "Мурзик");
        arrayList.add(map);

        map = new HashMap<>();
        map.put("Header", "2");
        map.put("Content", "Барсик");
        arrayList.add(map);

        map = new HashMap<>();
        map.put("Header", "3");
        map.put("Content", "Васька");
        arrayList.add(map);

        SimpleAdapter adapter = new SimpleAdapter(getActivity().getApplicationContext(), arrayList,
                R.layout.news_item,
                new String[]{"Header", "Content"},
                new int[]{R.id.news_header, R.id.news_content});

        setListAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}

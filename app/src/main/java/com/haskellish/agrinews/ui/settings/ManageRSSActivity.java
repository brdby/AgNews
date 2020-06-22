package com.haskellish.agrinews.ui.settings;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.haskellish.agrinews.NewsApp;
import com.haskellish.agrinews.R;
import com.haskellish.agrinews.db.*;
import com.haskellish.agrinews.db.entity.RSS;

import java.util.ArrayList;
import java.util.List;

public class ManageRSSActivity extends Activity implements View.OnClickListener {

    NewsDB db;

    TextInputEditText textInputEditText;
    Button add, delete;
    ListView listView;
    ArrayList<String> linksArr = new ArrayList<>();
    ArrayAdapter<String> listAdapter;
    ArrayList<String> checkedRSS = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_rss);

        textInputEditText = findViewById(R.id.rssInput);
        add = findViewById(R.id.add_button);
        add.setOnClickListener(this);
        delete = findViewById(R.id.delete_button);
        delete.setOnClickListener(this);
        listView = findViewById(R.id.recyclerView);
        db = NewsApp.getInstance().getDatabase();

        RSSDao rssDao = db.rssDao();
        List<RSS> links = rssDao.getAll();
        for (int i = 0; i < links.size(); i++){
            linksArr.add(links.get(i).url);
        }

        listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, linksArr);
        listView.setAdapter(listAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selItem = (String) adapterView.getItemAtPosition(i);
                if (checkedRSS.contains(selItem)) checkedRSS.remove(selItem);
                else checkedRSS.add(selItem);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_button:
                if (textInputEditText.getText() != null
                        && !textInputEditText.getText().toString().equals("")
                        && !linksArr.contains(textInputEditText.getText().toString())){
                    RSSDao rssDao = db.rssDao();
                    RSS rss = new RSS();
                    rss.url = textInputEditText.getText().toString();
                    rssDao.insert(rss);
                    linksArr.add(rss.url);
                    listAdapter.notifyDataSetChanged();
                }
                break;

            case R.id.delete_button:
                RSSDao rssDao = db.rssDao();
                for (String s : checkedRSS) {
                    rssDao.deleteByURL(s);
                    linksArr.remove(s);
                }
                listView.clearChoices();
                listAdapter.notifyDataSetChanged();
                break;
        }
    }



}

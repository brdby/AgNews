package com.haskellish.agnews.ui.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.haskellish.agnews.NewsApp;
import com.haskellish.agnews.R;
import com.haskellish.agnews.db.*;
import com.haskellish.agnews.db.DAO.RSSDao;
import com.haskellish.agnews.db.entity.RSS;

import java.util.ArrayList;
import java.util.List;

public class ManageRSSActivity extends Activity implements View.OnClickListener {

    /**
     * Activity where user can add delete RSS links from database
     */

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

        //initializing views
        textInputEditText = findViewById(R.id.rssInput);
        add = findViewById(R.id.rss_add_button);
        add.setOnClickListener(this);
        delete = findViewById(R.id.rss_delete_button);
        delete.setOnClickListener(this);
        listView = findViewById(R.id.RSSRecyclerView);
        db = NewsApp.getInstance().getDatabase();
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

        getRSS();
    }

    /**
     * Get all RSS links from database
     */
    private void getRSS() {
        RSSDao rssDao = db.rssDao();
        List<RSS> links = rssDao.getAll();
        for (int i = 0; i < links.size(); i++){
            linksArr.add(links.get(i).url);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rss_add_button:
                addRSS();
                break;

            case R.id.rss_delete_button:
                deleteRSS();
                break;
        }
    }

    /**
     * Delete all chosen RSS links from database
     */
    private void deleteRSS() {
        RSSDao rssDao = db.rssDao();
        for (String s : checkedRSS) {
            rssDao.deleteByURL(s);
            linksArr.remove(s);
        }
        listView.clearChoices();
        listAdapter.notifyDataSetChanged();
    }

    /**
     * Add RSS link from input field to database
     */
    private void addRSS() {
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
    }
}

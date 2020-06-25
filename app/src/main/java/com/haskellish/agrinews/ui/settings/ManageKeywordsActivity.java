package com.haskellish.agrinews.ui.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.haskellish.agrinews.NewsApp;
import com.haskellish.agrinews.R;
import com.haskellish.agrinews.db.DAO.KeywordDao;
import com.haskellish.agrinews.db.NewsDB;
import com.haskellish.agrinews.db.entity.Keyword;

import java.util.ArrayList;
import java.util.List;

public class ManageKeywordsActivity extends Activity implements View.OnClickListener {

    NewsDB db;

    TextInputEditText textInputEditText;
    Button add, delete;
    ListView listView;
    ArrayList<String> keywordsArr = new ArrayList<>();
    ArrayAdapter<String> listAdapter;
    ArrayList<String> checkedKeywords = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_keywords);


        //initializing views
        textInputEditText = findViewById(R.id.KeywordsInput);
        add = findViewById(R.id.keywords_add_button);
        add.setOnClickListener(this);
        delete = findViewById(R.id.keywords_delete_button);
        delete.setOnClickListener(this);
        listView = findViewById(R.id.KeywordsRecyclerView);
        db = NewsApp.getInstance().getDatabase();

        //initializing listView
        listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, keywordsArr);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selItem = (String) adapterView.getItemAtPosition(i);
                if (checkedKeywords.contains(selItem)) checkedKeywords.remove(selItem);
                else checkedKeywords.add(selItem);
            }
        });

        getKeywords();
    }

    private void getKeywords() {
        KeywordDao keywordDao = db.keywordDao();
        List<Keyword> links = keywordDao.getAll();
        for (int i = 0; i < links.size(); i++){
            keywordsArr.add(links.get(i).word);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.keywords_add_button:
                addKeyword();
                break;

            case R.id.keywords_delete_button:
                deleteKeywords();
                break;
        }
    }

    private void deleteKeywords() {
        KeywordDao keywordDao = db.keywordDao();
        for (String s : checkedKeywords) {
            keywordDao.deleteByWord(s);
            keywordsArr.remove(s);
        }
        listView.clearChoices();
        listAdapter.notifyDataSetChanged();
    }

    private void addKeyword() {
        if (textInputEditText.getText() != null
                && !textInputEditText.getText().toString().equals("")
                && !keywordsArr.contains(textInputEditText.getText().toString())){
            KeywordDao keywordDao = db.keywordDao();
            Keyword keyword = new Keyword();
            keyword.word = textInputEditText.getText().toString();
            keywordDao.insert(keyword);
            keywordsArr.add(keyword.word);
            listAdapter.notifyDataSetChanged();
        }
    }
}

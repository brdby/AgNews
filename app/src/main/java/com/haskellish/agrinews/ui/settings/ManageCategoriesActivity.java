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
import com.haskellish.agrinews.db.DAO.CategoryDAO;
import com.haskellish.agrinews.db.DAO.KeywordDao;
import com.haskellish.agrinews.db.NewsDB;
import com.haskellish.agrinews.db.entity.Category;
import com.haskellish.agrinews.db.entity.Keyword;

import java.util.ArrayList;
import java.util.List;

public class ManageCategoriesActivity extends Activity implements View.OnClickListener {

    NewsDB db;

    TextInputEditText textInputEditText;
    Button add, delete;
    ListView listView;
    ArrayList<String> categoriesArr = new ArrayList<>();
    ArrayAdapter<String> listAdapter;
    ArrayList<String> checkedCategories = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_categories);

        textInputEditText = findViewById(R.id.categoriesInput);
        add = findViewById(R.id.categories_add_button);
        add.setOnClickListener(this);
        delete = findViewById(R.id.categories_delete_button);
        delete.setOnClickListener(this);
        listView = findViewById(R.id.categoriesRecyclerView);
        db = NewsApp.getInstance().getDatabase();

        CategoryDAO categoryDAO = db.categoryDAO();
        List<Category> categories = categoryDAO.getAll();
        for (int i = 0; i < categories.size(); i++){
            categoriesArr.add(categories.get(i).word);
        }

        listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, categoriesArr);
        listView.setAdapter(listAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selItem = (String) adapterView.getItemAtPosition(i);
                if (checkedCategories.contains(selItem)) checkedCategories.remove(selItem);
                else checkedCategories.add(selItem);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.categories_add_button:
                if (textInputEditText.getText() != null
                        && !textInputEditText.getText().toString().equals("")
                        && !categoriesArr.contains(textInputEditText.getText().toString())){
                    CategoryDAO categoryDAO = db.categoryDAO();
                    Category category = new Category();
                    category.word = textInputEditText.getText().toString();
                    categoryDAO.insert(category);
                    categoriesArr.add(category.word);
                    listAdapter.notifyDataSetChanged();
                }
                break;

            case R.id.categories_delete_button:
                CategoryDAO categoryDAO = db.categoryDAO();
                for (String s : checkedCategories) {
                    categoryDAO.deleteByWord(s);
                    categoriesArr.remove(s);
                }
                listView.clearChoices();
                listAdapter.notifyDataSetChanged();
                break;
        }
    }
}
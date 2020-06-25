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
import com.haskellish.agnews.db.DAO.CategoryDAO;
import com.haskellish.agnews.db.NewsDB;
import com.haskellish.agnews.db.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class ManageCategoriesActivity extends Activity implements View.OnClickListener {
    /**
     * Activity where user can add delete categories from database
     */

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

        //initializing views
        textInputEditText = findViewById(R.id.categoriesInput);
        add = findViewById(R.id.categories_add_button);
        add.setOnClickListener(this);
        delete = findViewById(R.id.categories_delete_button);
        delete.setOnClickListener(this);
        listView = findViewById(R.id.categoriesRecyclerView);
        db = NewsApp.getInstance().getDatabase();
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

        getCategories();
    }

    /**
     * Get all categories from database
     */
    private void getCategories() {
        CategoryDAO categoryDAO = db.categoryDAO();
        List<Category> categories = categoryDAO.getAll();
        for (int i = 0; i < categories.size(); i++){
            categoriesArr.add(categories.get(i).word);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.categories_add_button:
                addCategory();
                break;

            case R.id.categories_delete_button:
                deleteCategories();
                break;
        }
    }

    /**
     * Delete all chosen categories from database
     */
    private void deleteCategories() {
        CategoryDAO categoryDAO = db.categoryDAO();
        for (String s : checkedCategories) {
            categoryDAO.deleteByWord(s);
            categoriesArr.remove(s);
        }
        listView.clearChoices();
        listAdapter.notifyDataSetChanged();
    }

    /**
     * Add category from input field to database
     */
    private void addCategory() {
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
    }
}
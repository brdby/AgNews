package com.haskellish.agrinews.ui.news;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.haskellish.agrinews.NewsApp;
import com.haskellish.agrinews.R;
import com.haskellish.agrinews.db.DAO.CategoryDAO;
import com.haskellish.agrinews.db.DAO.KeywordDao;
import com.haskellish.agrinews.db.NewsDB;
import com.haskellish.agrinews.db.DAO.RSSDao;
import com.haskellish.agrinews.db.entity.Category;
import com.haskellish.agrinews.db.entity.Keyword;
import com.haskellish.agrinews.db.entity.RSS;
import com.haskellish.agrinews.rss.News;
import com.haskellish.agrinews.rss.RSSParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsFragment extends ListFragment {
    SimpleAdapter adapter;
    ArrayList<HashMap<String, String>> adapterNewsList = new ArrayList<>();
    List<News> newsList = new ArrayList<>();
    List<String> keywordsList = new ArrayList<>();
    List<String> categoriesList = new ArrayList<>();

    Context context;

    class ParsingTask extends AsyncTask<String, Void, Void> {

        public Void doInBackground(String... strings) {
            HashMap<String, String> map;
            String url = strings[0];
            RSSParser rssParser = new RSSParser(url);
            List<News> newNews = rssParser.getNews();
            if (newNews != null){
                for (News news : newNews){
                    //check for keywords
                    boolean matchesKeywords = false;
                    for (String k : keywordsList){
                        if (news.getTitle().toLowerCase().contains(k.toLowerCase()) ||
                                news.getDescription().toLowerCase().contains(k.toLowerCase()))
                            matchesKeywords = true;
                    }

                    //check for categories
                    boolean matchesCategories = false;
                    for (String cl : categoriesList){
                        for (String c : news.getCategories()){
                            if (c.toLowerCase().contains(cl.toLowerCase())) matchesCategories = true;
                        }
                    }

                    if ((matchesKeywords || keywordsList.isEmpty())
                            && (matchesCategories || categoriesList.isEmpty())){
                        newsList.add(news);
                        map = new HashMap<>();
                        map.put("Header", news.getTitle());
                        map.put("Content", news.getDescription());
                        adapterNewsList.add(map);
                    }
                }
            }
            return null;
        }

        public void onPostExecute(Void o) {
            super.onPostExecute(o);
            NewsFragment.this.adapter.notifyDataSetChanged();
            if (NewsFragment.this.isAdded()) NewsFragment.this.setListShown(true);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setEmptyText(getResources().getString(R.string.empty_news_list));

        //initialize adapter
        adapter = new SimpleAdapter(getContext(), adapterNewsList,
                R.layout.news_item,
                new String[]{"Header"},
                new int[]{R.id.news_header});
        setListAdapter(adapter);

        //initialize keywords
        NewsDB db = NewsApp.getInstance().getDatabase();
        KeywordDao keywordDao = db.keywordDao();
        for (Keyword k : keywordDao.getAll()){
            keywordsList.add(k.word);
        }

        //initialize categories
        CategoryDAO categoryDAO = db.categoryDAO();
        for (Category c : categoryDAO.getAll()){
            categoriesList.add(c.word);
        }

        //initialize news
        RSSDao rssDao = db.rssDao();
        List<RSS> links = rssDao.getAll();
        if (!links.isEmpty()) setListShown(false);
        for (RSS rss : links){
            ParsingTask updateTask = new ParsingTask();
            updateTask.execute(rss.url);
        }
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        HashMap<String,String> map = (HashMap<String, String>) adapter.getItem(position);
        String title = map.get("Header");
        News news = null;
        for (News n : newsList){
            if (n.getTitle().equals(title)) news = n;
        }
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("newsObject", news);
        startActivity(intent);
    }
}
package com.haskellish.agrinews.ui.news;

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
import com.haskellish.agrinews.db.KeywordDao;
import com.haskellish.agrinews.db.NewsDB;
import com.haskellish.agrinews.db.RSSDao;
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
    List<String> keyWordsList = new ArrayList<>();

    class ParsingTask extends AsyncTask<String, Void, Void> {

        public Void doInBackground(String... strings) {
            HashMap<String, String> map;
            String url = strings[0];
            RSSParser rssParser = new RSSParser(url);
            List<News> newNews = rssParser.getNews();
            if (newNews != null){
                for (News news : newNews){
                    //check for keywords
                    boolean matchesKeyword = false;
                    for (String k : keyWordsList){
                        if (news.getTitle().toLowerCase().contains(k.toLowerCase()) ||
                                news.getDescription().toLowerCase().contains(k.toLowerCase()))
                            matchesKeyword = true;
                    }
                    if (matchesKeyword || keyWordsList.isEmpty()){
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize adapter
        adapter = new SimpleAdapter(getActivity().getApplicationContext(), adapterNewsList,
                R.layout.news_item,
                new String[]{"Header", "Content"},
                new int[]{R.id.news_header, R.id.news_content});
        setListAdapter(adapter);

        //initialize keywords
        NewsDB db = NewsApp.getInstance().getDatabase();
        KeywordDao keywordDao = db.keywordDao();
        for (Keyword k : keywordDao.getAll()){
            keyWordsList.add(k.word);
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
        Intent intent = new Intent(this.getContext(), NewsDetailActivity.class);
        intent.putExtra("newsObject", news);
        startActivity(intent);
    }
}
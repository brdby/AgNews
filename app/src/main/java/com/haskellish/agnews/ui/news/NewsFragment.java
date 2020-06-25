package com.haskellish.agnews.ui.news;

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

import com.haskellish.agnews.NewsApp;
import com.haskellish.agnews.R;
import com.haskellish.agnews.db.DAO.CategoryDAO;
import com.haskellish.agnews.db.DAO.KeywordDao;
import com.haskellish.agnews.db.NewsDB;
import com.haskellish.agnews.db.DAO.RSSDao;
import com.haskellish.agnews.db.entity.Category;
import com.haskellish.agnews.db.entity.Keyword;
import com.haskellish.agnews.db.entity.RSS;
import com.haskellish.agnews.rss.News;
import com.haskellish.agnews.rss.RSSParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsFragment extends ListFragment {

    /**
     * Fragment where all news will be located
     */

    SimpleAdapter adapter;
    ArrayList<HashMap<String, String>> adapterNewsList = new ArrayList<>();
    List<News> newsList = new ArrayList<>();
    List<String> keywordsList = new ArrayList<>();
    List<String> categoriesList = new ArrayList<>();

    Context context;
    NewsDB db;

    class ParsingTask extends AsyncTask<String, Void, Void> {
        /**
         * This class is parsing url presented in execute method
         * @param strings url that will be parsed
         */

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

                    //adding news in list if all checks passed
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

    /**
     * Manually attach context in case when fragment is not attached to an activity
     * @param context context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = NewsApp.getInstance().getDatabase();
        adapter = new SimpleAdapter(getContext(), adapterNewsList,
                R.layout.news_item,
                new String[]{"Header"},
                new int[]{R.id.news_header});
        setListAdapter(adapter);
        setEmptyText(getResources().getString(R.string.empty_news_list));

        getKeywords();
        getCategories();
        getRSS();
    }

    /**
     * Getting all RSS links from database and adding them to ArrayList
     */
    private void getRSS() {
        RSSDao rssDao = db.rssDao();
        List<RSS> links = rssDao.getAll();
        if (!links.isEmpty()) setListShown(false);
        for (RSS rss : links){
            ParsingTask updateTask = new ParsingTask();
            updateTask.execute(rss.url);
        }
    }

    /**
     * Getting all categories from database and adding them to ArrayList
     */
    private void getCategories() {
        CategoryDAO categoryDAO = db.categoryDAO();
        for (Category c : categoryDAO.getAll()){
            categoriesList.add(c.word);
        }
    }

    /**
     * Getting all keywords from database and adding them to ArrayList
     */
    private void getKeywords() {
        KeywordDao keywordDao = db.keywordDao();
        for (Keyword k : keywordDao.getAll()){
            keywordsList.add(k.word);
        }
    }


    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //Start new NewsDetailActivity and pass news to it
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
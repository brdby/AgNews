package com.haskellish.agrinews.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.haskellish.agrinews.R;
import com.haskellish.agrinews.object.News;
import com.haskellish.agrinews.rss.RSSParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsFragment extends ListFragment {
    SimpleAdapter adapter;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    ParsingTask updateTask = new ParsingTask();
    List<News> newsList = new ArrayList<>();

    class ParsingTask extends AsyncTask<Void, Void, Void> {

        public Void doInBackground(Void... objects) {
            HashMap<String, String> map;
            String url = "https://www.androidauthority.com/feed";
            RSSParser rssParser = new RSSParser(url);
            newsList = rssParser.getNews();
            for (News news : newsList){
                map = new HashMap<>();
                map.put("Header", news.getTitle());
                map.put("Content", news.getDescription());
                arrayList.add(map);
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
        /*
        NewsDB db = NewsApp.getInstance().getDatabase();
        RSSDao rssDao = db.rssDao();
        RSS employee = new RSS();
        */

        adapter = new SimpleAdapter(getActivity().getApplicationContext(), arrayList,
                R.layout.news_item,
                new String[]{"Header", "Content"},
                new int[]{R.id.news_header, R.id.news_content});
        setListAdapter(adapter);
        setListShown(false);
        updateTask.execute();
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
        Intent intent = new Intent(this.getActivity(), NewsDetailActivity.class);
        intent.putExtra("newsObject", news);
        startActivity(intent);
    }
}
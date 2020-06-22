package com.haskellish.agrinews.ui.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.haskellish.agrinews.R;
import com.haskellish.agrinews.rss.News;

public class NewsDetailActivity extends Activity {

    private TextView title;
    private TextView description;
    private TextView link;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        title = findViewById(R.id.detail_news_header);
        description = findViewById(R.id.detail_news_content);
        link = findViewById(R.id.detail_news_link);

        Intent i = getIntent();
        News news = (News) i.getSerializableExtra("newsObject");

        if (news != null){
            title.setText(news.getTitle());
            description.setText(news.getDescription());
            link.setText(news.getLink());
        }

    }
}

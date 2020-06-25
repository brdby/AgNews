package com.haskellish.agrinews.ui.news;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.haskellish.agrinews.R;
import com.haskellish.agrinews.rss.News;

import java.io.InputStream;

public class NewsDetailActivity extends Activity {

    private TextView title;
    private TextView description;
    private TextView link;
    private ImageView img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        title = findViewById(R.id.detail_news_header);
        description = findViewById(R.id.detail_news_content);
        link = findViewById(R.id.detail_news_link);
        img = findViewById(R.id.detail_news_image);

        Intent i = getIntent();
        News news = (News) i.getSerializableExtra("newsObject");

        if (news != null){
            title.setText(Html.fromHtml(news.getTitle()));
            description.setText(Html.fromHtml(news.getDescription()));
            link.setText(news.getLink());
            if (!news.getImage_url().equals("")) new DownloadImageTask(img).execute(news.getImage_url());
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageTask(ImageView bmImage) {
            this.imageView = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap image = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                image = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return image;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}

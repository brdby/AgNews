package com.haskellish.agnews.ui.news;

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

import com.haskellish.agnews.R;
import com.haskellish.agnews.rss.News;

import java.io.InputStream;
import java.util.ArrayList;

public class NewsDetailActivity extends Activity {
    /**
     * Activity with detailed news
     */

    private TextView title;
    private TextView description;
    private TextView categories;
    private TextView link;
    private ImageView img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        //initializing views
        title = findViewById(R.id.detail_news_header);
        description = findViewById(R.id.detail_news_content);
        link = findViewById(R.id.detail_news_link);
        img = findViewById(R.id.detail_news_image);
        categories = findViewById(R.id.detail_news_categories);

        //get serialized news
        Intent i = getIntent();
        News news = (News) i.getSerializableExtra("newsObject");
        setNewsContent(news);

    }

    /**
     * Set all content from News object to the views
     * @param news
     */
    private void setNewsContent(News news) {
        if (news != null){
            title.setText(Html.fromHtml(news.getTitle()));
            description.setText(Html.fromHtml(news.getDescription()));
            link.setText(news.getLink());
            ArrayList<String> categoriesList = news.getCategories();
            if (!categoriesList.isEmpty()){
                for (String c : categoriesList){
                    categories.append(c + "; ");
                }
            }
            if (!news.getImage_url().equals("")) new DownloadImageTask(img).execute(news.getImage_url());
        }
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView imageView;

        /**
         * Download image from the pointed URL and set it to the pointed Bitmap
         * @param bmImage Bitmap object where downloaded image will be set
         */

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

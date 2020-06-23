package com.haskellish.agrinews.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.haskellish.agrinews.db.entity.Keyword;
import com.haskellish.agrinews.db.entity.RSS;

@Database(entities = {RSS.class, Keyword.class}, version = 4)
public abstract class NewsDB extends RoomDatabase {
    public abstract RSSDao rssDao();
    public abstract KeywordDao keywordDao();
}

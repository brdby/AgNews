package com.haskellish.agrinews.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.haskellish.agrinews.db.DAO.CategoryDAO;
import com.haskellish.agrinews.db.DAO.KeywordDao;
import com.haskellish.agrinews.db.DAO.RSSDao;
import com.haskellish.agrinews.db.entity.Category;
import com.haskellish.agrinews.db.entity.Keyword;
import com.haskellish.agrinews.db.entity.RSS;

@Database(entities = {RSS.class, Keyword.class, Category.class}, version = 5)
public abstract class NewsDB extends RoomDatabase {
    public abstract RSSDao rssDao();
    public abstract KeywordDao keywordDao();
    public abstract CategoryDAO categoryDAO();
}

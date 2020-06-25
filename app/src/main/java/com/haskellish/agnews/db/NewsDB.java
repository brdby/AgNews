package com.haskellish.agnews.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.haskellish.agnews.db.DAO.CategoryDAO;
import com.haskellish.agnews.db.DAO.KeywordDao;
import com.haskellish.agnews.db.DAO.RSSDao;
import com.haskellish.agnews.db.entity.Category;
import com.haskellish.agnews.db.entity.Keyword;
import com.haskellish.agnews.db.entity.RSS;

@Database(entities = {RSS.class, Keyword.class, Category.class}, version = 5)
public abstract class NewsDB extends RoomDatabase {
    public abstract RSSDao rssDao();
    public abstract KeywordDao keywordDao();
    public abstract CategoryDAO categoryDAO();
}

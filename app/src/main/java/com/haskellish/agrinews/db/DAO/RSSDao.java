package com.haskellish.agrinews.db.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.haskellish.agrinews.db.entity.RSS;

import java.util.List;

@Dao
public interface RSSDao {

    @Query("SELECT * FROM rss")
    List<RSS> getAll();

    @Query("DELETE FROM rss WHERE url = :url")
    void deleteByURL(String url);

    @Insert
    void insert(RSS employee);

    @Update
    void update(RSS employee);

    @Delete
    void delete(RSS employee);

}

package com.haskellish.agnews.db.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.haskellish.agnews.db.entity.Keyword;

import java.util.List;

@Dao
public interface KeywordDao {

    @Query("SELECT * FROM keyword")
    List<Keyword> getAll();

    @Query("DELETE FROM keyword WHERE word = :word")
    void deleteByWord(String word);

    @Insert
    void insert(Keyword employee);

    @Update
    void update(Keyword employee);

    @Delete
    void delete(Keyword employee);

}

package com.haskellish.agrinews.db.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.haskellish.agrinews.db.entity.Category;

import java.util.List;

@Dao
public interface CategoryDAO {

    @Query("SELECT * FROM category")
    List<Category> getAll();

    @Query("DELETE FROM category WHERE word = :word")
    void deleteByWord(String word);

    @Insert
    void insert(Category employee);

    @Update
    void update(Category employee);

    @Delete
    void delete(Category employee);

}
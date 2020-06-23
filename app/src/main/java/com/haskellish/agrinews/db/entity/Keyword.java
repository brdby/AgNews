package com.haskellish.agrinews.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Keyword {

    @PrimaryKey
    @NonNull
    public String word;

}

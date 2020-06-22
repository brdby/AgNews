package com.haskellish.agrinews.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RSS {

    @PrimaryKey
    public long id;

    public String name;

    public String url;

}

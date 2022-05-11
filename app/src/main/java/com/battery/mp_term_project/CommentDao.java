package com.battery.mp_term_project;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM Comment")
    List<Comment> getAllComments();

    @Insert
    void Insert(Content content);

    @Delete
    void Delete(Content content);
}

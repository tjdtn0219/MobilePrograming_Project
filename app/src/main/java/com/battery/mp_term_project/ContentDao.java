package com.battery.mp_term_project;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContentDao {
    @Query("SELECT * FROM Content")
    List<Content> getAllContents();

    @Query("SELECT * FROM Content WHERE id == (:id)")
    List<Content> loadContentsById(int id);

    @Query("SELECT * FROM Comment WHERE id IN (:id)")
    List<Comment> loadCommentById(int[] id);

    @Insert
    void Insert(Content content);

    @Update
    void Update(Content content);

    @Delete
    void Delete(Content content);
}

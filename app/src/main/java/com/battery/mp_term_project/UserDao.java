package com.battery.mp_term_project;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User")
    List<User> getAllUsers();

    @Query("SELECT * FROM User WHERE uid == (:id)")
    List<User> loadUserById(String id);

    @Query("SELECT * FROM Content WHERE writer_id IN (:uid)")
    List<Content> loadContentsById(String[] uid);

    @Insert
    void Insert(User user);

    @Update
    void Update(User user);

    @Delete
    void Delete(User user);
}

package com.battery.mp_term_project;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {User.class,Content.class,Comment.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ContentDao contentDao();
    public abstract CommentDao commentDao();
}

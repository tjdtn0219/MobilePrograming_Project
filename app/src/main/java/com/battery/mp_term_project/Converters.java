package com.battery.mp_term_project;

import android.net.Uri;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public static List<Integer> fromStringToIntegerList(String value) {
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromIntegerList(List<Integer> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static List<Uri> fromStringToUriList(String value) {
        Type listType = new TypeToken<List<Uri>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromUriList(List<Uri> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}

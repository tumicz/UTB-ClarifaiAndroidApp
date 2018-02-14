package com.example.tumal.clarifai.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.tumal.clarifai.model.ClarifaiConcept;
import com.example.tumal.clarifai.model.ClarifaiRequest;
import com.example.tumal.clarifai.model.Settings;

@Database(entities = {ClarifaiRequest.class, ClarifaiConcept.class, Settings.class}, version = 8,exportSchema = false)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ClarifaiDatabaseDAO clarifaiRequestDAO();
}

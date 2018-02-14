package com.example.tumal.clarifai.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.tumal.clarifai.database.DatabaseAsyncDAO;
import com.example.tumal.clarifai.networking.ClarifaiClientInterface;

/**
 * Created by TumaL on 2/13/2018.
 */
@Entity
public class Settings {

    @PrimaryKey
    public long id=1;

    @ColumnInfo
    public String selectedPredictionModel;

}

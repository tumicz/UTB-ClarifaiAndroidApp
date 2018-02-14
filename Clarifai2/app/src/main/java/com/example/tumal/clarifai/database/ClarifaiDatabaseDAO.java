package com.example.tumal.clarifai.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.*;

import com.example.tumal.clarifai.ClarifiaiApplication;
import com.example.tumal.clarifai.model.ClarifaiConcept;
import com.example.tumal.clarifai.model.ClarifaiRequest;
import com.example.tumal.clarifai.model.Settings;

import java.util.List;

/**
 * Created by TumaL on 1/15/2018.
 */
@Dao
public interface ClarifaiDatabaseDAO {
        @Query("SELECT * FROM ClarifaiRequest order by created desc")
        List<ClarifaiRequest> getAll();

        @Query("SELECT * FROM ClarifaiRequest where id = :id")
        ClarifaiRequest getById(long id);

        @Query("SELECT * FROM ClarifaiConcept where idRequest = :idRequest order by conceptFitness")
        List<ClarifaiConcept> getAllByRequestId(long idRequest);

        @Insert
        long[] insertAll(ClarifaiRequest... requests);

        @Insert
        long[] insertAll(ClarifaiConcept... concepts);

        @Delete
        void delete(ClarifaiRequest requests);

        @Query("DELETE FROM ClarifaiRequest")
        public void nukeTable();

        @Update
        void update(ClarifaiRequest cRequest);

        @Query("SELECT * FROM Settings where id=1")
        Settings getSettings();

        @Update
        void saveSettings(Settings s);
}

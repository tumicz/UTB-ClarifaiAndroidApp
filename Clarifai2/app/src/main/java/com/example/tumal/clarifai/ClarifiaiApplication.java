package com.example.tumal.clarifai;

import android.app.Application;
import android.content.Context;

import com.example.tumal.clarifai.database.DatabaseAsyncDAO;
import com.example.tumal.clarifai.model.Settings;
import com.example.tumal.clarifai.networking.ConceptModelFactory;

public class ClarifiaiApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        ClarifiaiApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ClarifiaiApplication.context;
    }

    private static Settings settings=null;

    public static Settings getSettings()
    {
         if(settings==null)
             settings =  DatabaseAsyncDAO.getInstance().databaseDAO.getSettings();
         if(settings==null)
         {
             settings = new Settings();
             settings.selectedPredictionModel=ConceptModelFactory.DEFAULT_MODEL;
             DatabaseAsyncDAO.getInstance().saveSettings(settings);
         }
         return settings;
    }
}

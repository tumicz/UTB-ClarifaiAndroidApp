package com.example.tumal.clarifai.database;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.example.tumal.clarifai.ClarifiaiApplication;
import com.example.tumal.clarifai.Command;
import com.example.tumal.clarifai.model.ClarifaiConcept;
import com.example.tumal.clarifai.model.ClarifaiRequest;
import com.example.tumal.clarifai.model.Settings;

import java.util.List;

public class DatabaseAsyncDAO {
    private static final DatabaseAsyncDAO ourInstance = new DatabaseAsyncDAO();

    public static DatabaseAsyncDAO getInstance() {
        return ourInstance;
    }

    public ClarifaiDatabaseDAO databaseDAO;

    private boolean settingsLoaded=false;
    private Settings loadedSettings;

    private DatabaseAsyncDAO() {
        //database inicialization
        AppDatabase db = Room.databaseBuilder(ClarifiaiApplication.getAppContext(),
                AppDatabase.class, "clarifai_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        databaseDAO =db.clarifaiRequestDAO();
        nukeRequests();

    }

    public void loadRequestsToAdapter(final ArrayAdapter adapter)
    {
        new AsyncTask<Void, Void, List<ClarifaiRequest>>() {
            @Override
            protected List<ClarifaiRequest> doInBackground(Void... params) {
                return databaseDAO.getAll();

            }

            @Override
            protected void onPostExecute(List<ClarifaiRequest> result) {
                adapter.clear();
                adapter.addAll(result);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    public void nukeRequests() {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                databaseDAO.nukeTable();
                return 0;
            }
        }.execute();
    }

    public void saveRequestAndLoadToAdapter(final ClarifaiRequest cr, final ArrayAdapter adapter, final Command onFinishedCallback)
    {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                long[] ids= databaseDAO.insertAll(cr);
                cr.id=ids[0];
                return 0;
            }

            @Override
            protected void onPostExecute(Integer result) {
                if (result == 0) {
                    adapter.insert(cr,0);//add(cr);
                    adapter.notifyDataSetChanged();
                    onFinishedCallback.execute(null);
                }
            }
        }.execute();
    }

    public void GetRequestById(final long id, final Command cmd)
    {
        new AsyncTask<Void, Void, ClarifaiRequest>() {

            @Override
            protected ClarifaiRequest doInBackground(Void... params) {
                return databaseDAO.getById(id);

            }

            @Override
            protected void onPostExecute(ClarifaiRequest result) {
                cmd.execute(result);
            }

        }.execute();
    }

    public void saveCnceptsAndLoadToAdapter( final ArrayAdapter adapter, final Command onFinishedCallback, final ClarifaiConcept... cc)
    {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                long[] ids= databaseDAO.insertAll(cc);
                return 0;
            }

            @Override
            protected void onPostExecute(Integer result) {
                if (result == 0) {
                    adapter.clear();
                    adapter.addAll(cc);
                    adapter.notifyDataSetChanged();
                    onFinishedCallback.execute(null);
                }
            }
        }.execute();
    }
    public void LoadConceptsForRequestToAdapter( final ArrayAdapter adapter, final ClarifaiRequest cr)
    {
        new AsyncTask<Void, Void, List<ClarifaiConcept>>()
        {
            @Override
            protected List<ClarifaiConcept> doInBackground(Void... params) {
                return databaseDAO.getAllByRequestId(cr.id);
            }

            @Override
            protected void onPostExecute(List<ClarifaiConcept> result) {
                    adapter.clear();
                    adapter.addAll(result);
                    adapter.notifyDataSetChanged();
                }
        }.execute();
    }

    public void updateRequest(final ClarifaiRequest cr, final Command<ClarifaiRequest> onUpdated)
    {
        new AsyncTask<Void, Void, ClarifaiRequest>() {

            @Override
            protected ClarifaiRequest doInBackground(Void... params) {
                databaseDAO.update(cr);
                return cr;
            }

            @Override
            protected void onPostExecute(ClarifaiRequest result) {
                onUpdated.execute(result);
            }

        }.execute();
    }

    public void saveSettings(final Settings s)
    {
        loadedSettings=s;
        new AsyncTask<Void, Void, Object>() {

            @Override
            protected Object doInBackground(Void... params) {
                databaseDAO.saveSettings(s);
                return null;
            }

            @Override
            protected void onPostExecute(Object result) {

            }

        }.execute();


    }
}

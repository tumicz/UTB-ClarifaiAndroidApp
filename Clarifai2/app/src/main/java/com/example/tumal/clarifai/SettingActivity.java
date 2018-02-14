package com.example.tumal.clarifai;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.tumal.clarifai.database.DatabaseAsyncDAO;
import com.example.tumal.clarifai.networking.ClarifaiClientInterface;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        Button b = findViewById(R.id.button4);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogCreator.createYesNoDialog("Do you really want to clear all history ?", new Command<Boolean>() {
                    @Override
                    public void execute(Boolean o) {
                        if(o)
                            {
                                DatabaseAsyncDAO.getInstance().nukeRequests();
                                MainActivity.NotifyMainListChanged();
                                Snackbar.make(findViewById(android.R.id.content), "Data cleared.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                    }
                },SettingActivity.this);


            }
        });


        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ClarifaiClientInterface.GetInstance().modelFactory.getModelIdentifiers());

        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String model= (String)spinner.getAdapter().getItem(position);
                ClarifiaiApplication.getSettings().selectedPredictionModel=model;
                DatabaseAsyncDAO.getInstance().databaseDAO.saveSettings(ClarifiaiApplication.getSettings());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }
}

package com.example.tumal.clarifai;

import android.Manifest;
import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.tumal.clarifai.database.*;
import com.example.tumal.clarifai.model.ClarifaiRequest;
import com.example.tumal.clarifai.model.ClarifaiRequestState;
import com.example.tumal.clarifai.model.RequestListAdapter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.gson.internal.$Gson$Types.arrayOf;

public class MainActivity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST =1;
    private ListView listView;
    static RequestListAdapter onlyOneAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Permissions
        CheckPermissions(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET
        });


        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //list view
        listView = (ListView) findViewById(R.id.list_view1);
        onlyOneAdapter = new RequestListAdapter(this, new ArrayList<ClarifaiRequest>());
        listView.setAdapter(onlyOneAdapter);
        NotifyMainListChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                ClarifaiRequest cRequest =(ClarifaiRequest) adapter.getItemAtPosition(position);
                Intent myIntent = new Intent(MainActivity.this, DetailActivity.class);
                myIntent.putExtra("crequest", cRequest.id); //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });

        //floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent myIntent = new Intent(MainActivity.this, SettingActivity.class);
            MainActivity.this.startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE_REQUEST && data !=null){
            Snackbar.make(findViewById(android.R.id.content), "Picture will be analysed soon...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            final ClarifaiRequest cRequest= new ClarifaiRequest();
            cRequest.imageUri=data.getData().toString();
            cRequest.setImageThumbnail(ClarifaiRequest.createThumbnail(Uri.parse(cRequest.imageUri)));
            cRequest.state= ClarifaiRequestState.New;
            cRequest.created=  new Date();
            DatabaseAsyncDAO.getInstance().saveRequestAndLoadToAdapter(cRequest, (ArrayAdapter) listView.getAdapter(), new Command() {
                @Override
                public void execute(Object o) {
                    Snackbar.make(findViewById(android.R.id.content), "Picture saved", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Intent myIntent = new Intent(MainActivity.this, DetailActivity.class);
                    myIntent.putExtra("crequest", cRequest.id); //Optional parameters
                    MainActivity.this.startActivity(myIntent);
                }
            });


        }
    }

    public void CheckPermissions( String[] permissions)
    {
        boolean haveAll=true;
        for (String prm : permissions)
                 haveAll &= ContextCompat.checkSelfPermission(this,prm)== PackageManager.PERMISSION_GRANTED;

        if(!haveAll)
           ActivityCompat.requestPermissions(this, permissions,0);
    }

    public static void NotifyMainListChanged()
    {
        DatabaseAsyncDAO.getInstance().loadRequestsToAdapter(onlyOneAdapter);
    }
}

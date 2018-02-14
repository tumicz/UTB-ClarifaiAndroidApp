package com.example.tumal.clarifai.networking;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ListView;

import com.example.tumal.clarifai.ClarifiaiApplication;
import com.example.tumal.clarifai.Command;
import com.example.tumal.clarifai.R;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiImage;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import okhttp3.OkHttpClient;

public class ClarifaiClientInterface {

    private ClarifaiClient client;
    public ConceptModelFactory modelFactory;

    private static ClarifaiClientInterface instance = null;

    public static ClarifaiClientInterface GetInstance() {
        if (instance == null)
            instance = new ClarifaiClientInterface();
        return instance;
    }

    private ClarifaiClientInterface() {

        client = new ClarifaiBuilder(ClarifiaiApplication.getAppContext().getString((R.string.clarifai_api_key)))
                // Optionally customize HTTP client via a custom OkHttp instance
                .client(new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build())
                .buildSync(); // Increase timeout for poor mobile networks

        modelFactory= new ConceptModelFactory(client);

    }

    public void PredictConcepts(final Bitmap bitmap,final Command cmdOnStart, final Command cmdOnEnd){

        AsyncTask<Void, Void, List<ClarifaiOutput<Concept>>> task = new AsyncTask<Void, Void, List<ClarifaiOutput<Concept>>>() {

            @Override
            protected void onPreExecute()
            {
                cmdOnStart.execute(null);
            }
            @Override
            protected List<ClarifaiOutput<Concept>> doInBackground(Void... params) {

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                return PredictConcepts(stream.toByteArray());

            }

            @Override
            protected void onPostExecute(List<ClarifaiOutput<Concept>> result) {
                cmdOnEnd.execute(result);
            }

        };
        task.execute();

    }

    private List<ClarifaiOutput<Concept>> PredictConcepts(byte[] imageBuffer) {

        // The default Clarifai model that identifies concepts in images

        final ConceptModel generalModel = client.getDefaultModels().generalModel();

        // Use this model to predict, with the image that the user just selected as the input
        return generalModel.predict()
                .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(imageBuffer)))
                .executeSync().get();
    }

}

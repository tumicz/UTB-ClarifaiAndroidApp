package com.example.tumal.clarifai;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tumal.clarifai.database.DatabaseAsyncDAO;
import com.example.tumal.clarifai.model.ClarifaiConcept;
import com.example.tumal.clarifai.model.ClarifaiRequest;
import com.example.tumal.clarifai.model.ClarifaiRequestState;
import com.example.tumal.clarifai.model.ConceptsListAdapter;
import com.example.tumal.clarifai.networking.ClarifaiClientInterface;

import java.util.ArrayList;
import java.util.List;

import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import steelkiwi.com.library.DotsLoaderView;

public class DetailActivity extends AppCompatActivity {

    DotsLoaderView dotsLoaderView;

    private ListView listView2;
    private TextView textView5;

    ConceptsListAdapter adapter;

    Command<ClarifaiRequest> onRequestUpdatedHandler = new Command<ClarifaiRequest>() {
        @Override
        public void execute(ClarifaiRequest o) {
            MainActivity.NotifyMainListChanged();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dotsLoaderView= (DotsLoaderView)findViewById(R.id.dots);

        textView5=(TextView)findViewById(R.id.textView5);

        listView2 = (ListView) findViewById(R.id.listView2);
        adapter = new ConceptsListAdapter(this, new ArrayList<ClarifaiConcept>());
        listView2.setAdapter(adapter);

        //prevzeti pozadavku
        Bundle bundle = getIntent().getExtras();
        long id=bundle.getLong("crequest");
        if(id>0) LoadPictureData(id);


    }

    private void LoadPictureData(long id)
    {
        DatabaseAsyncDAO.getInstance().GetRequestById(id, new Command() {
            @Override
            public void execute(Object o) {
                final ClarifaiRequest clarifaiRequest= (ClarifaiRequest) o;
                ImageView imageView2 = (ImageView) DetailActivity.this.findViewById(R.id.imageView2);
                imageView2.setImageBitmap(clarifaiRequest.getImageThumbnailBitmap());

                if(clarifaiRequest.state== ClarifaiRequestState.New)
                {
                    startAnalysis(clarifaiRequest);
                }
                else if(clarifaiRequest.state== ClarifaiRequestState.Proceesing)
                {
                    DialogCreator.createYesNoDialog("Analysis probably hanged...Do you want to try again ?", new Command<Boolean>() {
                        @Override
                        public void execute(Boolean o) {
                            if(o)
                                startAnalysis(clarifaiRequest);
                        }
                    },DetailActivity.this);
                }
                else if(clarifaiRequest.state== ClarifaiRequestState.Done)
                {
                    DatabaseAsyncDAO.getInstance().LoadConceptsForRequestToAdapter(adapter,clarifaiRequest);
                }
            }
        });
    }

    private void startAnalysis(final ClarifaiRequest c)
    {
        c.state=ClarifaiRequestState.Proceesing;
        c.modelName=ClarifiaiApplication.getSettings().selectedPredictionModel;
        DatabaseAsyncDAO.getInstance().updateRequest(c,onRequestUpdatedHandler);

        ClarifaiClientInterface.GetInstance().PredictConcepts(
                c.getImage(),
                new Command()//command ond start
                {
                    @Override
                    public void execute(Object o)
                    {
                        showLoader();
                    }
                },
                new Command()//command on complete
                {
                    @Override
                    public void execute(Object o)
                    {
                        ClarifaiConcept [] finalList =ClarifaiConcept.createFromPrediction((List<ClarifaiOutput<Concept>>)o,c);
                        DatabaseAsyncDAO.getInstance().saveCnceptsAndLoadToAdapter(adapter, new Command() {
                                    @Override
                                    public void execute(Object o) {

                                        c.state=ClarifaiRequestState.Done;
                                        DatabaseAsyncDAO.getInstance().updateRequest(c, onRequestUpdatedHandler);
                                        hideLoader();
                                    }
                                },
                                finalList);

                    }
                }
        );
    }

    private void showLoader()
    {
        dotsLoaderView.show();
        listView2.setVisibility(View.GONE);
        textView5.setText(R.string.detail_divisor_loading);
    }

    private void hideLoader()
    {
        dotsLoaderView.hide();
        listView2.setVisibility(View.VISIBLE);
        textView5.setText(R.string.detail_divisor);
    }

}

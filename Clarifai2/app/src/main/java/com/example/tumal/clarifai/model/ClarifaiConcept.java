package com.example.tumal.clarifai.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

/**
 * Created by TumaL on 1/29/2018.
 */

@Entity
public class ClarifaiConcept {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo
    public long idRequest;

    @ColumnInfo
    public String ModelName;

    public String conceptClass;

    public double conceptFitness;

    public static ClarifaiConcept[] createFromPrediction (List<ClarifaiOutput<Concept>> prediction, ClarifaiRequest clarifaiRequest)
    {
        ArrayList<ClarifaiConcept> finalList = new ArrayList<ClarifaiConcept>();

        for (ClarifaiOutput<Concept> cOutput : prediction)
        {
            for(Concept c :cOutput.data())
            {
                ClarifaiConcept clarifaiConcept = new ClarifaiConcept();
                clarifaiConcept.conceptClass=c.name();
                clarifaiConcept.conceptFitness=c.value();
                clarifaiConcept.idRequest=clarifaiRequest.id;
                clarifaiConcept.ModelName=cOutput.model().name();
                finalList.add(clarifaiConcept);
            }
        }
        ClarifaiConcept arr[] = new ClarifaiConcept[finalList.size()];
        finalList.toArray(arr);
        return arr;
    }
}

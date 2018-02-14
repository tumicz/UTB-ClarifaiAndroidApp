package com.example.tumal.clarifai.networking;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import clarifai2.api.ClarifaiClient;
import clarifai2.dto.model.ConceptModel;

/**
 * Created by TumaL on 2/13/2018.
 */

public class ConceptModelFactory {

    private HashMap<String,ConceptModel> map = new HashMap<String,ConceptModel>();

    public static final String DEFAULT_MODEL="GENERAL";

    public ConceptModelFactory(ClarifaiClient clarifaiClient)
    {
        map.put(DEFAULT_MODEL, clarifaiClient.getDefaultModels().generalModel());
        map.put("APPAREL", clarifaiClient.getDefaultModels().apparelModel());
        map.put("FOOD", clarifaiClient.getDefaultModels().foodModel());
        map.put("MODERATION", clarifaiClient.getDefaultModels().moderationModel());
        map.put("TRAVEL", clarifaiClient.getDefaultModels().travelModel());
        map.put("WEDDING", clarifaiClient.getDefaultModels().weddingModel());
    }

    public ConceptModel getModelByIdentifier(String identifier)
    {
        return map.get(identifier);
    }
    public String getModelIdentifier(ConceptModel cm)
    {
        for (Map.Entry<String,ConceptModel> entry : map.entrySet())
        {
            if(entry.getValue().equals(cm))
                return entry.getKey();
        }
        return null;
    }

    public String[] getModelIdentifiers()
    {
        String[] strings = new String[map.size()];
        map.keySet().toArray(strings);
        return strings;
    }

}

package com.example.tumal.clarifai.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tumal.clarifai.R;

import java.util.ArrayList;

/**
 * Created by TumaL on 1/29/2018.
 */

public class ConceptsListAdapter extends ArrayAdapter<ClarifaiConcept> {
private final Context context;
private final ArrayList<ClarifaiConcept> values;


public ConceptsListAdapter(Context context, ArrayList<ClarifaiConcept> values){
        super(context,-1,values);
        this.context=context;
        this.values=values;
        }

@Override
public View getView(int position, View convertView, ViewGroup parent){

        ClarifaiConcept cc=values.get(position);
        LayoutInflater inflater=(LayoutInflater)context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.concept_row_layout,parent,false);
        TextView textView2=(TextView)rowView.findViewById(R.id.textView2);
        TextView textView3=(TextView)rowView.findViewById(R.id.textView3);

        textView2.setText(cc.conceptClass);
        textView3.setText(String.format("%.3f", cc.conceptFitness));

        return rowView;
        }
        }

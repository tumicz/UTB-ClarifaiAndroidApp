package com.example.tumal.clarifai.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tumal.clarifai.ClarifiaiApplication;
import com.example.tumal.clarifai.R;
import com.example.tumal.clarifai.database.DatabaseAsyncDAO;
import com.example.tumal.clarifai.networking.ClarifaiClientInterface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import clarifai2.dto.model.ConceptModel;

public class RequestListAdapter extends ArrayAdapter<ClarifaiRequest> {
    private final Context context;
    private final ArrayList<ClarifaiRequest> values;


    public RequestListAdapter(Context context, ArrayList<ClarifaiRequest> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ClarifaiRequest cr=values.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.request_row_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.textView);
        TextView textView4 = (TextView) rowView.findViewById(R.id.textView4);
        TextView textView10 = (TextView) rowView.findViewById(R.id.textView10);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);

        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd. MM. yyyy HH:mm:ss");
        String stringDate= simpleDateFormat.format(cr.created);
        imageView.setImageBitmap(cr.getImageThumbnailBitmap());
        textView.setText(stringDate);

        ConceptModel cm=ClarifaiClientInterface.GetInstance().modelFactory.getModelByIdentifier(cr.modelName);
        String modelName= cm==null ? cr.modelName:cm.name();
        textView4.setText(modelName);

        textView10.setText(ClarifaiRequestState.toString(cr.state));

        return rowView;
    }

}
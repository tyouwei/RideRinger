package com.example.rideringer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MrtLrtAdapter extends ArrayAdapter<MrtLrtModelClass> {
    private ArrayList<MrtLrtModelClass> mrtLrtModelClassArrayList;
    Context context;

    public MrtLrtAdapter(ArrayList<MrtLrtModelClass> data, Context context) {
        super(context, R.layout.mrtlrt_drop_down_item, data);
        this.mrtLrtModelClassArrayList = data;
        this.context = context;
    }

    // View Lookup Cache
    private static class ViewHolder {
        TextView txtStnName;
        ImageView stnLogoImg;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get data item for this position
        MrtLrtModelClass dataModel = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.mrtlrt_drop_down_item,
                    parent, false);

            viewHolder.txtStnName = (TextView) convertView.findViewById(R.id.stn_name);
            viewHolder.stnLogoImg = (ImageView) convertView.findViewById(R.id.stn_logo);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        // Get data from model class
        viewHolder.txtStnName.setText(dataModel.getStnName());
        viewHolder.stnLogoImg.setImageResource(dataModel.getStnLogo());

        return convertView;
    }

}

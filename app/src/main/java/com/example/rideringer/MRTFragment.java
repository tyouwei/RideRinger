package com.example.rideringer;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MRTFragment extends Fragment {
    private ArrayList<MrtLrtModelClass> mrt = new ArrayList<>();
    AutoCompleteTextView autoCompleteTextView;
    MrtLrtAdapter adapterItems;
    private Database db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_m_r_t, container, false);
        this.db = new Database(getActivity());
        v.findViewById(R.id.mrt_save).setOnClickListener(onSave);
        v.findViewById(R.id.mrt_alarm).setOnClickListener(onAlarm);

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            getContext().getAssets().open("mrt_lrt_stations.csv"), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                String[] row = mLine.split(",");
                int stnLogo = row[1].equals("EWL") ?
                        R.drawable.ewl_img : row[1].equals("NSL") ?
                        R.drawable.nsl_img : row[1].equals("NEl") ?
                        R.drawable.nel_img : row[1].equals("CCL") ?
                        R.drawable.ccl_img : row[1].equals("DTL") ?
                        R.drawable.dtl_img : row[1].equals("TEL") ?
                        R.drawable.tel_img : row[1].equals("BPL") ?
                        R.drawable.bp_img : row[1].equals("SKL") ?
                        R.drawable.sk_img : R.drawable.pg_img;
                mrt.add(new MrtLrtModelClass(row[2] + " " + row[3], stnLogo));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        autoCompleteTextView = v.findViewById(R.id.mrt_drop_list);
        adapterItems = new MrtLrtAdapter(mrt, getContext());
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(onClick);
        return v;
    }

    @Override
    public void onDestroyView() {
        db.close();
        super.onDestroyView();
    }

    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //db.insert(nameStr, addStr);
        }
    };

    private View.OnClickListener onAlarm = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Set Alarm
        }
    };

    private AdapterView.OnItemClickListener onClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String item = parent.getItemAtPosition(position).toString();
        }
    };
}
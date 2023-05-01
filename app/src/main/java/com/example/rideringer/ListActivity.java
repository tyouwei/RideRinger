package com.example.rideringer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivity extends AppCompatActivity {
    private Cursor model = null;
    private LocationAdapter adapter = null;
    private ListView list;
    private Database db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        db = new Database(this);
        list = findViewById(R.id.locationlist);
        model = db.getAll();
        adapter = new LocationAdapter(this, model, 0);
        list.setAdapter(adapter);

        list.setOnItemClickListener(onListClick);
    }

    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //CREATE A POP UP TO CONFIRM SETTING OF ALARM
        }
    };

    private static class LocationHolder {
        private TextView locationName = null;
        private TextView locationAddress = null;

        LocationHolder(View row) {
            locationName = row.findViewById(R.id.row_location);
            locationAddress = row.findViewById(R.id.row_address);
        }

        void populateFrom(Cursor c, Database helper) {
            locationName.setText(helper.getLocationName(c));
            locationAddress.setText(helper.getLocationAddress(c));
        }
    }
    private class LocationAdapter extends CursorAdapter {
        LocationAdapter(Context context, Cursor cursor, int flags){
            super(context, cursor, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.list_row, parent, false);
            LocationHolder holder = new LocationHolder(row);
            row.setTag(holder);
            return row;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            LocationHolder holder = (LocationHolder) view.getTag();
            holder.populateFrom(cursor, db);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (model != null) {
            model.close();
        }
        model = db.getAll();
        adapter.swapCursor(model);
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
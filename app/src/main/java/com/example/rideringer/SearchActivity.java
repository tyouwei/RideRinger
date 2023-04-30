package com.example.rideringer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchActivity extends AppCompatActivity {
    private EditText name;
    private EditText addr;
    private Button button;
    private Database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.name = findViewById(R.id.test_name);
        this.addr = findViewById(R.id.test_addr);
        this.button = findViewById(R.id.test_button);
        this.db = new Database(this);

        this.button.setOnClickListener(onSave);
    }

    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String nameStr = name.getText().toString();
            String addrStr = addr.getText().toString();
            db.insert(nameStr, addrStr);
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
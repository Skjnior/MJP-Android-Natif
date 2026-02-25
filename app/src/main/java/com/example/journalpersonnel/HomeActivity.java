package com.example.journalpersonnel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnViewList = findViewById(R.id.btnViewList);
        Button btnAddNew = findViewById(R.id.btnAddNew);

        btnViewList.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnAddNew.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddEditJournalActivity.class);
            startActivity(intent);
        });
    }
}

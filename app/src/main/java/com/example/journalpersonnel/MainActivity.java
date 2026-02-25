package com.example.journalpersonnel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.journalpersonnel.adapter.JournalAdapter;
import com.example.journalpersonnel.db.JournalDbHelper;
import com.example.journalpersonnel.details.DetailActivity;
import com.example.journalpersonnel.entity.JournalEntity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final ArrayList<JournalEntity> entries = new ArrayList<>();
    private JournalAdapter adapter;
    private JournalDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new JournalDbHelper(this);

        ListView lvEntrees = findViewById(R.id.lvEntrees);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        adapter = new JournalAdapter(this, new JournalAdapter.OnEntryMenuClickListener() {
            @Override
            public void onDetail(JournalEntity entry) {
                openDetail(entry);
            }

            @Override
            public void onEdit(JournalEntity entry, int position) {
                openEdit(entry);
            }

            @Override
            public void onDelete(int position) {
                confirmDelete(position);
            }
        });

        lvEntrees.setAdapter(adapter);

        if (fabAdd != null) {
            fabAdd.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AddEditJournalActivity.class);
                startActivity(intent);
            });
        }

        lvEntrees.setOnItemClickListener((parent, view, position, id) -> {
            JournalEntity entry = adapter.getItem(position);
            if (entry != null)
                openDetail(entry);
        });

        lvEntrees.setOnItemLongClickListener((parent, view, position, id) -> {
            showActionsDialog(position);
            return true;
        });

        SearchView searchView = findViewById(R.id.searchView);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    adapter.filter(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.filter(newText);
                    return true;
                }
            });
        }

        loadFromDb();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFromDb();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null)
            db.close();
    }

    private void loadFromDb() {
        entries.clear();
        entries.addAll(db.getAll());
        adapter.setData(new ArrayList<>(entries));
    }

    private void openDetail(JournalEntity entity) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("entity", entity);
        startActivity(intent);
    }

    private void openEdit(JournalEntity entity) {
        Intent intent = new Intent(this, AddEditJournalActivity.class);
        intent.putExtra("entity", entity);
        startActivity(intent);
    }

    private void showActionsDialog(int position) {
        JournalEntity selected = adapter.getItem(position);
        if (selected == null)
            return;

        String[] actions = { "Détails", "Modifier", "Supprimer" };

        new AlertDialog.Builder(this)
                .setTitle("Choisir une action")
                .setItems(actions, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            openDetail(selected);
                            break;
                        case 1:
                            openEdit(selected);
                            break;
                        case 2:
                            confirmDelete(position);
                            break;
                    }
                })
                .show();
    }

    private void confirmDelete(int position) {
        JournalEntity e = adapter.getItem(position);
        if (e == null)
            return;

        new AlertDialog.Builder(this)
                .setTitle("Supprimer")
                .setMessage("Voulez-vous supprimer cet journal ?")
                .setPositiveButton("Oui", (d, w) -> {
                    db.delete(e.getId());
                    loadFromDb();
                    Toast.makeText(this, "Journal supprimé", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Non", null)
                .show();
    }
}

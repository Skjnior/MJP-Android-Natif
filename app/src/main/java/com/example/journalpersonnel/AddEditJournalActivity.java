package com.example.journalpersonnel;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.journalpersonnel.db.JournalDbHelper;
import com.example.journalpersonnel.entity.JournalEntity;

import java.util.ArrayList;

public class AddEditJournalActivity extends AppCompatActivity {

    private EditText etTitre, etContenu;
    private DatePicker datePicker;
    private CheckBox cbPersonnel, cbTravail, cbVoyage;
    private Spinner spImage;
    private JournalDbHelper db;
    private JournalEntity existingEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        db = new JournalDbHelper(this);

        TextView tvTitleAction = findViewById(R.id.tvTitleAction);
        etTitre = findViewById(R.id.etTitre);
        etContenu = findViewById(R.id.etContenu);
        datePicker = findViewById(R.id.datePicker);
        cbPersonnel = findViewById(R.id.cbPersonnel);
        cbTravail = findViewById(R.id.cbTravail);
        cbVoyage = findViewById(R.id.cbVoyage);
        spImage = findViewById(R.id.spImage);
        Button btnSave = findViewById(R.id.btnSave);

        String[] iconNames = { "Standard", "Agenda", "Calendrier" };
        int[] iconRes = {
                android.R.drawable.ic_menu_edit,
                android.R.drawable.ic_menu_agenda,
                android.R.drawable.ic_menu_my_calendar
        };

        IconSpinnerAdapter adapter = new IconSpinnerAdapter(iconNames, iconRes);
        spImage.setAdapter(adapter);

        // Vérifier si c'est une modification
        existingEntity = (JournalEntity) getIntent().getSerializableExtra("entity");
        if (existingEntity != null) {
            tvTitleAction.setText("Modifier le journal");
            etTitre.setText(existingEntity.getTitre());
            etContenu.setText(existingEntity.getContenu());

            try {
                String[] d = existingEntity.getDate().split("/");
                datePicker.updateDate(Integer.parseInt(d[2]), Integer.parseInt(d[1]) - 1, Integer.parseInt(d[0]));
            } catch (Exception ignored) {
            }

            ArrayList<String> tags = existingEntity.getTags() == null ? new ArrayList<>() : existingEntity.getTags();
            cbPersonnel.setChecked(tags.contains("Personnel"));
            cbTravail.setChecked(tags.contains("Travail"));
            cbVoyage.setChecked(tags.contains("Voyage"));

            spImage.setSelection(reverseMapImage(existingEntity.getImageResId()));
        }

        btnSave.setOnClickListener(v -> saveJournal());
    }

    private void saveJournal() {
        String titre = etTitre.getText().toString().trim();
        String contenu = etContenu.getText().toString().trim();

        if (titre.isEmpty()) {
            etTitre.setError("Le titre est obligatoire");
            Toast.makeText(this, "Veuillez renseigner le titre", Toast.LENGTH_SHORT).show();
            return;
        }
        etTitre.setError(null);

        if (contenu.isEmpty()) {
            etContenu.setError("Le contenu est obligatoire");
            Toast.makeText(this, "Veuillez renseigner le contenu", Toast.LENGTH_SHORT).show();
            return;
        }
        etContenu.setError(null);

        boolean hasTag = cbPersonnel.isChecked() || cbTravail.isChecked() || cbVoyage.isChecked();
        if (!hasTag) {
            Toast.makeText(this, "Veuillez sélectionner au moins un tag (Personnel, Travail ou Voyage)", Toast.LENGTH_LONG).show();
            return;
        }

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        String date = String.format("%02d/%02d/%04d", day, month, year);

        ArrayList<String> tags = new ArrayList<>();
        if (cbPersonnel.isChecked())
            tags.add("Personnel");
        if (cbTravail.isChecked())
            tags.add("Travail");
        if (cbVoyage.isChecked())
            tags.add("Voyage");

        int imgRes = mapImage(spImage.getSelectedItemPosition());

        if (existingEntity == null) {
            JournalEntity newEntity = new JournalEntity(date, titre, contenu, tags, imgRes);
            db.insert(newEntity);
            Toast.makeText(this, "Journal créé avec succès", Toast.LENGTH_SHORT).show();
        } else {
            JournalEntity updated = new JournalEntity(existingEntity.getId(), date, titre, contenu, tags, imgRes);
            db.update(updated);
            Toast.makeText(this, "Journal modifié avec succès", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private int mapImage(int pos) {
        if (pos == 1)
            return android.R.drawable.ic_menu_agenda;
        if (pos == 2)
            return android.R.drawable.ic_menu_my_calendar;
        return android.R.drawable.ic_menu_edit;
    }

    private int reverseMapImage(int resId) {
        if (resId == android.R.drawable.ic_menu_agenda)
            return 1;
        if (resId == android.R.drawable.ic_menu_my_calendar)
            return 2;
        return 0;
    }

    private class IconSpinnerAdapter extends BaseAdapter {
        private String[] names;
        private int[] icons;

        public IconSpinnerAdapter(String[] names, int[] icons) {
            this.names = names;
            this.icons = icons;
        }

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return names[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(AddEditJournalActivity.this)
                        .inflate(R.layout.spinner_item_with_icon, parent, false);
            }
            ImageView iv = convertView.findViewById(R.id.ivIcon);
            TextView tv = convertView.findViewById(R.id.tvName);
            iv.setImageResource(icons[position]);
            tv.setText(names[position]);
            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getView(position, convertView, parent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null)
            db.close();
    }
}

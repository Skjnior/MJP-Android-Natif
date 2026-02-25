package com.example.journalpersonnel.details;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.journalpersonnel.R;
import com.example.journalpersonnel.entity.JournalEntity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        JournalEntity entity = (JournalEntity) getIntent().getSerializableExtra("entity");

        TextView tvDate = findViewById(R.id.tvDetailDate);
        TextView tvTitre = findViewById(R.id.tvDetailTitre);
        TextView tvTags = findViewById(R.id.tvDetailTags);
        TextView tvContenu = findViewById(R.id.tvDetailContenu);
        ImageView ivHeaderIcon = findViewById(R.id.ivDetailHeader);
        TextView tvIconName = findViewById(R.id.tvDetailIconName);

        if (entity != null) {
            tvDate.setText(entity.getDate());
            tvTitre.setText(entity.getTitre());
            tvTags.setText(entity.getTagsText());
            tvContenu.setText(entity.getContenu());

            if (ivHeaderIcon != null) {
                ivHeaderIcon.setImageResource(entity.getImageResId());
            }
            if (tvIconName != null) {
                tvIconName.setText(mapResToName(entity.getImageResId()));
            }
        }
    }

    private String mapResToName(int resId) {
        if (resId == android.R.drawable.ic_menu_agenda)
            return "Agenda";
        if (resId == android.R.drawable.ic_menu_my_calendar)
            return "Calendrier";
        return "Standard";
    }
}

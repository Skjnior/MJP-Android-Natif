package com.example.journalpersonnel.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.journalpersonnel.entity.JournalEntity;

import java.util.ArrayList;

public class JournalDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "journal.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE = "entries";
    public static final String COL_ID = "_id";
    public static final String COL_DATE = "date";
    public static final String COL_TITRE = "titre";
    public static final String COL_CONTENU = "contenu";
    public static final String COL_TAGS = "tags";
    public static final String COL_IMAGE = "imageRes";

    public JournalDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DATE + " TEXT NOT NULL, "
                + COL_TITRE + " TEXT NOT NULL, "
                + COL_CONTENU + " TEXT NOT NULL, "
                + COL_TAGS + " TEXT, "
                + COL_IMAGE + " INTEGER NOT NULL"
                + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    // --- CRUD ---
    public long insert(JournalEntity e) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_DATE, e.getDate());
        cv.put(COL_TITRE, e.getTitre());
        cv.put(COL_CONTENU, e.getContenu());
        cv.put(COL_TAGS, e.getTagsText().equals("Aucun tag") ? "" : e.getTagsText());
        cv.put(COL_IMAGE, e.getImageResId());
        return db.insert(TABLE, null, cv);
    }

    public int update(JournalEntity e) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_DATE, e.getDate());
        cv.put(COL_TITRE, e.getTitre());
        cv.put(COL_CONTENU, e.getContenu());
        cv.put(COL_TAGS, e.getTagsText().equals("Aucun tag") ? "" : e.getTagsText());
        cv.put(COL_IMAGE, e.getImageResId());
        return db.update(TABLE, cv, COL_ID + "=?", new String[]{String.valueOf(e.getId())});
    }

    public int delete(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public ArrayList<JournalEntity> getAll() {
        ArrayList<JournalEntity> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(TABLE, null, null, null, null, null, COL_ID + " DESC");
        try {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow(COL_ID));
                String date = c.getString(c.getColumnIndexOrThrow(COL_DATE));
                String titre = c.getString(c.getColumnIndexOrThrow(COL_TITRE));
                String contenu = c.getString(c.getColumnIndexOrThrow(COL_CONTENU));
                String tagsStr = c.getString(c.getColumnIndexOrThrow(COL_TAGS));
                int img = c.getInt(c.getColumnIndexOrThrow(COL_IMAGE));

                ArrayList<String> tags = new ArrayList<>();
                if (tagsStr != null && !tagsStr.trim().isEmpty() && !tagsStr.equals("Aucun tag")) {
                    String[] parts = tagsStr.split(",");
                    for (String p : parts) tags.add(p.trim());
                }

                JournalEntity e = new JournalEntity(id, date, titre, contenu, tags, img);
                list.add(e);
            }
        } finally {
            c.close();
        }
        return list;
    }
}

//package com.example.journalpersonnel.entity;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//
//public class JournalEntity implements Serializable {
//
//    private final String date;
//    private final String titre;
//    private final String contenu;
//    private final ArrayList<String> tags;
//
//    // 🔹 Image associée à l'entrée
//    private int imageResId;
//
//    public JournalEntity(
//            String date,
//            String titre,
//            String contenu,
//            ArrayList<String> tags,
//            int imageResId
//    ) {
//        this.date = date;
//        this.titre = titre;
//        this.contenu = contenu;
//        this.tags = tags;
//        this.imageResId = imageResId;
//    }
//
//    public String getDate() { return date; }
//    public String getTitre() { return titre; }
//    public String getContenu() { return contenu; }
//    public ArrayList<String> getTags() { return tags; }
//
//    // 🔹 Image
//    public int getImageResId() {
//        return imageResId;
//    }
//
//    public void setImageResId(int imageResId) {
//        this.imageResId = imageResId;
//    }
//
//    public String getTagsText() {
//        if (tags == null || tags.isEmpty()) return "Aucun tag";
//        return String.join(", ", tags);
//    }
//}
package com.example.journalpersonnel.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class JournalEntity implements Serializable {
    private long id; // SQLite primary key
    private final String date;
    private final String titre;
    private final String contenu;
    private final ArrayList<String> tags;
    private final int imageResId;

    public JournalEntity(long id, String date, String titre, String contenu, ArrayList<String> tags, int imageResId) {
        this.id = id;
        this.date = date;
        this.titre = titre;
        this.contenu = contenu;
        this.tags = tags;
        this.imageResId = imageResId;
    }

    // Pour créer avant insertion (id inconnu)
    public JournalEntity(String date, String titre, String contenu, ArrayList<String> tags, int imageResId) {
        this(0, date, titre, contenu, tags, imageResId);
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getDate() { return date; }
    public String getTitre() { return titre; }
    public String getContenu() { return contenu; }
    public ArrayList<String> getTags() { return tags; }

    public int getImageResId() { return imageResId; }

    public String getTagsText() {
        if (tags == null || tags.isEmpty()) return "Aucun tag";
        return String.join(", ", tags);
    }
}

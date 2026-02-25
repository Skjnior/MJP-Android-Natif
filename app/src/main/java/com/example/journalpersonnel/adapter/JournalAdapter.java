//package com.example.journalpersonnel.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.PopupMenu;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.example.journalpersonnel.R;
//import com.example.journalpersonnel.entity.JournalEntity;
//
//import java.util.List;
//
//public class JournalAdapter extends ArrayAdapter<JournalEntity> {
//
//    public interface OnEntryMenuClickListener {
//        void onDetail(JournalEntity entry);
//        void onEdit(JournalEntity entry, int position);
//        void onDelete(int position);
//    }
//
//    private final OnEntryMenuClickListener listener;
//
//    public JournalAdapter(
//            @NonNull Context context,
//            @NonNull List<JournalEntity> objects,
//            @NonNull OnEntryMenuClickListener listener
//    ) {
//        super(context, 0, objects);
//        this.listener = listener;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View v = convertView;
//        if (v == null) {
//            v = LayoutInflater.from(getContext()).inflate(R.layout.item_journal, parent, false);
//        }
//
//        JournalEntity entry = getItem(position);
//
//        TextView tvDateTitre = v.findViewById(R.id.tvDateTitre);
//        TextView tvTags = v.findViewById(R.id.tvTags);
//        ImageView ivIcon = v.findViewById(R.id.ivIcon);
//        ImageButton btnMenu = v.findViewById(R.id.btnMenu);
//
//        if (entry != null) {
//            tvDateTitre.setText(entry.getDate() + " - " + entry.getTitre());
//            tvTags.setText(entry.getTagsText());
//            ivIcon.setImageResource(entry.getImageResId());
//
//            btnMenu.setOnClickListener(view -> {
//                PopupMenu popup = new PopupMenu(getContext(), view);
//                popup.getMenuInflater().inflate(R.menu.menu_entry, popup.getMenu());
//                popup.setOnMenuItemClickListener(item -> {
//                    int id = item.getItemId();
//                    if (id == R.id.action_detail) {
//                        listener.onDetail(entry);
//                        return true;
//                    } else if (id == R.id.action_edit) {
//                        listener.onEdit(entry, position);
//                        return true;
//                    } else if (id == R.id.action_delete) {
//                        listener.onDelete(position);
//                        return true;
//                    }
//                    return false;
//                });
//                popup.show();
//            });
//        }
//
//        return v;
//    }
//}
package com.example.journalpersonnel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;

import com.example.journalpersonnel.R;
import com.example.journalpersonnel.entity.JournalEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JournalAdapter extends BaseAdapter {

    public interface OnEntryMenuClickListener {
        void onDetail(JournalEntity entry);
        void onEdit(JournalEntity entry, int position);
        void onDelete(int position);
    }

    private final Context context;
    private final OnEntryMenuClickListener listener;

    private final ArrayList<JournalEntity> allItems = new ArrayList<>();
    private final ArrayList<JournalEntity> visibleItems = new ArrayList<>();

    public JournalAdapter(Context context, OnEntryMenuClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setData(List<JournalEntity> data) {
        allItems.clear();
        allItems.addAll(data);

        visibleItems.clear();
        visibleItems.addAll(data);

        notifyDataSetChanged();
    }

    public void filter(String query) {
        String q = (query == null) ? "" : query.trim().toLowerCase(Locale.ROOT);

        visibleItems.clear();

        if (q.isEmpty()) {
            visibleItems.addAll(allItems);
        } else {
            for (JournalEntity e : allItems) {
                String hay = (e.getTitre() + " " + e.getContenu() + " " + e.getDate() + " " + e.getTagsText())
                        .toLowerCase(Locale.ROOT);
                if (hay.contains(q)) visibleItems.add(e);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return visibleItems.size();
    }

    @Override
    public JournalEntity getItem(int position) {
        return visibleItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return visibleItems.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.item_journal, parent, false);
        }

        JournalEntity entry = getItem(position);

        ImageView ivIcon = v.findViewById(R.id.ivIcon);
        TextView tvDateTitre = v.findViewById(R.id.tvDateTitre);
        TextView tvTags = v.findViewById(R.id.tvTags);
        ImageButton btnMenu = v.findViewById(R.id.btnMenu);

        ivIcon.setImageResource(entry.getImageResId());
        tvDateTitre.setText(entry.getDate() + " - " + entry.getTitre());
        tvTags.setText(entry.getTagsText());

        btnMenu.setOnClickListener(view -> {
            PopupMenu menu = new PopupMenu(context, btnMenu);
            menu.getMenuInflater().inflate(R.menu.menu_entry, menu.getMenu());

            menu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.action_detail) {
                    listener.onDetail(entry);
                    return true;
                } else if (id == R.id.action_edit) {
                    listener.onEdit(entry, position);
                    return true;
                } else if (id == R.id.action_delete) {
                    listener.onDelete(position);
                    return true;
                }
                return false;
            });

            menu.show();
        });

        return v;
    }
}

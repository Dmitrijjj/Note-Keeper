package com.dimidroid.notekeeper.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    public Note(String title, String description, String date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    private final String title;

    private final String description;

    private final String date;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }
}

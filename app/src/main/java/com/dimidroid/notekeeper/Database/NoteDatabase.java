package com.dimidroid.notekeeper.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dimidroid.notekeeper.Dao.NoteDao;
import com.dimidroid.notekeeper.Model.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public abstract NoteDao dao();

    public static NoteDatabase instance;

    public static synchronized NoteDatabase getInstance(Context context){

        if (instance == null){

            instance = Room.databaseBuilder(
                    context, NoteDatabase.class, "Note_DB")
                    .fallbackToDestructiveMigration().build();
        }

        return instance;

    }

}

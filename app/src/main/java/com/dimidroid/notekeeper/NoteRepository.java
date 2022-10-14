package com.dimidroid.notekeeper;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {

    private final NoteDao dao;

    LiveData<List<Note>> noteList;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public NoteRepository(Application application){

        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        dao = noteDatabase.dao();
        noteList = dao.getAllNotes();

    }

    public void add(Note note){

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                dao.add(note);
            }
        });

    }

    public void edit(Note note){

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                dao.edit(note);
            }
        });

    }

    public void delete(Note note){

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(note);
            }
        });

    }

    LiveData<List<Note>> getAllNotes(){

        return noteList;

    }
}
